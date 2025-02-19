package bachelors.project.repr.cond;

import bachelors.project.util.DiffData;

import java.util.Map;

public class BooleanLiteral<T> extends Condition {
    private final boolean value;

    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    @Override
    public Object evaluate(Map<String, Object> variables, DiffData diffData) {
        return value;
    }
}
