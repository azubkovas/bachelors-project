package org.defdiff.deflang;

import org.defdiff.ChangesContainer;
import org.defdiff.util.DiffData;

import java.util.List;

public class Definitions {
    private final List<Definition> definitions;

    public Definitions(List<Definition> definitions) {
        this.definitions = definitions;
    }

    public void filterChanges(DiffData diffData, ChangesContainer changesContainer) {
        for (Definition definition : definitions) {
            changesContainer.addChanges(definition, definition.filterChanges(diffData, new VariableContainer()));
        }
    }
}
