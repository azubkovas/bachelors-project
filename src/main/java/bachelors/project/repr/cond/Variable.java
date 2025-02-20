package bachelors.project.repr.cond;

import bachelors.project.repr.NotWellFormedException;
import bachelors.project.repr.nodepattern.Literal;
import bachelors.project.repr.nodepattern.VariableContainer;
import bachelors.project.repr.nodepattern.VariablePattern;
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

    public Object evaluate(VariableContainer variables, DiffData diffData) throws NotWellFormedException {
        if (variables.contains(name)) {
            VariablePattern variablePattern = variables.get(name);
            if (variablePattern.getCorrespondingNodePattern() != null && variablePattern.getCorrespondingNodePattern() instanceof Literal literal) {
                return literal.getValue();
            }
            return variables.get(name);
        }
        throw new NotWellFormedException("VariablePattern " + name + " not found in the given map");
    }
}
