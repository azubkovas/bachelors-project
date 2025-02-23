package bachelors.project.repr.cond.eval;

import bachelors.project.repr.NotWellFormedException;
import bachelors.project.repr.VariableValue;
import bachelors.project.repr.nodepattern.LiteralPattern;
import bachelors.project.repr.VariableContainer;
import bachelors.project.repr.nodepattern.VariablePattern;
import bachelors.project.util.DiffData;

public class Variable implements Evaluatable {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public VariableValue evaluate(VariableContainer variables, DiffData diffData) throws NotWellFormedException {
        if (variables.contains(name)) {
            return variables.get(name);
        }
        throw new NotWellFormedException("VariablePattern " + name + " not found in the given map");
    }
}
