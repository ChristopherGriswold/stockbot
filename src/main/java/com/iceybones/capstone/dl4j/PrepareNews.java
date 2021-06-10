package com.iceybones.capstone.dl4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrepareNews {

  public static int classify(double buy, double strongBuy) {
    buy /= 100;
    strongBuy /= 100;
    Map<Integer, List<String>> newsMap = new HashMap<>();
//    newsMap.put(0, new HashSet<>());
//    newsMap.put(1, new HashSet<>());
//    newsMap.put(2, new HashSet<>());
    newsMap.put(0, new ArrayList<>());
    newsMap.put(1, new ArrayList<>());
    newsMap.put(2, new ArrayList<>());
    int minLines = 0;

    try (var allIn = Files
        .newBufferedReader(Path.of("data/news/news_priced.csv"));
//        var allOut = Files
//            .newBufferedWriter(Path.of("data/news/labeled_news/train/all_news.txt"));
        var strongBuyOut = Files
            .newBufferedWriter(Path.of("data/news/labeled_news/train/2_all.txt"),
                StandardOpenOption.CREATE);
        var buyOut = Files
            .newBufferedWriter(Path.of("data/news/labeled_news/train/1_all.txt"),
                StandardOpenOption.CREATE);
        var ignoreOut = Files
            .newBufferedWriter(Path.of("data/news/labeled_news/train/0_all.txt"),
                StandardOpenOption.CREATE);
    ) {
      String line = allIn.readLine();
      while ((line = allIn.readLine()) != null) {
        var lines = line.split(",");
        var date = LocalDate.of(Integer.parseInt(line.substring(0, 4)),
            Integer.parseInt(line.substring(5, 7)), Integer.parseInt(line.substring(8, 10)));
        if (Double.parseDouble(lines[2]) - TestNews.marketPerformance.get(date) >= strongBuy) {
          newsMap.get(2).add(lines[3]);
        } else if (Double.parseDouble(lines[2]) - TestNews.marketPerformance.get(date) >= buy) {
          newsMap.get(1).add(lines[3]);
        }
//        if ((Double.parseDouble(lines[3]) / Double.parseDouble(lines[2])) / Double.parseDouble(lines[6]) >= strongBuy) {
//          newsMap.get(2).add(lines[7]);
//        } else if ((Double.parseDouble(lines[3]) / Double.parseDouble(lines[2])) / Double.parseDouble(lines[6]) >= buy) {
//          newsMap.get(1).add(lines[7]);
//        }
        else {
          newsMap.get(0).add(lines[3]);
        }
      }
//      List<String> tempBuy = new ArrayList<>();
//      for (var news : newsMap.get(1)) {
////        if (!newsMap.get(0).contains(news)) {
////          newsMap.get(2).remove(news);
//          tempBuy.add(news);
////        }
//      }
//      List<String> tempStrongBuy = new ArrayList<>();
//      for (var news : newsMap.get(2)) {
////        if (!newsMap.get(0).contains(news)) {
//          tempStrongBuy.add(news);
////        }
//      }
      for (var news : newsMap.get(2)) {
        strongBuyOut.append(news);
        strongBuyOut.newLine();
//        allOut.append(news);
//        allOut.newLine();
      }
      for (var news : newsMap.get(1)) {
        buyOut.append(news);
        buyOut.newLine();
//        allOut.append(news);
//        allOut.newLine();
      }
      for (var news : newsMap.get(0)) {
        ignoreOut.append(news);
        ignoreOut.newLine();
//        allOut.append(news);
//        allOut.newLine();
      }
      minLines = Math.min(newsMap.get(0).size(), Math.min(newsMap.get(1).size(), newsMap.get(2).size()));
//      minLines = Math.min(newsMap.get(0).size(), Math.min(tempBuy.size(), tempStrongBuy.size()));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return minLines;
  }

}
