package bachelors.project.repr;

import bachelors.project.repr.changepattern.ChangePattern;
import bachelors.project.repr.cond.Condition;
import bachelors.project.util.DiffData;
import com.github.gumtreediff.actions.model.Action;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Definition {
    private final ChangePattern pattern;
    private final Condition condition;
    private String definitionStr;

    public Definition(ChangePattern pattern, Condition condition) {
        this.pattern = pattern;
        this.condition = condition;
    }

    public boolean matchesAction(Action action, DiffData diffData, VariableContainer variables) {
        return pattern.matchesAction(action, variables) && (condition == null || (Boolean) condition.evaluate(variables, diffData));
    }

    public ChangePattern getPattern() {
        return pattern;
    }

    public Condition getCondition() {
        return condition;
    }

    public String getDefinitionStr() {
        return definitionStr;
    }

    public void setDefinitionStr(String definitionStr) {
        this.definitionStr = definitionStr;
    }
}
