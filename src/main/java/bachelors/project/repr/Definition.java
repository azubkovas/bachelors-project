package bachelors.project.repr;

import bachelors.project.repr.changetype.ChangeType;
import bachelors.project.repr.cond.Condition;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Definition {
    private final ChangeType pattern;
    private final Condition condition;

    public Definition(ChangeType pattern, Condition condition) {
        this.pattern = pattern;
        this.condition = condition;
    }

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

    public ChangeType getPattern() {
        return pattern;
    }

    public Condition getCondition() {
        return condition;
    }
}
