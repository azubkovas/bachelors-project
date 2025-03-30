package org.defdiff.deflang;

import com.github.gumtreediff.actions.model.Action;
import org.defdiff.util.DiffData;

import java.util.Set;

public abstract class Definition {
    private final String definitionString;

    @Override
    public String toString() {
        return definitionString;
    }

    protected Definition(String definitionString) {
        this.definitionString = definitionString;
    }

    public abstract Set<Action> filterChanges(DiffData diffData, VariableContainer variables);
}
