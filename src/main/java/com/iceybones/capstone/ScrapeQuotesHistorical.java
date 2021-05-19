package com.iceybones.capstone;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ScrapeQuotesHistorical {
    public static void main(String[] args) throws Exception {
        scrape(Exchange.NYSE);
    }
    public static void scrape(Exchange exchange) throws Exception {
        List<String> symbols = Files.newBufferedReader(Path.of(exchange.path + "symbols.csv"))
                .lines().map((a) -> a.split(",")[0]).collect(Collectors.toList());
        Collections.reverse(symbols);
        for (var symbol : symbols) {
            if (Files.exists(Path.of(exchange.path + "individual/" + symbol + ".csv"))) {
                System.out.println("FILE ALREADY EXISTS");
            } else {
                try {
                    downloadFile(new URL("https://query1.finance.yahoo.com/v7/finance/download/" + symbol
                                    + "?period1=1433894400&period2=1591747200&interval=1d&events=history&includeAdjustedClose=true")
                            , exchange.path + "individual/" + symbol + ".csv");
                    System.out.println("SUCCESS!!!!!!!!!!");
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public static void downloadFile(URL url, String fileName) throws Exception {
        try (InputStream in = url.openStream()) {
            Files.copy(in, Paths.get(fileName));
        }
    }
}
