package bachelors.project.repr.cond.eval;

import bachelors.project.repr.VariableContainer;
import bachelors.project.util.DiffData;

public interface Evaluatable {
    Object evaluate(VariableContainer variables, DiffData diffData);
}
