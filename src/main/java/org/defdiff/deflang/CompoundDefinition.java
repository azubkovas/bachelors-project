package org.defdiff.deflang;

import com.github.gumtreediff.actions.model.Action;
import org.defdiff.ChangesContainer;
import org.defdiff.util.DiffData;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CompoundDefinition extends Definition {
    private List<SimpleDefinition> simpleDefinitions;

    public CompoundDefinition(List<SimpleDefinition> simpleDefinitions) {
        this.simpleDefinitions = simpleDefinitions;
    }

    @Override
    public Set<Action> filterChanges(DiffData diffData, ChangesContainer changesContainer, VariableContainer variables) {
        Set<Action> allMatchingActions = new HashSet<>();
        for (SimpleDefinition simpleDefinition : simpleDefinitions) {
            Set<Action> matchingActions = simpleDefinition.filterChanges(diffData, changesContainer, variables);
            if (matchingActions.isEmpty()) {
                return new HashSet<>();
            } else allMatchingActions.addAll(matchingActions);
        }
        return allMatchingActions;
    }
}
