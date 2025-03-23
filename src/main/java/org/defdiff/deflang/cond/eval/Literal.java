package org.defdiff.deflang.cond.eval;

import org.defdiff.deflang.VariableContainer;
import org.defdiff.util.DiffData;

public class Literal implements Evaluatable {
    private final String value;

    public Literal(String value) {
        this.value = value;
    }

    @Override
    public Object evaluate(VariableContainer variables, DiffData diffData) {
        return value;
    }
}
