package bachelors.project.repr.cond;

import bachelors.project.repr.VariableContainer;
import bachelors.project.util.DiffData;

public class OrCondition extends Condition {
    private final Condition left;
    private final Condition right;

    public OrCondition(Condition left, Condition right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Boolean evaluate(VariableContainer variables, DiffData diffData) {
        return (Boolean) left.evaluate(variables, diffData) || (Boolean) right.evaluate(variables, diffData);
    }
}
