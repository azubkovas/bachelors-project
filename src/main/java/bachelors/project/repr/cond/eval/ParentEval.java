package bachelors.project.repr.cond.eval;

import bachelors.project.repr.VariableContainer;
import bachelors.project.repr.VariableValue;
import bachelors.project.util.DiffData;

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
