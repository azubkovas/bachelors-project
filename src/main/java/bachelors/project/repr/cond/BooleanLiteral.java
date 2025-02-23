package bachelors.project.repr.cond;

import bachelors.project.repr.VariableContainer;
import bachelors.project.util.DiffData;

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
