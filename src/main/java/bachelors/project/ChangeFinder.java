package bachelors.project;

import bachelors.project.repr.Definition;
import bachelors.project.repr.changetype.Update;
import bachelors.project.util.DiffData;
import com.github.gumtreediff.actions.model.Action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeFinder {
    public static List<Action> findChanges(DiffData diffData, List<Definition> definitions) {
        List<Action> changes = new ArrayList<>();
        List<Action> allChanges = diffData.getAllActions();
        for (Definition definition : definitions) {
            Map<String, String> variables = new HashMap<>();
            for (Action action : allChanges) {
                if (
                        definition.getPattern() instanceof Update updatePattern &&
                                action instanceof com.github.gumtreediff.actions.model.Update updateAction &&
                                updatePattern.getTarget().getNodeType().getStandardName().equals(updateAction.getNode().getType().name)
                ) {
                    variables.put(updatePattern.getOld(), updateAction.getNode().getLabel());
                    variables.put(updatePattern.getNew_(), updateAction.getValue());
                    if ((Boolean)definition.getCondition().evaluate(variables)) changes.add(updateAction);
                }
            }
        }
        return changes;
    }
}
