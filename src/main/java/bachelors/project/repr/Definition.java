package bachelors.project.repr;

import bachelors.project.repr.changepattern.ChangePattern;
import bachelors.project.repr.cond.Condition;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Definition {
    private final ChangePattern pattern;
    private final Condition condition;

    public Definition(ChangePattern pattern, Condition condition) {
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

    public ChangePattern getPattern() {
        return pattern;
    }

    public Condition getCondition() {
        return condition;
    }
}
