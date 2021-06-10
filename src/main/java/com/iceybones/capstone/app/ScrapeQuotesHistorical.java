package com.iceybones.capstone.app;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ScrapeQuotesHistorical {
    public static void main(String[] args) throws Exception {
        scrape();
    }
    public static void scrape() throws Exception {
        List<String> symbols = Files.newBufferedReader(Path.of("data/all_symbols.csv"))
                .lines().map((a) -> a.split(",")[0]).collect(Collectors.toList());
        var service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//        Collections.reverse(symbols);
        AtomicInteger counter = new AtomicInteger(0);
//        System.setProperty("http.proxyHost", "36.66.124.193");
//        System.setProperty("http.proxyPort", "3128");

// Now, let's 'unset' the proxy.
//        System.clearProperty("http.proxyHost");

        for (var symbol : symbols) {
            counter.incrementAndGet();
//            service.submit(() -> {
                if (Files.exists(Path.of("data/all_individual/" + symbol + ".csv"))) {
                    try {
//                        System.out.println("FILE ALREADY EXISTS");
                        if (Files.size(Path.of("data/all_individual/" + symbol + ".csv")) == 8) {
                            Files.delete(Path.of("data/all_individual/" + symbol + ".csv"));
                            System.out.println("DELETING FILE");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        var url = new URL("https://query1.finance.yahoo.com/v7/finance/download/" + symbol
                            + "?period1=1234569600&period2=1591833600&interval=1d&events=history&includeAdjustedClose=true");
                        System.out.println("Downloading: " + symbol + ", " + counter + "/" + symbols.size());
                        downloadFile(url,"data/all_individual/" + symbol + ".csv");
                        System.out.println("SUCCESS!!!!!!!!!!");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
//            });
        }
    }

    public static void downloadFile(URL url, String fileName) throws Exception {
        try (InputStream in = url.openStream()) {
            Files.copy(in, Paths.get(fileName));
        }
    }
}
