package bachelors.project;

import bachelors.project.repr.Definition;
import bachelors.project.repr.VariableContainer;
import bachelors.project.util.DiffData;
import bachelors.project.util.JoernClient;
import com.github.gumtreediff.actions.model.Action;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChangeFinder {
    public static Set<Action> findChanges(DiffData diffData, List<Definition> definitions) throws IOException {
        Set<Action> changes = new HashSet<>();
        Set<Action> allChanges = diffData.getAllActions();
        for (Action action : allChanges) {
            boolean addedToList = false;
            for (Definition definition : definitions) {
                VariableContainer variables = new VariableContainer();
                if (definition.matchesAction(action, diffData, variables)) {
                    changes.add(action);
                    addedToList = true;
                }
                if (addedToList) {
                    break;
                }
            }
        }
        return changes;
    }
}
