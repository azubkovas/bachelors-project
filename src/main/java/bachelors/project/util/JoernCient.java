package bachelors.project.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class JoernCient {
    public static String executeQuery(String query, String filePath) throws IOException {
        Files.deleteIfExists(Path.of("tmp/script.cpgql"));
        Files.deleteIfExists(Path.of("tmp/output.log"));
        String script = """
                import java.nio.file.{Files, Path, StandardOpenOption}
                import replpp.Operators.#>
                given replpp.Colors = replpp.Colors.BlackWhite
                
                @main def exec() = {
                   if (openForInputPath("%s").isEmpty) {
                      importCode("%s")
                   }
                   (%s) #> "tmp/output.log"
                }
                """.formatted(filePath, filePath, query);
        Files.writeString(Path.of("tmp/script.cpgql"), script, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("joern", "--script", "tmp/script.cpgql");
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
            int exitCode = process.waitFor();
//            System.out.println("Process finished with exit code " + exitCode);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        String queryOutput = Files.readString(Path.of("tmp/output.log"));
        return queryOutput;
    }
}
