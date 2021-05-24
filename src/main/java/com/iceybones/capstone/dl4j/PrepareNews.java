package com.iceybones.capstone.dl4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PrepareNews {

  public static int classify(double buy, double strongBuy) {
    Map<Integer, Set<String>> newsMap = new HashMap<>();
    newsMap.put(0, new HashSet<>());
    newsMap.put(1, new HashSet<>());
    newsMap.put(2, new HashSet<>());
    int minLines = 0;
    try (var allIn = Files
        .newBufferedReader(Path.of("stocks/news/news_rua2.csv"));
        var allOut = Files
            .newBufferedWriter(Path.of("stocks/news/labeled_news/train/all_news.txt"));
        var strongBuyOut = Files
            .newBufferedWriter(Path.of("stocks/news/labeled_news/train/2_all.txt"));
        var buyOut = Files
            .newBufferedWriter(Path.of("stocks/news/labeled_news/train/1_all.txt"));
        var ignoreOut = Files
            .newBufferedWriter(Path.of("stocks/news/labeled_news/train/0_all.txt"));
    ) {
      String line = allIn.readLine();
      while ((line = allIn.readLine()) != null) {
        var lines = line.split(",");
        if (Double.parseDouble(lines[2]) / Double.parseDouble(lines[4]) >= strongBuy) {
          newsMap.get(2).add(lines[5]);
        } else if (Double.parseDouble(lines[2]) / Double.parseDouble(lines[4]) >= buy) {
          newsMap.get(1).add(lines[5]);
        }
        else {
          newsMap.get(0).add(lines[5]);
        }
      }
      List<String> tempBuy = new ArrayList<>();
      for (var news : newsMap.get(1)) {
        if (!newsMap.get(0).contains(news)) {
          newsMap.get(2).remove(news);
          tempBuy.add(news);
        }
      }
      List<String> tempStrongBuy = new ArrayList<>();
      for (var news : newsMap.get(2)) {
        if (!newsMap.get(0).contains(news)) {
          tempStrongBuy.add(news);
        }
      }
      for (var news : tempStrongBuy) {
        strongBuyOut.append(news);
        strongBuyOut.newLine();
        allOut.append(news);
        allOut.newLine();
      }
      for (var news : tempBuy) {
        buyOut.append(news);
        buyOut.newLine();
        allOut.append(news);
        allOut.newLine();
      }
      for (var news : newsMap.get(0)) {
        ignoreOut.append(news);
        ignoreOut.newLine();
        allOut.append(news);
        allOut.newLine();
      }
      minLines = Math.min(newsMap.get(0).size(), Math.min(tempBuy.size(), tempStrongBuy.size()));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return minLines;
  }

}
