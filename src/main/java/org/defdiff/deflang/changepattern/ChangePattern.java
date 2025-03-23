package org.defdiff.deflang.changepattern;

import org.defdiff.deflang.VariableContainer;
import com.github.gumtreediff.actions.model.Action;

public abstract class ChangePattern {
    public abstract boolean matchesAction(Action action, VariableContainer variables);
}
