package org.defdiff.deflang.cond;

import org.defdiff.deflang.SimpleDefinition;
import org.defdiff.deflang.VariableContainer;
import org.defdiff.util.DiffData;
import com.github.gumtreediff.actions.model.Action;

public class ExistentialQuantification extends Condition {
    private final SimpleDefinition simpleDefinition;

    public ExistentialQuantification(SimpleDefinition simpleDefinition) {
        this.simpleDefinition = simpleDefinition;
    }

    @Override
    public Boolean evaluate(VariableContainer variables, DiffData diffData) {
        for (Action action : diffData.getAllActions()) {
            VariableContainer innerVariables = new VariableContainer(variables);
            if (simpleDefinition.matchesAction(action, diffData, innerVariables)) return true;
        }
        return false;
    }
}
