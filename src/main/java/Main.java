import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        var stockFolder = Path.of("stocks/individual/");
        List<File> files = Files.list(stockFolder)
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .sorted()
                .collect(Collectors.toList());
        var outPath = Path.of("stocks/nasdaq.csv");
        try (var bufOut = Files.newBufferedWriter(outPath)) {
            for (var file : files) {
                try (var inFile = Files.newBufferedReader(file.toPath())) {
                    String line = inFile.readLine();
                    while ((line = inFile.readLine()) != null) {
                        if (line.split(",")[1].equals("null") || line.split(",")[6].equals("0")) {
                            continue;
                        }
                        line = line.substring(0, 11) + file.getName().split("\\.")[0] + "," + line.substring(11);
                        bufOut.write(line);
                        bufOut.newLine();
                    }
                }
            }
            bufOut.flush();
        }
    }
}
