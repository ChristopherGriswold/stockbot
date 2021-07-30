package com.iceybones.capstone;

import java.io.IOException;

public class Testbed {
    public static void main(String[] args) throws IOException {
//        try (var in = Files.newBufferedReader(Path.of("train/2x.txt"));
//             var out = Files.newBufferedWriter(Path.of("test/2.txt"));
//             var out2 = Files.newBufferedWriter(Path.of("train/2.txt"))) {
//            List<String> lines = in.lines().collect(Collectors.toList());
//            Collections.shuffle(lines);
//            int counter = 0;
//            for (var line : lines) {
//                if (counter >= 12000) {
//                    break;
//                }
//                if (++counter % 3 == 0) {
//                    out.write(line);
//                    out.newLine();
//                } else {
//                    out2.write(line);
//                    out2.newLine();
//                }
//            }
//        }

//        var stocks = Files.newBufferedReader(Path.of("stocks/nyse/nyse_all.csv"))
//                .lines().collect(Collectors.toList());
//        stocks.addAll(Files.newBufferedReader(Path.of("stocks/nasdaq/nasdaq_all.csv"))
//                .lines().skip(1).collect(Collectors.toList()));
//        try(var out = Files.newBufferedWriter(Path.of("stocks/all.csv"))) {
//            for (var stock : stocks) {
//                out.write(stock);
//                out.newLine();
//            }
//        }

//        try (var in = Files.newBufferedReader(Path.of("stocks/all.csv"));
//             var out = Files.newBufferedWriter(Path.of("stocks/all_percent.csv"), StandardOpenOption.CREATE)) {
//            for (var line : in.lines().collect(Collectors.toList())) {
//                if(line.contains("Open")) {
//                    out.write(line + "," + "high/open,low/open");
//                    out.newLine();
//                    continue;
//                }
//                var lineSplit = line.split(",");
//                double openPercent = 1;
//                double closePercent = 1;
//                if (Double.parseDouble(lineSplit[2]) != 0) {
//                    openPercent = Double.parseDouble(lineSplit[3]) / Double.parseDouble(lineSplit[2]);
//                }
//                if (Double.parseDouble(lineSplit[2]) != 0) {
//                    closePercent = Double.parseDouble(lineSplit[4]) / Double.parseDouble(lineSplit[2]);
//                }
//                out.write(line + "," + openPercent + "," + closePercent);
//                out.newLine();
//            }
//        }
    }
}
