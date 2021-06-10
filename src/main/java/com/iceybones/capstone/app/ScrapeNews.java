package com.iceybones.capstone.app;

import com.iceybones.capstone.controllers.MainController;
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
import javafx.scene.control.ListView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ScrapeNews {

//  public static Map<String, String> nameSymbols = new HashMap<>();
  static Path feedPath = Path.of("data/news/feed.csv");
  static Path symbolPath = Path.of("data/symbols.csv");
//  static List<String> symbols;
  int counter = 0;
  static Map<String, String> feedBucket = new HashMap<>();
  public static ExecutorService service = Executors
      .newFixedThreadPool(Runtime.getRuntime().availableProcessors());

//  static {
//    try {
//      symbols = Files.newBufferedReader(symbolPath).lines()
//          .map((a) -> a.substring(0, a.indexOf(",")))
//          .collect(Collectors.toList());
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }

//  static {
//    try {
//      nameSymbols = Files.newBufferedReader(Path.of("src/main/resources/data/symbols.csv")).lines()
//          .collect(Collectors.toMap((a) -> a.split(",")[0], (b) -> b.split(",")[1]));
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }

  public static void cancelScan() {
    service.shutdownNow();
    dumpFeed();
  }

  public ObservableList<String> scan(ListView<String> newsParent, List<String> symbols, MainController mainController) throws IOException, InterruptedException {
    int total = MainController.usEquities.size();

    if (service.isTerminated()) {
      service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }
    ObservableList<String> newsList = FXCollections.observableArrayList();
    newsParent.getItems().addAll("========== Gathering Today's Headlines ==========",
        "This may take a while. Press the scan button at any time to stop the current scan.",
        "The results will be saved but subsequent scans will restart at the beginning of the list.");
    if (!Files.exists(feedPath)) {
      Files.createFile(feedPath);
    }
    if (feedBucket.size() == 0) {
      Runtime.getRuntime().addShutdownHook(new Thread(ScrapeNews::dumpFeed));
      feedBucket = Files.newBufferedReader(feedPath).lines().collect(Collectors
          .toMap((a) -> a.split(",")[0], (b) -> b.substring(b.indexOf(",") + 1)));
//            Collections.shuffle(symbols);
    }
    for (int i = 0; i < symbols.size(); i++) {
      int finalI = i;
      service.submit(() -> {
        try {
          String news = getNews(symbols.get(finalI), newsParent);
          if (news != null) {
            Platform.runLater(() -> newsParent.getItems().add(finalI + "/" + total + ": " + news));
          }
        } catch (IOException e) {
          System.out.println("\u001B[31m" + e.getMessage() + "\033[0m");
        }
      });
    }
    service.shutdown();
    Executors.newSingleThreadExecutor().submit(() -> {
      while(!service.isTerminated()) { }
      dumpFeed();
      Platform.runLater(mainController::finishScanTest);
    });
    return newsList;
  }

  public String getNews(String symbol, ListView<String> newsParent) throws IOException {
    String url = "https://www.benzinga.com/quote/";
    Document doc = Jsoup.connect(url + symbol).timeout(10000).get();
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
      if (feedBucket.containsKey(symbol)
          && feedBucket.get(symbol).contains(",\"" + line + "\"")) {
      } else {
        feedBucket.put(symbol, newsDate + ",\"" + line + "\"");
        var dtf = DateTimeFormatter.ofPattern("MM/dd/yy h:mma");
        body = doc.select("div.flex.justify-between");
        String name = "undefined";
        if (!body.isEmpty()) {
          name = body.last().text().substring(0, body.last().text().length() - 28);
        }
        out = symbol + ": " + name + " - " + newsDate.format(dtf) + " - " + line;
      }
    }
    return out;
  }

  public static void dumpFeed() {
    service.shutdownNow();
    Executors.newSingleThreadExecutor().submit(() -> {
      while (!service.isTerminated()) {}
        try (var out = Files.newBufferedWriter(feedPath, StandardCharsets.UTF_8,
            StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
          for (var element : feedBucket.keySet()) {
            out.write(element + "," + feedBucket.get(element));
            out.newLine();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
    });
  }

  public static void getNames() throws IOException {
    ExecutorService service = Executors
        .newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    Scanner scanner = new Scanner(System.in);
    if (!Files.exists(Path.of("data/nasdaq_symbols.csv"))) {
      Files.createFile(Path.of("data/nasdaq_symbols.csv"));
    }
    try (var out = Files
        .newBufferedWriter(Path.of("data/nasdaq_symbols.csv"), StandardOpenOption.APPEND)) {
      var lines = Files.newBufferedReader(Path.of("data/nasdaq_raw.csv"))
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
          service.shutdown();
          break;
        }
      }
    }
  }
}
