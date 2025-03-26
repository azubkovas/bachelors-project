package org.defdiff;

import org.defdiff.deflang.Definitions;
import org.defdiff.deflang.SimpleDefinition;
import org.defdiff.deflang.VariableContainer;
import org.defdiff.util.DiffData;

import java.io.IOException;

public class ChangeFinder {
    public static ChangesContainer findChanges(DiffData diffData, Definitions definitions) throws IOException {
        ChangesContainer changes = new ChangesContainer();
        definitions.filterChanges(diffData, changes);
        return changes;
    }
}
