package com.iceybones.capstone;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CleanNews {
    public static Path news = Path.of("stocks/news/analyst_ratings_processed.csv");
    public static List<String> blacklist = new ArrayList<>();
//            List.of("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday",
//            "January", "February", "March", "April", "May", "June", "July", "August", "September", "October",
//            "November", "December");
    public static Map<String, String> nameSymbols;

    static {
        try {
            nameSymbols = Files.newBufferedReader(Path.of("stocks/nasdaq/symbols.csv")).lines()
                    .collect(Collectors.toMap((a) -> a.split(",")[0], (b) -> b.split(",")[1]));
            var nyse = Files.newBufferedReader(Path.of("stocks/nyse/symbols.csv")).lines()
                    .collect(Collectors.toMap((a) -> a.split(",")[0], (b) -> b.split(",")[1]));
            nameSymbols.putAll(nyse);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        try (var newsIn = Files.newBufferedReader(news)) {
            List<String> lines = newsIn.lines().collect(Collectors.toList());
            cleanLines(lines);
        }
    }

    private static String cleanLine(String line) {
        String symbol = line.substring(line.lastIndexOf(",") + 1);
        if (!nameSymbols.containsKey(symbol) || line.lastIndexOf(",") < 27) {
            return null;
        }
        String date = line.substring(line.lastIndexOf(",") - 25, line.lastIndexOf(","));
//        line = line.replace(symbol, "");
        System.out.println(line);
        line = line.replace(nameSymbols.get(symbol), "");
        line = line.toLowerCase().replaceAll("[0123456789.,'\"!@#$%^&*â„;()¢/:+=_®~`?’><—-]", "");
        line = line.trim().replaceAll(" +", " ");
        return date + "," + symbol + "," + line;
    }
    public static String cleanLine(String line, String symbol) {
//        line = line.replace(symbol, "");
        if(!nameSymbols.containsKey(symbol)) {
            return null;
        }
        line = line.replace(nameSymbols.get(symbol), "");
        line = line.toLowerCase().replaceAll("[0123456789.,'\"!@#$%^&*â„;()¢/:+=_®~`?’><—-]", "");
        line = line.trim().replaceAll(" +", " ");
        return line;
    }

    private static void cleanLines(List<String> lines) throws IOException {
        try (var out = Files.newBufferedWriter(Path.of("news/newsProcessed.csv"))) {
            for (var line : lines) {
//                if (line.lastIndexOf(",") < 27 || !nameSymbols.containsKey(line.substring(line.lastIndexOf(",") + 1))) {
//                } else {
                    String cleanLine = cleanLine(line);
                    if (cleanLine != null) {
                        out.write(cleanLine);
                        out.newLine();
                    }
//                }
            }
        }
    }
}
