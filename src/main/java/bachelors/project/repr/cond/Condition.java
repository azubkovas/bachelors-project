package bachelors.project.repr.cond;

import bachelors.project.util.DiffData;

import java.util.Map;

public abstract class Condition implements Evaluatable {
    public abstract Object evaluate(Map<String, Object> variables, DiffData diffData);
}
