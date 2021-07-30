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

<<<<<<< HEAD
import com.iceybones.capstone.app.CleanNews;
import com.iceybones.capstone.controllers.MainController;
import com.iceybones.capstone.controllers.MainController.BuyType;
import com.iceybones.capstone.models.AlpacaWrapper;
=======
import com.iceybones.capstone.CleanNews;
import com.iceybones.capstone.controllers.MainController;
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
import com.iceybones.capstone.models.WatchlistEntry;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
<<<<<<< HEAD
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import net.jacobpeterson.alpaca.enums.marketdata.BarsTimeFrame;
import net.jacobpeterson.alpaca.rest.exception.AlpacaAPIRequestException;
import net.jacobpeterson.domain.alpaca.marketdata.historical.bar.Bar;
import net.jacobpeterson.domain.alpaca.marketdata.historical.bar.BarsResponse;
=======
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
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
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
<<<<<<< HEAD
  private static String dataLocalPath;

  static {
    dataLocalPath = "data/news";
  }

  public static ConcurrentHashMap<String, String> verdicts = new ConcurrentHashMap<>();
  public static MultiLayerNetwork net;
  public static Map<LocalDate, Double> marketPerformance;
  public static DecimalFormat df = new DecimalFormat("###,###,##0.00");

  static {
    try {
      marketPerformance = Files
          .newBufferedReader(Path.of("data/market.csv")).lines().skip(1)
          .collect(
              Collectors.toMap((a) -> LocalDate.of(Integer.parseInt(a.substring(0, 4)),
                  Integer.parseInt(a.substring(5, 7)), Integer.parseInt(a.substring(8, 10))),
                  (a) -> Double.parseDouble(a.split(",")[1])));
      tokenizerFactory = new DefaultTokenizerFactory();
      tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());
      loadNet();
      wordVectors = WordVectorSerializer
          .readWord2VecModel("data/news/newsVector.txt");
=======
  private static final String dataLocalPath = "stocks/news/";
  public static Map<String, String> verdicts = new HashMap<>();
  public static MultiLayerNetwork net;

  static {
    try {
      net = MultiLayerNetwork.load(new File(dataLocalPath, "news_model.net"), true);
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

<<<<<<< HEAD
  public static void loadNet() {
    if (Files.exists(Path.of(dataLocalPath, "news_model.net"))) {
      try {
        net = MultiLayerNetwork.load(new File(dataLocalPath, "news_model.net"), true);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private static String analyze(String symbol, String rawNews, boolean test) {
    String news = CleanNews.cleanLine(rawNews, symbol, false);
=======
  private static void analyze(String symbol, String rawNews) {
    String news = CleanNews.cleanLine(rawNews, symbol);
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
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
<<<<<<< HEAD
    String label = "sell";
=======

>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
    try (var cats = Files.newBufferedReader(categories)) {
      String temp;
      List<String> labels = new ArrayList<>();
      while ((temp = cats.readLine()) != null) {
        labels.add(temp);
      }
<<<<<<< HEAD
      label = labels.get(pos).split(",")[1];
      verdicts.put(symbol, label + "," + symbol + "," + rawNews);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return label;
  }

  public static List<WatchlistEntry> main(ListView<String> newsParent,
      MainController mainController) throws Exception {
    AtomicInteger counter = new AtomicInteger(1);
    Platform.runLater(
        () -> newsParent.getItems()
            .addAll("========== Starting Evaluation ==========",
                "Evaluating Headlines: |..................................................|"));
    List<WatchlistEntry> watchlist = new ArrayList<>();
    var ratedPath = Path.of(dataLocalPath, "rated.csv");
    if (!Files.exists(ratedPath)) {
      Files.createFile(ratedPath);
    }
    var rated = Files.newBufferedReader(ratedPath).lines()
        .collect(Collectors.toList());
=======
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
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
    Map<String, String> newsMap = new HashMap<>();
    try {
      newsMap = Files.newBufferedReader(Path.of(dataLocalPath, "feed.csv")).lines()
          .collect(Collectors
              .toMap((a) -> a.split(",")[0], (b) -> b.substring(b.indexOf(",") + 1)));
    } catch (IOException e) {
      e.printStackTrace();
    }
<<<<<<< HEAD
    final int total = newsMap.size();
    for (var element : newsMap.keySet().stream().sorted().collect(Collectors.toList())) {
      if (counter.intValue() % (newsMap.size() / 50) == 1) {
        Platform.runLater(
            () -> {
              String current = newsParent.getItems().get(newsParent.getItems().size() - 1)
                  .replaceFirst("[.]", "|");
              newsParent.getItems().set(newsParent.getItems().size() - 1, current);
//                  newsParent.getItems().set(newsParent.getItems().size() - 1,
//                      "Evaluating " + symbol + ": " + finalCounter + "/" + dailyStocks.get(date).size());
            });
      }
=======
   final int total = newsMap.size();
    for (var element : newsMap.keySet().stream().sorted().collect(Collectors.toList())) {
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
      counter.getAndIncrement();
      var split = newsMap.get(element).split(",");
      if (LocalDateTime.parse(split[0]).toLocalDate().isEqual(LocalDateTime.now().toLocalDate())
          || (LocalDateTime.parse(split[0]).toLocalTime().isAfter(LocalTime.of(9, 30))
          && LocalDateTime.parse(split[0]).toLocalDate()
          .isAfter(LocalDateTime.now().toLocalDate().minusDays(2)))) {
<<<<<<< HEAD
        if (rated.contains(element)) {
          verdicts.remove(element);
        } else {
          analyze(element, newsMap.get(element), false);
//          Platform.runLater(() -> newsParent.getItems().add(
//              counter + "/" + total + ": " + verdicts.get(element)));
        }
      }
    }
    try (var out = Files
        .newBufferedWriter(Path.of(dataLocalPath, "rated.csv"))) {
=======
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
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
      for (var el : verdicts.keySet()) {
        var elSplit = verdicts.get(el).split(",");
        watchlist.add(new WatchlistEntry(elSplit[1], elSplit[0]));
        out.append(verdicts.get(el));
        out.newLine();
      }
<<<<<<< HEAD
      Platform
          .runLater(() -> newsParent.getItems()
              .addAll("========== Evaluation Complete ==========",
                  "============================================================="));
=======
      Platform.runLater(() -> newsParent.getItems().add("--------- Evaluation complete. ---------"));
      mainController.finishScanTest();
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
    }
    return watchlist;
  }

<<<<<<< HEAD
  // DateTime	Symbol	High/Open	Low/Open	Rua_High/Open	News
  public static void backTest(ListView<String> newsParent, MainController mainController,
      LocalDate start, LocalDate end, double initInvestment, BuyType buyType) {
    double[] investment = {initInvestment};
    Platform.runLater(
        () -> newsParent.getItems()
            .addAll("========== Starting Quick BackTest =========="));
    long daysBetween = ChronoUnit.DAYS.between(start, end) + 1;
    long businessDays = Stream.iterate(start, date -> date.plusDays(1))
        .limit(daysBetween).filter((a) -> a.getDayOfWeek() != DayOfWeek.SATURDAY
            && a.getDayOfWeek() != DayOfWeek.SUNDAY).count();
    AtomicLong days = new AtomicLong(1);
    verdicts = new ConcurrentHashMap<>();
    Map<String, String> resultMap = new HashMap<>();
    try {
      var newsLines = Files.newBufferedReader(Path.of("data/news/news_priced.csv"))
          .lines().skip(1).collect(Collectors.toList());
      for (var line : newsLines) {
        var date = LocalDate
            .of(Integer.parseInt(line.substring(0, 4)), Integer.parseInt(line.substring(5, 7)),
                Integer.parseInt(line.substring(8, 10)));
        if (date.isBefore(start) || date.isAfter(end)) {
          continue;
        }
        var symbol = line.split(",")[1];
        resultMap.putIfAbsent(date + "," + symbol, line);
      }

      long total = resultMap.size();
      long counter = total / businessDays;
      var analyzeService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
      for (var key : resultMap.keySet()) {
        long finalTotal = total;
        var lineSplit = resultMap.get(key).split(",");
        analyzeService.submit(() -> {
          if (finalTotal % counter == 0) {
            Platform.runLater(() -> newsParent.getItems()
                .add("Processing day " + days.getAndIncrement() + " of " + businessDays));
          }

          analyze(key, lineSplit[lineSplit.length - 1], true);
          if (!mainController.getBackTestBtm().isSelected()) {
            analyzeService.shutdownNow();
          }
        });
        total--;
      }
      analyzeService.shutdown();
      while (!analyzeService.isTerminated()) {

      }
      mainController.finishBackTest();
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }
//    double average = 1;
    double totalCount = 0;
    double totalSum = 0;
    double totalMarketCount = 0;
    double totalMarketSum = 0;
    double marketAvg = 1;
    for (var key : verdicts.keySet()) {
      String verdict = verdicts.get(key).split(",")[0];
      var lineSplit = key.split(",");
      String dateString = lineSplit[0];
      var date = LocalDate
          .of(Integer.parseInt(dateString.substring(0, 4)),
              Integer.parseInt(dateString.substring(5, 7)),
              Integer.parseInt(dateString.substring(8, 10)));
      var symbol = lineSplit[1];
      var category = verdicts.get(key).split(",")[0];
      var stats = resultMap.get(key).split(",");
      var dayChange = Double.parseDouble(stats[2]);
      var market = marketPerformance.get(date);
      totalMarketSum += market;
      totalMarketCount++;
      if (verdict.equals("sell")
          || (buyType == BuyType.STRONG_ONLY && !verdict.equals("buyStrong"))) {
        continue;
      }
      totalSum += dayChange;
      totalCount++;
    }
    days.decrementAndGet();
    var newEnd = Stream.iterate(start, date -> date.plusDays(1))
        .filter((a) -> a.getDayOfWeek() != DayOfWeek.SATURDAY
            && a.getDayOfWeek() != DayOfWeek.SUNDAY).limit(days.intValue())
        .reduce((first, second) -> second).orElse(start);
    double finalTotalCount = totalCount;
    double finalTotalSum = totalSum;
    double finalTotalMarketSum = totalMarketSum;
    double finalTotalMarketCount = totalMarketCount;
    Platform
        .runLater(() -> newsParent.getItems().addAll(
            "========== BackTest Results ==========",
            "With an initial investment of $" + df.format(investment[0]) + ", from " + start
                + " to "
                + newEnd + ":",
            "Total Profit/Loss: $" + df
                .format(
                    investment[0] * (Math.pow(finalTotalSum / finalTotalCount, days.doubleValue()))
                        - investment[0]),
            "Average Daily P&L: " + df.format((finalTotalSum / finalTotalCount - 1) * 100) + "%",
            "Average Daily Market Performance: " + df
                .format((finalTotalMarketSum / finalTotalMarketCount - 1) * 100) + "%",
            "============================================================="));
  }


  public static Map<String, Integer> calculateBuys(ListView<String> newsParent,
      List<WatchlistEntry> watchList, double[] investment, BuyType buyType) {
    double initInvestment = investment[0];
    Map<String, Integer> out = new HashMap<>();
    watchList = watchList.stream().sorted().filter((a) -> !a.getRatingRaw().equals("sell"))
        .toList();
    if (buyType == BuyType.STRONG_ONLY) {
      watchList = watchList.stream().filter((a) -> !a.getRatingRaw().equals("buy")).toList();
    }
    while (investment[0] > watchList.get(0).getValue()) {
      for (int i = watchList.size() - 1; i >= 0; i--) {
        double stockPrice = watchList.get(i).getValue();
        if (investment[0] / (i + 1) >= stockPrice) {
          int qty = (int) Math.round((investment[0] / (i + 1)) / stockPrice);
          while (investment[0] < qty * stockPrice) {
            qty--;
          }
          if (qty <= 0) {
            continue;
          }
          int finalQty = qty;
          int finalQty1 = qty;
          int finalI = i;
          List<WatchlistEntry> finalWatchList = watchList;
          Platform.runLater(() -> newsParent.getItems()
              .add("Buy " + finalQty + " share(s) of " + finalWatchList.get(finalI).getSymbol()
                  + " @ $" + df.format(stockPrice)
                  + " ea. - Total: $" + df.format(finalQty1 * stockPrice)));
          out.put(watchList.get(i).getSymbol(), qty);
          investment[0] -= qty * stockPrice;
        }
      }
    }
    Platform.runLater(() -> newsParent.getItems().addAll(
        "=============================================================",
        "Total Estimated Price: $" + df.format(initInvestment - investment[0]),
        "WARNING: Actual price may vary according to the BUY LIMIT % entered.",
        "Press CONFIRM to submit order.",
        "============================================================="));
    return out;
  }

  public static Map<String, Integer> calculateBuys(Map<String, List<Bar>> barMap,
      double[] investment) {
    Map<String, Integer> out = new HashMap<>();
    List<String> stocks = barMap.keySet().stream()
        .sorted((a, b)
        -> (int) ((barMap.get(a).get(0).getO() * 1000) - (barMap.get(b).get(0).getO() * 1000)))
        .collect(Collectors.toList());
    while (investment[0] > barMap.get(stocks.get(0)).get(0).getO()) {
      for (int i = stocks.size() - 1; i >= 0; i--) {
        double stockPrice = barMap.get(stocks.get(i)).get(0).getO();
        if (investment[0] / (i + 1) >= stockPrice) {
          int qty = (int) Math.round((investment[0] / (i + 1)) / stockPrice);
          while (investment[0] < qty * stockPrice) {
            qty--;
          }
          if (qty <= 0) {
            continue;
          }
          out.put(stocks.get(i), qty);
          investment[0] -= qty * stockPrice;
        }
      }
    }
    return out;
  }

  public static void backTest(ListView<String> newsParent, MainController mainController,
      LocalDate start, LocalDate end, double initInvestment, double trailPercent, BuyType buyType) {
    double[] investment = {initInvestment};
    var zonedStart = ZonedDateTime.of(start, LocalTime.of(14, 30), ZoneId.systemDefault());
    var zonedEnd = ZonedDateTime.of(end, LocalTime.of(21, 0), ZoneId.systemDefault());
    BarsTimeFrame testSize = BarsTimeFrame.MINUTE;
    Platform
        .runLater(() -> newsParent.getItems().addAll("========== Starting BackTest =========="));
    long daysBetween = ChronoUnit.DAYS.between(start, end) + 1;
    var businessDays = Stream.iterate(start, date -> date.plusDays(1))
        .limit(daysBetween).filter((a) -> a.getDayOfWeek() != DayOfWeek.SATURDAY
            && a.getDayOfWeek() != DayOfWeek.SUNDAY).collect(Collectors.toList());
    if (businessDays.size() > 25) {
      testSize = BarsTimeFrame.HOUR;
    }
    Map<LocalDate, Map<String, String>> dailyStocks = new HashMap<>();
    Map<String, BarsResponse> bars = new HashMap<>();
    Set<String> buySet = new HashSet<>();
    try {
      var newsLines = Files.newBufferedReader(Path.of(dataLocalPath, "news_priced.csv"))
          .lines().skip(1).collect(Collectors.toList());
      for (var line : newsLines) {
        var date = LocalDate
            .of(Integer.parseInt(line.substring(0, 4)), Integer.parseInt(line.substring(5, 7)),
                Integer.parseInt(line.substring(8, 10)));
        if (!businessDays.contains(date)) {
          continue;
        } else {
          var symbol = line.split(",")[1];
          dailyStocks.putIfAbsent(date, new HashMap<>());
          dailyStocks.get(date).put(symbol, line);
        }
      }
      var analyzeService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
      var dailyStocksKeySet = dailyStocks.keySet().stream().sorted().collect(Collectors.toList());
      for (var date : dailyStocksKeySet) {
        Platform.runLater(
            () -> newsParent.getItems()
                .addAll("========== Processing " + date + " ========== ",
                    "Equity: $" + df.format(investment[0]),
                    "Gathering Historical Data: |..................................................|"));
        int counter = 0;
        for (var symbol : dailyStocks.get(date).keySet()) {
          counter++;
          int finalCounter = counter;
          if (counter % (dailyStocks.get(date).size() / 50) == 1) {
            Platform.runLater(
                () -> {
                  String current = newsParent.getItems().get(newsParent.getItems().size() - 1)
                      .replaceFirst("[.]", "|");
                  newsParent.getItems().set(newsParent.getItems().size() - 1, current);
//                  newsParent.getItems().set(newsParent.getItems().size() - 1,
//                      "Evaluating " + symbol + ": " + finalCounter + "/" + dailyStocks.get(date).size());
                });
          }
//          Platform.runLater(
//              () -> newsParent.getItems().set(newsParent.getItems().size() - 1,
//                  "Evaluating " + symbol + ": " + finalCounter + "/" + dailyStocks.get(date).size()));
          var lineSplit = dailyStocks.get(date).get(symbol).split(",");
          String eval = analyze(symbol, lineSplit[lineSplit.length - 1], true);
          if (eval.equals("sell") || (buyType == BuyType.STRONG_ONLY && eval.equals("buy"))) {
            continue;
          }
          bars.putIfAbsent(symbol, AlpacaWrapper
              .getAPI().getBars(symbol, zonedStart, zonedEnd, 10_000, null, testSize));
          buySet.add(symbol);
//            else if (buyType == BuyType.PREFER_STRONG) {
//              var comparator = Comparator.comparing(WatchlistEntry::getRating)
//                  .thenComparing(WatchlistEntry::getValue);
//              watchList = watchList.stream().sorted(comparator).toList();
//            }
        }
        Platform.runLater(
            () -> newsParent.getItems()
                .addAll("========== Evaluating Data ========== "));
        Map<String, List<Bar>> todayBarMap = new HashMap<>();
        STOCK:
        for (var stock : dailyStocks.get(date).keySet()) {
          if (!buySet.contains(stock)) {
            continue;
          }
//          var b = ;
//          int size = b.size();
          List<Bar> todayBars = new ArrayList<>(bars.get(stock).getBars());
//          BAR:
//          for (int i = 0; i < size; i++) {
//              todayBars.add(b.get(i));
//          }
          if (todayBars.size() > 0) {
            todayBars.sort(Comparator.comparingInt(a -> a.getT().getNano()));
            todayBarMap.put(stock, todayBars);
          }
        }
        var buyMap = calculateBuys(todayBarMap, investment);
        for (var buy : buyMap.keySet()) {
          double buyPrice = todayBarMap.get(buy).get(0).getO();
          double highWater = buyPrice;
          double curPrice = 0.0;
          int qty = buyMap.get(buy);
          boolean sold = false;
          for (var bar : todayBarMap.get(buy)) {
            curPrice = bar.getO();
            if (curPrice > highWater) {
              highWater = curPrice;
            }
            if (100 - ((curPrice / highWater) * 100) > trailPercent) {
              sold = true;
              break;
            }
          }
          investment[0] += curPrice * qty;
          boolean finalSold = sold;
          double finalCurPrice = curPrice;

          Platform.runLater(
              () -> newsParent.getItems()
                  .addAll(buy + ": Bought " + qty + " shares @ $" + df.format(buyPrice) + " ea."
                      + (finalSold ? " - Sold @ $" : " - Holding @ $") + df.format(finalCurPrice)
                      + ", P&L: $" + df
                      .format((finalCurPrice * qty) - (buyPrice * qty))));
        }
        var datesBetween = start.datesUntil(date.plusDays(1)).filter((a) ->
            a.getDayOfWeek() != DayOfWeek.SATURDAY && a.getDayOfWeek() != DayOfWeek.SUNDAY).toList();
        var marketPercent = (datesBetween.stream().mapToDouble((a) ->
            (marketPerformance.getOrDefault(a, 1.0)))
            .average()
            .orElse(Double.NaN) - 1) * 100;
        Platform
            .runLater(() -> newsParent.getItems().addAll(
                "========== BackTest Results ==========",
                "With an initial investment of $" + df.format(initInvestment) + ", from " + start
                    + " through "
                    + date + ":",
                "Total Profit/Loss: $" + df.format(investment[0] - initInvestment) + " : "
                    + df.format(((investment[0] / initInvestment) - 1) * 100) + " %",
                "Market Performance: " + df.format(marketPercent) + " %",
//              "Average Daily P&L: " + df.format((finalTotalSum / finalTotalCount - 1) * 100) + "%",
//              "Average Daily Market Performance: " + df
//                  .format((finalTotalMarketSum / finalTotalMarketCount - 1) * 100) + "%",
                "============================================================="));
      }
//      DATE:
//      for (var date : dailyStocksKeySet) {
//        System.out.println("Starting day with $" + investment[0]);
//        Map<String, List<Bar>> todayBarMap = new HashMap<>();
//        STOCK:
//        for (var stock : dailyStocks.get(date).keySet()) {
//          if (!buySet.contains(stock)) {
//            continue;
//          }
//          var b = bars.get(stock).getBars();
//          int size = b.size();
//          List<Bar> todayBars = new ArrayList<>();
//          BAR:
//          for (int i = 0; i < size; i++) {
//            if (b.get(i).getT().toLocalDate().isEqual(date)) {
//              todayBars.add(b.get(i));
//            }
//          }
//          todayBarMap.put(stock, todayBars);
//        }
//        var buyMap = calculateBuys(todayBarMap, investment);
//        for (var buy : buyMap.keySet()) {
//          double buyPrice = todayBarMap.get(buy).get(0).getO();
//          double highWater = buyPrice;
//          double curPrice = 0.0;
//          int qty = buyMap.get(buy);
//          boolean sold = false;
//          for (var bar : todayBarMap.get(buy)) {
//            curPrice = bar.getO();
//            if (curPrice > highWater) {
//              highWater = curPrice;
//            }
//            if (100 - ((curPrice / highWater) * 100) > trailPercent) {
//              sold = true;
//              break;
//            }
//          }
//          investment[0] += curPrice * qty;
//          boolean finalSold = sold;
//          double finalCurPrice = curPrice;
//          Platform.runLater(
//              () -> newsParent.getItems()
//                  .addAll(buy + ": Bought " + qty + " shares @ $" + df.format(buyPrice) + " ea."
//                      + (finalSold ? " - Sold @ $" : " - Holding @ $") + df.format(finalCurPrice)
//                      + ", P&L: $" + df
//                      .format((finalCurPrice * qty) - (buyPrice * qty))));
//        }
//      }
//      Platform
//          .runLater(() -> newsParent.getItems().addAll(
//              "========== BackTest Results ==========",
//              "With an initial investment of $" + df.format(initInvestment) + ", from " + start
//                  + " to "
//                  + end + ":",
//              "Total Profit/Loss: $" + df.format(investment[0] - initInvestment),
////              "Average Daily P&L: " + df.format((finalTotalSum / finalTotalCount - 1) * 100) + "%",
////              "Average Daily Market Performance: " + df
////                  .format((finalTotalMarketSum / finalTotalMarketCount - 1) * 100) + "%",
//              "============================================================="));
    } catch (IOException | AlpacaAPIRequestException e) {
      e.printStackTrace();
    }
  }

=======
  // One news story gets transformed into a dataset with one element.
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
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
<<<<<<< HEAD
    INDArray labels = Nd4j.create(news.size(), 3,
        maxLength);    //labels: Crime, Politics, Bollywood, Business&Development
=======
    INDArray labels = Nd4j.create(news.size(), 4, maxLength);
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
    INDArray featuresMask = Nd4j.zeros(news.size(), maxLength);
    INDArray labelsMask = Nd4j.zeros(news.size(), maxLength);

    int[] temp = new int[2];
    for (int i = 0; i < news.size(); i++) {
      List<String> tokens = allTokens.get(i);
      temp[0] = i;
      for (int j = 0; j < tokens.size() && j < maxLength; j++) {
        String token = tokens.get(j);
        INDArray vector = wordVectors.getWordVectorMatrix(token);
<<<<<<< HEAD
        features.put(new INDArrayIndex[]{NDArrayIndex.point(i),
                NDArrayIndex.all(),
                NDArrayIndex.point(j)},
            vector);

=======
        features.put(
            new INDArrayIndex[]{NDArrayIndex.point(i), NDArrayIndex.all(), NDArrayIndex.point(j)},
            vector);
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
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
