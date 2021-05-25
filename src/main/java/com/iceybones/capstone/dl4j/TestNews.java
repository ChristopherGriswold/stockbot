/* *****************************************************************************
 * Copyright (c) 2020 Konduit K.K.
 * Copyright (c) 2015-2019 Skymind, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ******************************************************************************/

package com.iceybones.capstone.dl4j;

import com.iceybones.capstone.CleanNews;
import com.iceybones.capstone.controllers.MainController;
import com.iceybones.capstone.models.WatchlistEntry;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.INDArrayIndex;
import org.nd4j.linalg.indexing.NDArrayIndex;

public class TestNews {

  private static WordVectors wordVectors;
  private static TokenizerFactory tokenizerFactory;
  private static final String dataLocalPath = "stocks/news/";
  public static Map<String, String> verdicts = new HashMap<>();
  public static MultiLayerNetwork net;

  static {
    try {
      net = MultiLayerNetwork.load(new File(dataLocalPath, "news_model.net"), true);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void analyze(String symbol, String rawNews) {
    String news = CleanNews.cleanLine(rawNews, symbol);
    DataSet testNews = prepareTestData(news);
    INDArray fet = testNews.getFeatures();
    INDArray predicted = net.output(fet, false);
    long[] arrsiz = predicted.shape();

    Path categories = Path.of(dataLocalPath, "labeled_news/categories.txt");

    double max = 0;
    int pos = 0;
    for (int i = 0; i < arrsiz[1]; i++) {
      if (max < (double) predicted.slice(0).getRow(i).sumNumber()) {
        max = (double) predicted.slice(0).getRow(i).sumNumber();
        pos = i;
      }
    }

    try (var cats = Files.newBufferedReader(categories)) {
      String temp;
      List<String> labels = new ArrayList<>();
      while ((temp = cats.readLine()) != null) {
        labels.add(temp);
      }
      String label = labels.get(pos).split(",")[1];
      verdicts.put(symbol, label + "," + symbol + "," + rawNews);
    } catch (Exception e) {
      System.out.println("File Exception : " + e.getMessage());
    }
  }

  public static List<WatchlistEntry> main(ListView<String> newsParent, MainController mainController) throws Exception {
    AtomicInteger counter = new AtomicInteger(1);
    Platform.runLater(() -> newsParent.getItems().add("--------- Evaluating today's headlines. ---------"));
    List<WatchlistEntry> watchlist = new ArrayList<>();
    var ratedPath = Path.of("rated.csv");
    if (!Files.exists(ratedPath)) {
      Files.createFile(ratedPath);
    }
    var bought = Files.newBufferedReader(ratedPath).lines()
        .collect(Collectors.toList());
    tokenizerFactory = new DefaultTokenizerFactory();
    tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());
    wordVectors = WordVectorSerializer.readWord2VecModel(new File(dataLocalPath, "newsVector.txt"));
    Map<String, String> newsMap = new HashMap<>();
    try {
      newsMap = Files.newBufferedReader(Path.of(dataLocalPath, "feed.csv")).lines()
          .collect(Collectors
              .toMap((a) -> a.split(",")[0], (b) -> b.substring(b.indexOf(",") + 1)));
    } catch (IOException e) {
      e.printStackTrace();
    }
   final int total = newsMap.size();
    for (var element : newsMap.keySet().stream().sorted().collect(Collectors.toList())) {
      counter.getAndIncrement();
      var split = newsMap.get(element).split(",");
      if (LocalDateTime.parse(split[0]).toLocalDate().isEqual(LocalDateTime.now().toLocalDate())
          || (LocalDateTime.parse(split[0]).toLocalTime().isAfter(LocalTime.of(9, 30))
          && LocalDateTime.parse(split[0]).toLocalDate()
          .isAfter(LocalDateTime.now().toLocalDate().minusDays(2)))) {
        if (bought.contains(element)) {
          verdicts.remove(element);
        } else {
          analyze(element, newsMap.get(element));
          Platform.runLater(() -> newsParent.getItems().add(
               counter + "/" + total + ": " + verdicts.get(element)));
        }
      }
    }
    try (var out = Files.newBufferedWriter(Path.of("rated.csv"), StandardOpenOption.TRUNCATE_EXISTING)) {
      for (var el : verdicts.keySet()) {
        var elSplit = verdicts.get(el).split(",");
        watchlist.add(new WatchlistEntry(elSplit[1], elSplit[0]));
        out.append(verdicts.get(el));
        out.newLine();
      }
      Platform.runLater(() -> newsParent.getItems().add("--------- Evaluation complete. ---------"));
      mainController.finishScanTest();
    }
    return watchlist;
  }

  // One news story gets transformed into a dataset with one element.
  @SuppressWarnings("DuplicatedCode")
  private static DataSet prepareTestData(String i_news) {
    List<String> news = new ArrayList<>(1);
    int[] category = new int[1];
    news.add(i_news);

    List<List<String>> allTokens = new ArrayList<>(news.size());
    int maxLength = 0;
    for (String s : news) {
      List<String> tokens = tokenizerFactory.create(s).getTokens();
      List<String> tokensFiltered = new ArrayList<>();
      for (String t : tokens) {
        if (wordVectors.hasWord(t)) {
          tokensFiltered.add(t);
        }
      }
      allTokens.add(tokensFiltered);
      maxLength = Math.max(maxLength, tokensFiltered.size());
    }

    INDArray features = Nd4j.create(news.size(), wordVectors.lookupTable().layerSize(), maxLength);
    INDArray labels = Nd4j.create(news.size(), 4, maxLength);
    INDArray featuresMask = Nd4j.zeros(news.size(), maxLength);
    INDArray labelsMask = Nd4j.zeros(news.size(), maxLength);

    int[] temp = new int[2];
    for (int i = 0; i < news.size(); i++) {
      List<String> tokens = allTokens.get(i);
      temp[0] = i;
      for (int j = 0; j < tokens.size() && j < maxLength; j++) {
        String token = tokens.get(j);
        INDArray vector = wordVectors.getWordVectorMatrix(token);
        features.put(
            new INDArrayIndex[]{NDArrayIndex.point(i), NDArrayIndex.all(), NDArrayIndex.point(j)},
            vector);
        temp[1] = j;
        featuresMask.putScalar(temp, 1.0);
      }
      int idx = category[i];
      int lastIdx = Math.min(tokens.size(), maxLength);
      labels.putScalar(new int[]{i, idx, lastIdx - 1}, 1.0);
      labelsMask.putScalar(new int[]{i, lastIdx - 1}, 1.0);
    }

    return new DataSet(features, labels, featuresMask, labelsMask);
  }
}
