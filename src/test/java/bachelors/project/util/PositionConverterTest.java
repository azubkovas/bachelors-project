package bachelors.project.util;

import com.github.gumtreediff.utils.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;


public class PositionConverterTest {
    @Test
    public void testGetLineAndColumn() throws IOException {
        String filePath = "src/test/data/TestFile.java";
        int charPosition = 0;

        Pair<Integer, Integer> res = PositionConverter.getLineAndColumn(filePath, charPosition);

        assertEquals(1, res.first);
        assertEquals(1, res.second);
    }

    @Test
    public void testGetLineAndColumn2() throws IOException {
        String filePath = "src/test/data/TestFile.java";
        int charPosition = 47;

        Pair<Integer, Integer> res = PositionConverter.getLineAndColumn(filePath, charPosition);

        assertEquals(2, res.first);
        assertEquals(24, res.second);
    }

    @Test
    public void testGetLineAndColumn3() throws IOException {
        String filePath = "src/test/data/TestFile.java";
        int charPosition = 97;

        Pair<Integer, Integer> res = PositionConverter.getLineAndColumn(filePath, charPosition);

        assertEquals(3, res.first);
        assertEquals(29, res.second);
    }
}
