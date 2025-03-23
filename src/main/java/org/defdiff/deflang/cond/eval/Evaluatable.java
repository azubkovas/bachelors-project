package org.defdiff.deflang.cond.eval;

import org.defdiff.deflang.VariableContainer;
import org.defdiff.util.DiffData;

public interface Evaluatable {
    Object evaluate(VariableContainer variables, DiffData diffData);
}
