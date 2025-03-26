package org.defdiff;

import org.defdiff.deflang.Definition;
import org.defdiff.deflang.SimpleDefinition;
import com.github.gumtreediff.actions.model.Action;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ChangesContainer {
    private final Map<Definition, Set<Action>> changes;

    public ChangesContainer() {
        this.changes = new HashMap<>();
    }

    public void addChanges(Definition definition, Set<Action> changes) {
        if (!this.changes.containsKey(definition)) {
            this.changes.put(definition, new HashSet<>());
        }
        this.changes.get(definition).addAll(changes);
    }

    public Set<Action> getChanges(SimpleDefinition simpleDefinition) {
        return changes.get(simpleDefinition);
    }

    public Set<Action> getAllChanges() {
        Set<Action> result = new HashSet<>();
        for (Set<Action> actions : changes.values()) {
            result.addAll(actions);
        }
        return result;
    }

    public void printChangeCounts() {
        for (Map.Entry<Definition, Set<Action>> entry : changes.entrySet()) {
            System.out.println("Number of changes matching " + entry.getKey().toString() + ": " + entry.getValue().size());
        }
    }

    public void printChanges() {
        for (Map.Entry<Definition, Set<Action>> entry : changes.entrySet()) {
            System.out.println("Changes matching " + entry.getKey().toString() + ":");
            for (Action action : entry.getValue()) {
                System.out.println(action.toString());
            }
        }
    }
}
