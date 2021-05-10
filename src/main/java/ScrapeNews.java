import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ScrapeNews {
    static Path feedPath = Path.of("news/feed.csv");
    static Path symbolPath = Path.of("stocks/symbols/symbols.csv");
    static List<String> symbols;
    static int counter = 1607;

    static {
        try {
            symbols = Files.newBufferedReader(symbolPath).lines().map((a) -> a.substring(0, a.indexOf(",")))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static Map<String, String> feedBucket = new HashMap<>();
    static ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static void main(String[] args) throws IOException, InterruptedException {
        counter = 1607;
        if (feedBucket.size() == 0) {
            Runtime.getRuntime().addShutdownHook(new Thread(ScrapeNews::dumpFeed));
            feedBucket = Files.newBufferedReader(feedPath).lines()
                    .collect(Collectors.toMap((a) -> a.split(",")[0], (b) -> b.substring(b.indexOf("\""))));
            Collections.shuffle(symbols);
        }
        for (var s : symbols) {
            Thread.sleep(500);
            service.submit(() -> {
                try {
                    getNews(s);
                } catch (IOException e) {
                    System.out.println("\u001B[31m" + e.getMessage() + "\033[0m");
                }
            });
        }
        main(null);
    }

    public static void getNews(String symbol) throws IOException {
        String url = "https://www.benzinga.com/quote/";
        Document doc = Jsoup.connect(url + symbol)
                .timeout(10000).get();
        Elements body = doc.select("div.py-2.content-headline");
        if (!body.isEmpty()) {
            if (feedBucket.containsKey(symbol)
                    && feedBucket.get(symbol).equals("\"" + body.get(0).child(0).text() + "\"")) {
                System.out.println(--counter + " - " + symbol + ": ");
            } else {
                feedBucket.put(symbol, "\"" + body.get(0).child(0).text() + "\"");
                breakingNews(symbol);
            }
        } else {
            System.out.println(--counter + " - " + symbol + ": ");
        }
        ;
    }


    public static void breakingNews(String symbol) {
        Toolkit.getDefaultToolkit().beep();
        System.out.println(--counter + " - " +  "\033[0;32m" + symbol + ": " + feedBucket.get(symbol) + "\033[0m");
    }

    public static void dumpFeed() {
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
}
