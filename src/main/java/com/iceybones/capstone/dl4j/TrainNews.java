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

import com.iceybones.capstone.controllers.MainController;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.nn.api.Model;
import org.deeplearning4j.nn.conf.GradientNormalization;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.LSTM;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.api.InvocationType;
import org.deeplearning4j.optimize.listeners.PerformanceListener;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.parallelism.ParallelWrapper;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.RmsProp;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.nd4j.nativeblas.NativeOpsHolder;

public class TrainNews {

  public static String DATA_PATH = "data/news/labeled_news/";
  public static WordVectors wordVectors;

  public static void train(int batchSize, int epochs, int testSize, ListView<String> newsParent, MainController mainController) {
    Platform.runLater(() -> newsParent.getItems().addAll("========== Starting Training ==========",
        "This may take a while. Please wait."));
//    String dataLocalPath = "data/news/";
//    DATA_PATH = new File(dataLocalPath, "labeled_news").getAbsolutePath();

    int truncateReviewsToLength = 300;  //Truncate reviews with length (# words) greater than this

    for (int i = 0; i < 3; i++) {
      try (var trainOut = Files.newBufferedWriter(Path.of(DATA_PATH + "train/" + i + ".txt")
          , StandardOpenOption.CREATE);
          var testOut = Files.newBufferedWriter(Path.of(DATA_PATH + "test/" + i + ".txt")
              , StandardOpenOption.CREATE)) {
        var allIn = Files.newBufferedReader(Path.of(DATA_PATH + "train/" + i + "_all.txt"))
            .lines().collect(Collectors.toList());
        Collections.shuffle(allIn);
        for (int j = 0; j < testSize; j++) {
          if (j % 3 == 0) {
            testOut.write(allIn.get(j));
            testOut.newLine();
          } else {
            trainOut.write(allIn.get(j));
            trainOut.newLine();
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    //DataSetIterators for training and testing respectively
    //Using AsyncDataSetIterator to do data loading in a separate thread; this may improve performance vs. waiting for data to load
    wordVectors = WordVectorSerializer.readWord2VecModel(new File("data/news/newsVector.txt"));

    TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
    tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

    NewsIterator iTrain = new NewsIterator.Builder()
        .dataDirectory(DATA_PATH)
        .wordVectors(wordVectors)
        .batchSize(batchSize)
        .truncateLength(truncateReviewsToLength)
        .tokenizerFactory(tokenizerFactory)
        .train(true)
        .build();

    NewsIterator iTest = new NewsIterator.Builder()
        .dataDirectory(DATA_PATH)
        .wordVectors(wordVectors)
        .batchSize(batchSize)
        .tokenizerFactory(tokenizerFactory)
        .truncateLength(truncateReviewsToLength)
        .train(false)
        .build();
//    DataSetIterator train = new AsyncDataSetIterator(iTrain,1);
//    DataSetIterator test = new AsyncDataSetIterator(iTest,1);

    int inputNeurons = wordVectors
        .getWordVector(wordVectors.vocab().wordAtIndex(0)).length; // 100 in our case
    int outputs = iTrain.getLabels().size();

    tokenizerFactory = new DefaultTokenizerFactory();
    tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

    //Set up network configuration
    MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
        .updater(new RmsProp(0.0018))
        .l2(1e-5)
        .weightInit(WeightInit.XAVIER)
        .gradientNormalization(GradientNormalization.ClipElementWiseAbsoluteValue)
        .gradientNormalizationThreshold(1.0)
        .list()
        .layer(new LSTM.Builder().nIn(inputNeurons).nOut(200)
            .activation(Activation.TANH).build())
        .layer(new RnnOutputLayer.Builder().activation(Activation.SOFTMAX)
            .lossFunction(LossFunctions.LossFunction.MCXENT).nIn(200).nOut(outputs).build())
        .build();

    MultiLayerNetwork net = new MultiLayerNetwork(conf);
    net.init();
    var wrapper = new ParallelWrapper.Builder<>(net)
        .prefetchBuffer(16 * Runtime.getRuntime().availableProcessors())
        .workers(Runtime.getRuntime().availableProcessors())
        .averagingFrequency(8)
        .reportScoreAfterAveraging(true)
        .build();
    AtomicInteger counter = new AtomicInteger(wrapper.getWorkers());
    var score = new ScoreIterationListener() {
      private int printIterations = 1;

      @Override
      public void iterationDone(Model model, int iteration, int epoch) {
          if (printIterations <= 0) {
              printIterations = 1;
          }
        if (iteration % printIterations == 0) {
          double score = model.score();
          if (counter.incrementAndGet() % wrapper.getWorkers() == 0) {
            Platform.runLater(
                () -> newsParent.getItems().add("Score at iteration " + iteration + " is " + score));
            if(!mainController.getTrainBtn().isSelected()) {
              Platform.runLater(() -> newsParent.getItems().add("Shutting down..."));
            }
          }
        }
      }

      @Override
      public String toString() {
        return "ScoreIterationListener(" + printIterations + ")";
      }
    };
    wrapper.setListeners(new PerformanceListener(1), score, new EvaluativeListener(newsParent, iTest, 1, InvocationType.EPOCH_END));
    NativeOpsHolder.getInstance().getDeviceNativeOps().setOmpNumThreads(1);
    for (int i = 0; i < epochs; i++) {
      if(!mainController.getTrainBtn().isSelected()) {
        break;
      }
      int finalI = i;
      Platform.runLater(() -> newsParent.getItems().add("Training Epoch No. " + (finalI + 1)));
      wrapper.fit(iTrain);
      Platform.runLater(() -> newsParent.getItems().add("Evaluating Epoch No. " + (finalI + 1)));
      var eval = net.evaluate(iTest);
      Platform.runLater(() -> newsParent.getItems().addAll(eval.stats().split("\n")));
      iTrain.reset();
      Toolkit.getDefaultToolkit().beep();
    }

//    wrapper.fit(iTrain);


    try {
      net.save(new File( "data/news/news_model.net"), true);
      TestNews.loadNet();
    } catch (IOException e) {
      e.printStackTrace();
    }
    Platform.runLater(() -> newsParent.getItems().addAll("========== Training Complete ==========",
        "You now have a trained model on file and are free to backtest and use operational functions.",
        "============================================================="));
  }

}