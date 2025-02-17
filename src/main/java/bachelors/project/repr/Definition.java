package bachelors.project.repr;

import bachelors.project.repr.changetype.ChangeType;
import bachelors.project.repr.filter.Filter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Definition {
    ChangeType pattern;
    List<Filter> filters;

    public static List<Definition> getDefinitions(String filePath) {
        List<Definition> definitions = new ArrayList<Definition>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                definitions.add(getDefinition(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return definitions;
    }

    public static Definition getDefinition(String defString) {
        return null; // todo
    }
}
