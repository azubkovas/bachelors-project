package org.defdiff.deflang;

import com.github.gumtreediff.actions.model.Action;
import org.defdiff.ChangesContainer;
import org.defdiff.util.DiffData;

import java.util.Set;

public abstract class Definition {
    public abstract Set<Action> filterChanges(DiffData diffData, ChangesContainer changesContainer, VariableContainer variables);
}
