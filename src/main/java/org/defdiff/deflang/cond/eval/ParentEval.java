package org.defdiff.deflang.cond.eval;

import org.defdiff.deflang.VariableContainer;
import org.defdiff.deflang.VariableValue;
import org.defdiff.util.DiffData;

public class ParentEval implements Evaluatable {
    private final Evaluatable child;

    public ParentEval(Evaluatable child) {
        this.child = child;
    }

    @Override
    public Object evaluate(VariableContainer variables, DiffData diffData) {
        return ((VariableValue) child.evaluate(variables, diffData)).getCorrespondingNode().getParent();
    }
}
