package org.defdiff.deflang.cond;

import org.defdiff.deflang.VariableContainer;
import org.defdiff.util.DiffData;

public class NotCondition extends Condition {
    private final Condition negatedCondition;

    public NotCondition(Condition negatedCondition) {
        this.negatedCondition = negatedCondition;
    }

    @Override
    public Object evaluate(VariableContainer variables, DiffData diffData) {
        return !((Boolean) negatedCondition.evaluate(variables, diffData));
    }
}
