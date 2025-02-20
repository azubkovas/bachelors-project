package bachelors.project.repr.cond;

import bachelors.project.repr.nodepattern.VariableContainer;
import bachelors.project.util.DiffData;

import java.util.Map;

public interface Evaluatable {
    Object evaluate(VariableContainer variables, DiffData diffData);
}
