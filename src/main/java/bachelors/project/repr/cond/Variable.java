package bachelors.project.repr.cond;

import bachelors.project.repr.NotWellFormedException;
import bachelors.project.util.DiffData;

import java.util.Map;

public class Variable implements Evaluatable {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Object evaluate(Map<String, Object> variables, DiffData diffData) throws NotWellFormedException {
        if (variables.containsKey(name)) {
            return variables.get(name);
        }
        throw new NotWellFormedException("Variable " + name + " not found in the given map");
    }
}
