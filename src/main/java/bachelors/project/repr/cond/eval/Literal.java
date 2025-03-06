package bachelors.project.repr.cond.eval;

import bachelors.project.repr.VariableContainer;
import bachelors.project.util.DiffData;

public class Literal implements Evaluatable {
    private final String value;

    public Literal(String value) {
        this.value = value;
    }

    @Override
    public Object evaluate(VariableContainer variables, DiffData diffData) {
        return value;
    }
}
