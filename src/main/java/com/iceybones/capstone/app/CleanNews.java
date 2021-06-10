package com.iceybones.capstone.app;

import com.iceybones.capstone.controllers.MainController;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import net.jacobpeterson.alpaca.rest.exception.AlpacaAPIRequestException;

public class CleanNews {

  public static Path news = Path.of("data/news/analyst_ratings_processed.csv");

  public static void main(String[] args) throws IOException, AlpacaAPIRequestException {
//    trim();

    try (var newsIn = Files.newBufferedReader(news)) {
      List<String> lines = newsIn.lines().collect(Collectors.toList());
      cleanLines(lines);
    }
  }

  public static void trim() throws IOException {
    var lineSet = Files.newBufferedReader(Path.of("data/news/newsProcessed.csv")).lines().collect(
        Collectors.toSet());
    try (var out = Files.newBufferedWriter(Path.of("data/news/news_processed.txt"))) {
      for (var line : lineSet) {
        if (!line.isEmpty()) {
          out.write(line);
          out.newLine();
        }
      }
    }
  }

  public static String cleanLine(String line, String symbol, boolean removeT) {
    String name = "";
    if (MainController.usEquities.containsKey(symbol)) {
      name = MainController.usEquities.get(symbol).getName().toLowerCase()
          .replaceAll("[0123456789.,'!@#$%^™&*„;()¢/:+=_®~`?’><—-]", "");
    }
    name = name.replace("class", "");
    name = name.replace("common stock", "");
    line = line.toLowerCase().replaceAll("[0123456789.,'!@#$%^™&*„;()¢/:+=_®~`?’><—-]", " ");
      for (var element : name.split(" ")) {
        if (element.length() > 1) {
          line = line.replace(element, "");
        }
      }
    line = line.replace("\"","");

    line = line.trim().replaceAll(" +", " ");
    if (!removeT) {
      return line;
    }
    return line.length() > 0 ? line.substring(1) : line;
  }

  private static void cleanLines(List<String> lines) throws IOException {
    try (var out = Files.newBufferedWriter(Path.of("data/news/newsProcessed.csv"))) {
      for (var line : lines) {
          var split = line.split(",");
        String cleanLine = cleanLine(line, split[split.length - 1], false);
        if (!cleanLine.isEmpty()) {
          out.write(cleanLine);
          out.newLine();
        }
      }
    }
  }
}
