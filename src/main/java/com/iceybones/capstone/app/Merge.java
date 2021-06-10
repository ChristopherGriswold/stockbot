package com.iceybones.capstone.app;

import java.io.IOException;

public class Merge {

  public static void main(String[] args) throws IOException {
//    mergeIndividualStocks();
//    Files.newBufferedReader(Path.of("src/main/java/com/iceybones/capstone/data/all_quotes.csv")).lines().forEach(System.out::println);
//        shiftDates();
//        processNews();
//    mergeRua();
//    Map<String, String> stockMap = new HashMap<>();
//    Path stocksPath = null;
//    try {
//      stocksPath = Path.of(Merge.class.getClassLoader().getResource("data/all_quotes.csv").toURI());
//    } catch (URISyntaxException e) {
//      e.printStackTrace();
//    }
//    var newsPath = Path.of(Merge.class.getClassLoader().getResource("data//news/news_shifted.csv");
//    var outNews = Path.of(Merge.class.getClassLoader().getResource("data//news/news_priced.csv");
//    try (var stocks = Files.newBufferedReader(stocksPath);
//        var news = Files.newBufferedReader(newsPath);
//        var out = Files.newBufferedWriter(outNews, StandardOpenOption.CREATE)) {
//      for (var line : stocks.lines().skip(1).collect(Collectors.toList())) {
//        System.out.println(line);
//        var lineSplit = line.split(",");
//        var symbol = lineSplit[1];
//        var date = lineSplit[0];
//        stockMap.putIfAbsent(date + symbol, line);
//      }
//      out.write("DateTime,Symbol,Open,High,Low,Close,Adj_Close,Volume,News");
//      out.newLine();
//      for (var line : news.lines().collect(Collectors.toList())) {
//        var lineSplit = line.split(",");
//        var stockLine = stockMap.get(lineSplit[0].substring(0, 10) + lineSplit[1]);
//        if (stockLine != null) {
//          var stockSplit = stockLine.split(",");
//          System.out.println(stockLine);
//          out.write(
//              lineSplit[0] + "," + lineSplit[1] + "," + stockSplit[2] + ","
//                  + stockSplit[3] + "," + stockSplit[4] + "," + stockSplit[5] + "," + stockSplit[6]
//                  + "," + stockSplit[7] + "," + lineSplit[2]);
//          out.newLine();
//        } else {
////          System.out.println(line);
//        }
//      }
//    }
//  }
//
//  public static void trimPercents() {
//    try {
//      var lines = Files.newBufferedReader(Path.of("src/main/java/com/iceybones/capstone/data/all_percent.csv")).lines().collect(
//          Collectors.toList());
//      try (var out = Files.newBufferedWriter(Path.of("src/main/java/com/iceybones/capstone/data/all_percent2.csv"))) {
//        for (var line : lines) {
//          var split = line.split(",");
//          var combo = "";
//          for (int i = 0; i < 8; i++) {
//            combo += (split[i] + ",");
//          }
//          out.write(combo.substring(0, combo.length() - 1));
//          out.newLine();
//        }
//      }
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }
//
//  public static void shiftDates() throws IOException {
//    try (var in = Files.newBufferedReader(Path.of("src/main/java/com/iceybones/capstone/data/news/news_processed.csv"));
//        var out = Files.newBufferedWriter(Path.of("src/main/java/com/iceybones/capstone/data/news/news_shifted.csv"))) {
//      List<String> lines = in.lines().collect(Collectors.toList());
//      for (var line : lines) {
//        var lineSplit = line.split(",");
//        if (lineSplit.length < 3) {
//          continue;
//        }
//        String dateString = lineSplit[0].substring(0, 19);
//        String symbol = lineSplit[1];
//        String news = lineSplit[2];
//        var dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        var date = ZonedDateTime
//            .of(LocalDateTime.parse(dateString, dtf), ZoneId.of("America/New_York"));
//        if (date.toLocalTime().isAfter(LocalTime.of(9, 30))) {
//          date = date.plusDays(1);
//        }
//        if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
//          date = date.plusDays(2);
//        }
//        if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
//          date = date.plusDays(1);
//        }
//        var dtf2 = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ssXXX");
//        out.write(date.format(dtf2) + "," + symbol + "," + news);
//        out.newLine();
//      }
//    }
//  }
//
//  public static void processNews() throws IOException {
//    try (var in = Files.newBufferedReader(Path.of("src/main/java/com/iceybones/capstone/data/news/analyst_ratings_processed.csv"));
//        var out = Files.newBufferedWriter(Path.of("src/main/java/com/iceybones/capstone/data/news/news_processed.csv"))) {
//      var lines = in.lines().skip(1).collect(Collectors.toList());
//      for (var line : lines) {
//        System.out.println(line);
//        var splitLine = line.split(",");
//        if (splitLine.length < 4) {
//          continue;
//        }
//        String symbol = splitLine[splitLine.length - 1];
//        String date = splitLine[splitLine.length - 2];
//        String headline = CleanNews.cleanLine(line.substring(line.indexOf(",")
//            + 1, line.lastIndexOf(",")), symbol, false);
//        if (headline == null) {
//          continue;
//        }
//        out.write(date + "," + symbol + "," + headline);
//        out.newLine();
//      }
//    }
//  }
//
//  private static void mergeRua() {
//    try (var stocks = Files.newBufferedReader(Path.of("src/main/java/com/iceybones/capstone/data/news/news_priced.csv"));
//        var stocksOut = Files.newBufferedWriter(Path.of("src/main/java/com/iceybones/capstone/data/news/news_rua.csv"))) {
//      Map<String, Double> newsMap = Files.newBufferedReader(Path.of("src/main/java/com/iceybones/capstone/data/^RUA.csv")).lines()
//          .filter((a) -> !a.contains("null"))
//          .skip(1).collect(Collectors.toMap(a -> a.split(",")[0],
//              a -> Double.parseDouble(a.split(",")[4]) / Double.parseDouble(a.split(",")[1])));
//      String line;
//      while ((line = stocks.readLine()) != null) {
//        var lineSplit = line.split(",");
//        var date = lineSplit[0].split(" ")[0];
//        if (newsMap.get(date) == null) {
//          continue;
//        }
//        stocksOut.write(
//            lineSplit[0] + "," + lineSplit[1] + "," + lineSplit[2] + "," + lineSplit[3]
//                + "," + lineSplit[4] + "," + lineSplit[5] + "," + newsMap.get(date) + ","
//                + lineSplit[6]);
//        stocksOut.newLine();
//      }
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }
//
//  public static void mergeIndividualStocks() throws IOException {
//    List<String> symbols = Files.newBufferedReader(Path.of("src/main/java/com/iceybones/capstone/data/all_symbols.csv"))
//        .lines().map((a) -> a.split(",")[0]).collect(Collectors.toList());
//    var stockFolder = Path.of("src/main/java/com/iceybones/capstone/data/all_individual/");
//    List<File> files = Files.list(stockFolder)
//        .filter(Files::isRegularFile)
//        .map(Path::toFile)
//        .sorted()
//        .collect(Collectors.toList());
//    var outPath = Path.of("src/main/java/com/iceybones/capstone/data/all_quotes.csv");
//    try (var bufOut = Files.newBufferedWriter(outPath)) {
//      bufOut.write("Date,Symbol,Open,High,Low,Close,Adj_Close,Volume");
//      bufOut.newLine();
//      int counter = 0;
//      for (var file : files) {
//        try (var inFile = Files.newBufferedReader(file.toPath())) {
//          List<String> lines = inFile.lines().collect(Collectors.toList());
//          Collections.reverse(lines);
//          for (int i = 0; i < lines.size() - 1; i++) {
//            if (lines.get(i).split(",")[1].equals("null")
//                || !symbols.contains(file.getName().split("\\.")[0])) {
//              continue;
//            }
//            System.out.println(counter++);
//            lines.set(i, lines.get(i).substring(0, 11) + file.getName().split("\\.")[0]
//                + "," + lines.get(i).substring(11));
//            bufOut.write(lines.get(i));
//            bufOut.newLine();
//          }
//        }
//      }
//    }
  }
}
