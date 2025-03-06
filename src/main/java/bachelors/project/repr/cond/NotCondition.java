package bachelors.project.repr.cond;

import bachelors.project.repr.VariableContainer;
import bachelors.project.util.DiffData;

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
