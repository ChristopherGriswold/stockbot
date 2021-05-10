import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ScrapeQuotesHistorical {
    public static void downloadFile(URL url, String fileName) throws Exception {
        try (InputStream in = url.openStream()) {
            Files.copy(in, Paths.get(fileName));
        }
    }
    static String saveFolder = "stocks/";
    static int counter = 0;

    public static void scrape() throws Exception {






//        for (int i = counter; i < symbols.size(); i++) {
//            if(Files.exists(Path.of("/home/chris/IdeaProjects/capstone/stocks/" + symbols.get(i) + ".csv"))) {
////                System.out.println("FILE ALREADY EXISTS");
//                badIo.add(symbols.get(i));
//            }}
//            System.out.println(i + ": " + symbols.get(i));
//            try
//            {
//                downloadFile(new URL("https://query1.finance.yahoo.com/v7/finance/download/" + symbols.get(i) + "?period1=1433894400&period2=1591747200&interval=1d&events=history&includeAdjustedClose=true"), saveFolder + symbols.get(i) + ".csv");
//                System.out.println("SUCCESS!!!!!!!!!!");
//            } catch (FileNotFoundException e) {
//                System.out.println("XXXXXX");
////                noFIle.add(symbols.get(i));
//            } catch (IOException f) {
//                System.out.println("00000");
////                badIo.add(symbols.get(i));
//
//            }
//        }
//        main(null);
//        try (var out = Files.newBufferedWriter(Path.of("/home/chris/IdeaProjects/capstone/stocks/symbols/symbols.csv"));
//        ) {
//            for (var el : badIo) {
//                out.write(el + ",");
//                out.newLine();
//            }
//            out.flush();
//        }

    }


}
