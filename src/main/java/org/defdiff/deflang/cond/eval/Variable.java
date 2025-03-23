package org.defdiff.deflang.cond.eval;

import org.defdiff.deflang.NotWellFormedException;
import org.defdiff.deflang.VariableContainer;
import org.defdiff.deflang.VariableValue;
import org.defdiff.util.DiffData;

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
