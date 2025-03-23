package org.defdiff.deflang.cond;

import org.defdiff.deflang.Definition;
import org.defdiff.deflang.VariableContainer;
import org.defdiff.util.DiffData;
import com.github.gumtreediff.actions.model.Action;

public class ExistentialQuantification extends Condition {
    private final Definition definition;

    public ExistentialQuantification(Definition definition) {
        this.definition = definition;
    }

    @Override
    public Object evaluate(VariableContainer variables, DiffData diffData) {
        for (Action action : diffData.getAllActions()) {
            VariableContainer innerVariables = new VariableContainer(variables);
            if (definition.matchesAction(action, diffData, innerVariables)) return true;
        }
        return false;
    }
}
