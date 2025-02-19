package bachelors.project.repr.cond;

import bachelors.project.util.DiffData;

import java.util.Map;

public interface Evaluatable {
    Object evaluate(Map<String, Object> variables, DiffData diffData);
}
