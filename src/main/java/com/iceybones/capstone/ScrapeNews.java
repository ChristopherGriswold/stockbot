package com.iceybones.capstone;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ScrapeNews {

  public static Map<String, String> nameSymbols;
  static Path feedPath = Path.of("stocks/news/feed.csv");
  static Path symbolPath = Path.of("stocks/symbols.csv");
  static List<String> symbols;
  int counter = 0;
  static Map<String, String> feedBucket = new HashMap<>();
  static ExecutorService service = Executors
      .newFixedThreadPool(Runtime.getRuntime().availableProcessors());

  static {
    try {
      symbols = Files.newBufferedReader(symbolPath).lines()
          .map((a) -> a.substring(0, a.indexOf(",")))
          .collect(Collectors.toList());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  static {
    try {
      nameSymbols = Files.newBufferedReader(Path.of("stocks/symbols.csv")).lines()
          .collect(Collectors.toMap((a) -> a.split(",")[0], (b) -> b.split(",")[1]));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void cancelScan() {
    service.shutdownNow();
    counter = 0;
//    dumpFeed();
  }

  public ObservableList<String> scan() throws IOException, InterruptedException {
    if (service.isTerminated()) {
      service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }
    ObservableList<String> newsList = FXCollections.observableArrayList();
    System.out.println("Setting up.");
    counter = 0;
    if (!Files.exists(feedPath)) {
      Files.createFile(feedPath);
    }
    if (feedBucket.size() == 0) {
      Runtime.getRuntime().addShutdownHook(new Thread(ScrapeNews::dumpFeed));
      feedBucket = Files.newBufferedReader(feedPath).lines().collect(Collectors
          .toMap((a) -> a.split(",")[0], (b) -> b.substring(b.indexOf(",") + 1)));
//            Collections.shuffle(symbols);
    }
    System.out.println("Data Loaded. Starting Scan.");
    for (var s : symbols) {
//            Thread.sleep(200);
      service.submit(() -> {
        try {
          String news = getNews(s);
          if (news != null) {
            Platform.runLater(() -> newsList.add(news));
          }
          counter++;
        } catch (IOException e) {
          System.out.println("\u001B[31m" + e.getMessage() + "\033[0m");
        }
      });
    }
//        dumpFeed();
//        Toolkit.getDefaultToolkit().beep();
//        main(null);
    return newsList;
  }

  public String getNews(String symbol) throws IOException {
    String url = "https://www.benzinga.com/quote/";
    Document doc = Jsoup.connect(url + symbol)
        .timeout(10000).get();
    Elements body = doc.select("div.py-2.content-headline");
    String out = null;
    if (!body.isEmpty()) {
      String line = body.get(0).child(0).text();
      String date = body.get(0).child(1).text();
      date = date.substring(date.indexOf("-") + 2);
      LocalDateTime newsDate = null;
      if (date.contains("ago")) {
        var elements = date.split(" ");
        if (elements[elements.length - 2].contains("minute")) {
          newsDate = LocalDateTime.now()
              .minusMinutes(Integer.parseInt(elements[elements.length - 3]))
              .truncatedTo(ChronoUnit.HOURS);
        } else if (elements[elements.length - 2].contains("hour")) {
          newsDate = LocalDateTime.now().minusHours(Integer.parseInt(elements[elements.length - 3]))
              .truncatedTo(ChronoUnit.HOURS);
        } else {
          newsDate = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT)
              .minusDays(Integer.parseInt(elements[elements.length - 3]))
              .truncatedTo(ChronoUnit.HOURS);
        }
      } else {

        var dtf = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss");
        var altDtb = DateTimeFormatter.ofPattern("MMM d, yyyy, h:mma");
        try {
          newsDate = LocalDateTime.parse(date, dtf).truncatedTo(ChronoUnit.HOURS);
        } catch (Exception e) {
          try {
            newsDate = LocalDateTime.parse(date, altDtb).truncatedTo(ChronoUnit.HOURS);
          } catch (Exception d) {
            System.out.println(d.getMessage());
          }
        }
      }
      String cleanLine = cleanLine(symbol, line);
      if (feedBucket.containsKey(symbol)
          && feedBucket.get(symbol).equals(newsDate + ",\"" + line + "\"")) {
        System.out.println(counter + " - " + symbol + ": ");
      } else {
        feedBucket.put(symbol, newsDate + ",\"" + line + "\"");
        breakingNews(symbol);
        var dtf = DateTimeFormatter.ofPattern("MM/dd/yy h:mma");
        out = symbol + ": " + nameSymbols.get(symbol) + " - " + newsDate.format(dtf) + " - " + line;
      }
    } else {
      System.out.println(counter + " - " + symbol + ": ");
    }
    return out;
  }

  public static String cleanLine(String symbol, String line) {
//        String date = line.substring(line.lastIndexOf(",") - 25, line.lastIndexOf(","));
//        line = line.replace(symbol, "");
//        line = line.replace(nameSymbols.get(symbol), "");
//        line = line.toLowerCase().replaceAll("[0123456789.,'\"!@#$%^&*â„;()¢/:+=_®~`?’><—-]", "");
//        line = line.trim().replaceAll(" +", " ");
    return line;
  }


  public void breakingNews(String symbol) {
//        Toolkit.getDefaultToolkit().beep();
    System.out.println(
        counter + " - " + "\033[0;32m" + symbol + ": " + feedBucket.get(symbol) + "\033[0m");
  }

  public static void dumpFeed() {
    System.out.println("Dump Feed");
    try (var out = Files.newBufferedWriter(feedPath, StandardCharsets.UTF_8,
        StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
      for (var element : feedBucket.keySet()) {
        out.write(element + "," + feedBucket.get(element));
        out.newLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void getNames() throws IOException {
    ExecutorService service = Executors
        .newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    Scanner scanner = new Scanner(System.in);
    if (!Files.exists(Path.of("stocks/nasdaq_symbols.csv"))) {
      Files.createFile(Path.of("stocks/nasdaq_symbols.csv"));
    }
    try (var out = Files
        .newBufferedWriter(Path.of("stocks/nasdaq_symbols.csv"), StandardOpenOption.APPEND)) {
      var lines = Files.newBufferedReader(Path.of("stocks/nasdaq_raw.csv"))
          .lines().skip(1).collect(Collectors.toList());
      String url = "https://www.benzinga.com/quote/";
//            Collections.reverse(lines);
      for (var line : lines) {
        service.submit(() -> {
          try {
            Document doc = Jsoup.connect(url + line).timeout(10000).get();
            Elements body = doc.select("div.flex.justify-between");
            if (!body.isEmpty()) {
              var name = body.last().text().substring(0, body.last().text().length() - 28);
              System.out.println(name);
              if (!name.equals("undefined")) {
                out.write(line + ",\"" + name + "\"");
                out.newLine();
              }
            }
          } catch (Exception e) {
            System.out.println(e.getMessage());
          }
        });
      }
      while (true) {
        if (scanner.hasNext()) {
          System.out.println("SHUTTING DOWN SERVICE");
          service.shutdown();
          break;
        }
      }
    }
  }
}
