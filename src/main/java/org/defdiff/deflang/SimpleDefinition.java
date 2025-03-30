package org.defdiff.deflang;

import org.defdiff.deflang.changepattern.ChangePattern;
import org.defdiff.deflang.cond.Condition;
import org.defdiff.util.DiffData;
import com.github.gumtreediff.actions.model.Action;

import java.util.HashSet;
import java.util.Set;

public class SimpleDefinition extends Definition {
    @Override
    public Set<Action> filterChanges(DiffData diffData, VariableContainer variables) {
        Set<Action> matchingActions = new HashSet<>();
        for (Action action : diffData.getAllActions()) {
            VariableContainer vars = new VariableContainer(variables);
            if (matchesAction(action, diffData, vars)) {
                matchingActions.add(action);
            }
        }
        return matchingActions;
    }

    private final ChangePattern pattern;
    private final Condition condition;
    private String definitionStr;

    public SimpleDefinition(ChangePattern pattern, Condition condition, String definitionStr) {
        super(definitionStr);
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
