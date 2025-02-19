package bachelors.project;

import bachelors.project.repr.Definition;
import bachelors.project.repr.changepattern.UpdatePattern;
import bachelors.project.util.DiffData;
import bachelors.project.util.JoernManager;
import com.github.gumtreediff.actions.model.Action;

import java.io.IOException;
import java.util.*;

public class ChangeFinder {
    public static Set<Action> findChanges(DiffData diffData, List<Definition> definitions) throws IOException {
        Set<Action> changes = new HashSet<>();
        Set<Action> allChanges = diffData.getAllActions();
        JoernManager.getInstance().executeQuery("""
                if (openForInputPath("%s").isEmpty) {
                      importCode("%s")
                   }""".formatted(diffData.getPrePatchRevisionPath(), diffData.getPrePatchRevisionPath()));
        for (Definition definition : definitions) {
            Map<String, Object> variables = new HashMap<>();
            for (Action action : allChanges) {
                if (
                        definition.getPattern() instanceof UpdatePattern updatePattern &&
                                action instanceof com.github.gumtreediff.actions.model.Update updateAction &&
                                JoernManager.checkNodeOfRequiredType(updateAction.getNode(), updatePattern.getTarget().getNodePattern().getNodeType())
                ) {
                    variables.put(updatePattern.getOld(), updateAction.getNode().getLabel());
                    variables.put(updatePattern.getNew_(), updateAction.getValue());
                    updatePattern.getTarget().getNodePattern().setCorrespondingNode(updateAction.getNode());
                    variables.put(updatePattern.getTarget().getLabel(), updatePattern.getTarget().getNodePattern());
                    if ((Boolean) definition.getCondition().evaluate(variables, diffData)) changes.add(updateAction);
                }
            }
        }
        return changes;
    }
}
