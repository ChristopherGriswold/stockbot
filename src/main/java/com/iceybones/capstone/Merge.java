package com.iceybones.capstone;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Merge {
    public static void main(String[] args) throws IOException {
//        shiftDates();
//        processNews();
        Map<String, String> stockMap = new HashMap<>();
        var stocksPath = Path.of("stocks/all_percent.csv");
        var newsPath = Path.of("stocks/news/news_shifted.csv");
        var outNews = Path.of("stocks/news/news_priced.csv");
        try (var stocks = Files.newBufferedReader(stocksPath);
             var news = Files.newBufferedReader(newsPath);
             var out = Files.newBufferedWriter(outNews, StandardOpenOption.CREATE)) {
            for (var line : stocks.lines().collect(Collectors.toList())) {
                var lineSplit = line.split(",");
                var symbol = lineSplit[1];
                var date = lineSplit[0];
                stockMap.putIfAbsent(date + symbol, line);
            }
            for (var line : news.lines().collect(Collectors.toList())) {
                var lineSplit = line.split(",");
                var stockLine = stockMap.get(lineSplit[0].substring(0, 10) + lineSplit[1]);
                if (stockLine != null) {
                    var stockSplit = stockLine.split(",");
                    out.write(lineSplit[0] + "," + lineSplit[1] + "," + stockSplit[stockSplit.length - 2] + ","
                            + stockSplit[stockSplit.length - 1] + "," + lineSplit[2]);
                    out.newLine();
                } else {
                    System.out.println(line);
                }
            }
        }
    }

    public static void shiftDates() throws IOException {
        try (var in = Files.newBufferedReader(Path.of("stocks/news/news_processed.csv"));
             var out = Files.newBufferedWriter(Path.of("stocks/news/news_shifted.csv"))) {
            List<String> lines = in.lines().collect(Collectors.toList());
            for (var line : lines) {
                var lineSplit = line.split(",");
                if (lineSplit.length < 3) continue;
                String dateString = lineSplit[0].substring(0, 19);
                String symbol = lineSplit[1];
                String news = lineSplit[2];
                var dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                var date = ZonedDateTime
                        .of(LocalDateTime.parse(dateString, dtf), ZoneId.of("America/New_York"));
                if (date.toLocalTime().isAfter(LocalTime.of(9, 30))) {
                    date = date.plusDays(1);
                }
                if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
                    date = date.plusDays(2);
                    System.out.println(date + " SATURDAY NEWS: " + symbol);
                }
                if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    date = date.plusDays(1);
                }
                var dtf2 = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ssXXX");
                out.write(date.format(dtf2) + "," + symbol + "," + news);
                out.newLine();
            }
        }
    }

    public static void processNews() throws IOException {
        try (var in = Files.newBufferedReader(Path.of("stocks/news/analyst_ratings_processed.csv"));
        var out = Files.newBufferedWriter(Path.of("stocks/news/news_processed.csv"))) {
            var lines = in.lines().skip(1).collect(Collectors.toList());
            for (var line : lines) {
                System.out.println(line);
                var splitLine = line.split(",");
                if (splitLine.length < 4) {
                    continue;
                }
                String symbol =  splitLine[splitLine.length - 1];
                String date =  splitLine[splitLine.length - 2];
                String headline = CleanNews.cleanLine(line.substring(line.indexOf(",")
                        + 1, line.lastIndexOf(",")), symbol);
                if (headline == null) {
                    continue;
                }
                out.write(date + "," + symbol + "," + headline);
                out.newLine();
            }
        }
    }

    public static void mergeIndividualStocks(Exchange exchange) throws IOException {
        List<String> symbols = Files.newBufferedReader(Path.of(exchange.path + "symbols.csv"))
                .lines().map((a) -> a.split(",")[0]).collect(Collectors.toList());
        var stockFolder = Path.of(exchange.path + "individual/");
        List<File> files = Files.list(stockFolder)
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .sorted()
                .collect(Collectors.toList());
        var outPath = Path.of(exchange.path + exchange.name().toLowerCase() + "_all.csv");
        try (var bufOut = Files.newBufferedWriter(outPath)) {
            for (var file : files) {
                try (var inFile = Files.newBufferedReader(file.toPath())) {
                    List<String> lines = inFile.lines().collect(Collectors.toList());
                    Collections.reverse(lines);
                    for (int i = 0; i < lines.size() - 1; i++) {
                        if (lines.get(i).split(",")[1].equals("null")
                                || !symbols.contains(file.getName().split("\\.")[0])) {
                            continue;
                        }
                        lines.set(i, lines.get(i).substring(0, 11) + file.getName().split("\\.")[0]
                                + "," + lines.get(i).substring(11));
                        bufOut.write(lines.get(i));
                        bufOut.newLine();
                    }
                }
            }
        }
    }
}
