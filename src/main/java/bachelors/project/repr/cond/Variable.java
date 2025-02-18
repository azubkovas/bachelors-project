package bachelors.project.repr.cond;

import bachelors.project.repr.NotWellFormedException;

import java.util.Map;

public class Variable extends Condition {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public <T> T evaluate(Map<String, String> variables) {
        if (variables.containsKey(name)) {
            return (T) variables.get(name);
        }
        throw new NotWellFormedException("Variable " + name + " not found in the given map");
    }
}
