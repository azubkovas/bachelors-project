package bachelors.project;

import bachelors.project.repr.Definition;
import bachelors.project.repr.VariableContainer;
import bachelors.project.util.DiffData;
import bachelors.project.util.JoernClient;
import com.github.gumtreediff.actions.model.Action;

import java.io.IOException;
import java.util.*;

public class ChangeFinder {
    public static Set<Action> findChanges(DiffData diffData, List<Definition> definitions) throws IOException {
        Set<Action> changes = new HashSet<>();
        Set<Action> allChanges = diffData.getAllActions();
        JoernClient.getInstance().executeQuery("""
                if (openForInputPath("%s").isEmpty) {
                      importCode("%s")
                   }""".formatted(diffData.getPrePatchRevisionPath(), diffData.getPrePatchRevisionPath()));
        for (Definition definition : definitions) {
            for (Action action : allChanges) {
                VariableContainer variables = new VariableContainer();
                if (definition.matchesAction(action, diffData, variables)) {
                    changes.add(action);
                }
            }
        }
        return changes;
    }

}
