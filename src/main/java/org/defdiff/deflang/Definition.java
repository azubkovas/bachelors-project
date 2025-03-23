package org.defdiff.deflang;

import org.defdiff.deflang.changepattern.ChangePattern;
import org.defdiff.deflang.cond.Condition;
import org.defdiff.util.DiffData;
import com.github.gumtreediff.actions.model.Action;

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
