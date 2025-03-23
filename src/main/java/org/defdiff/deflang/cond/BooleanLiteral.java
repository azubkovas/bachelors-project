package org.defdiff.deflang.cond;

import org.defdiff.deflang.VariableContainer;
import org.defdiff.util.DiffData;

public class BooleanLiteral<T> extends Condition {
    private final boolean value;

    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    @Override
    public Object evaluate(VariableContainer variables, DiffData diffData) {
        return value;
    }
}
