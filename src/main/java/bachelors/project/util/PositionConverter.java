package bachelors.project.util;

import com.github.gumtreediff.utils.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PositionConverter {
    public static Pair<Integer, Integer> getLineAndColumn(String filePath, int charPosition) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            int line = 1;
            int column = 1;
            int currentCharPosition = 0;

            String lineContent;
            while ((lineContent = reader.readLine()) != null) {
                int lineLength = lineContent.length() + 1; // Include newline character
                if (currentCharPosition + lineLength > charPosition) {
                    column = charPosition - currentCharPosition + 1;
                    break;
                }
                currentCharPosition += lineLength;
                line++;
            }
            return new Pair<>(line, column);
        }
    }
}
