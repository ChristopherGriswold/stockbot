import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Merge {
    public static void main(String[] args) throws IOException {
        Map<String, Map<String, List<String>>> newsMap = new HashMap<>();
        var stocksPath = Path.of("stocks/nasdaq.csv");
        var newsPath = Path.of("news/headlines.csv");
        var outNews = Path.of("news/headlines_price.csv");
        try {var stocks = Files.newBufferedReader(stocksPath);
            var news = Files.newBufferedReader(newsPath);
            var out = Files.newBufferedWriter(outNews);
            String line = null;
            while ((line = news.readLine()) != null) {
                var lines = line.split(",");
                var symbol = lines[1];
                var date = lines[0];
                var headline = line.substring(line.indexOf("\""));
//                out.write(date + "," + symbol + "," + headline);
//                out.newLine();
//                System.out.println(date + "," + symbol + "," + headline);
                newsMap.putIfAbsent(symbol, new HashMap<>());

            }
            news = Files.newBufferedReader(newsPath);
            while ((line = news.readLine()) != null) {
                var lines = line.split(",");
                var symbol = lines[1];
                var date = lines[0];
                var headline = line.substring(line.indexOf("\""));
//                out.write(date + "," + symbol + "," + headline);
//                out.newLine();
//                System.out.println(date + "," + symbol + "," + headline);
                newsMap.get(symbol).putIfAbsent(date, new ArrayList<>());

            }
            news = Files.newBufferedReader(newsPath);
            while ((line = news.readLine()) != null) {
                var lines = line.split(",");
                var symbol = lines[1];
                var date = lines[0];
                var headline = line.substring(line.indexOf("\""));
//                out.write(date + "," + symbol + "," + headline);
//                out.newLine();
//                System.out.println(date + "," + symbol + "," + headline);
                newsMap.get(symbol).get(date).add(headline);
            }
            String sLine = null;
            while ((sLine = stocks.readLine()) != null) {
                var lines = sLine.split(",");
                var symbol = lines[1];
                var date = lines[0];
//                var headline = lines[2];
//                out.write(date + "," + symbol + "," + headline);
//                out.newLine();
//                System.out.println(date + "," + symbol + "," + headline);
                StringBuilder heads = new StringBuilder();
                if (newsMap.get(symbol) != null && newsMap.get(symbol).get(date) != null) {
                    for (var el : newsMap.get(symbol).get(date)) {
                        heads.append(",").append(el);
                    }
                }
                if (heads.length() > 0) {
                    out.write(sLine + heads);
                    out.newLine();
//                    System.out.println(sLine + heads);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
