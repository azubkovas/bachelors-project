package bachelors.project.repr.cond;

import java.util.Map;

public class Literal<T> extends Condition {
    private final T value;

    public Literal(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public <U> U evaluate(Map<String, String> variables) {
        if (value instanceof String stringVar)
            return (U) ('"' + stringVar + '"');
        return (U) value;
    }
}
