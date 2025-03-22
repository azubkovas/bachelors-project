package bachelors.project;

import bachelors.project.repr.Definition;
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

    public void addChange(Definition definition, Action action) {
        if (!changes.containsKey(definition)) {
            changes.put(definition, new HashSet<>());
        }
        changes.get(definition).add(action);
    }

    public Set<Action> getChanges(Definition definition) {
        return changes.get(definition);
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
            System.out.println("Number of changes matching " + entry.getKey().getDefinitionStr() + ": " + entry.getValue().size());
        }
    }

    public void printChanges() {
        for (Map.Entry<Definition, Set<Action>> entry : changes.entrySet()) {
            System.out.println("Changes matching " + entry.getKey().getDefinitionStr() + ":");
            for (Action action : entry.getValue()) {
                System.out.println(action.toString());
            }
        }
    }
}
