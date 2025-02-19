package bachelors.project;

import bachelors.project.repr.Definition;
import bachelors.project.repr.changepattern.ChangePattern;
import bachelors.project.repr.changepattern.UpdatePattern;
import bachelors.project.repr.nodepattern.Call;
import bachelors.project.repr.nodepattern.Method;
import bachelors.project.repr.nodepattern.NodePattern;
import bachelors.project.util.DiffData;
import bachelors.project.util.JoernManager;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.Tree;

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
            for (Action action : allChanges) {
                Map<String, Object> variables = new HashMap<>();
                if (actionMatchesChangePattern(action, definition.getPattern(), variables)) {
                    populateVariables(variables, definition.getPattern(), action);
                    if ((Boolean) definition.getCondition().evaluate(variables, diffData)) changes.add(action);
                }
            }
        }
        return changes;
    }

    public static boolean actionMatchesChangePattern(Action action, ChangePattern changePattern, Map<String, Object> variables) {
        if (changePattern instanceof UpdatePattern updatePattern &&
                action instanceof com.github.gumtreediff.actions.model.Update updateAction
        ) {
            NodePattern targetNodePattern = updatePattern.getTarget().getNodePattern();
            Tree targetNode = updateAction.getNode();
            if (targetNodePattern instanceof Call &&
                    targetNode.getType().name.equals("name") &&
                    targetNode.getParent().getType().name.equals("call"))
                targetNode = targetNode.getParent();
            if (targetNodePattern instanceof Method &&
                    targetNode.getType().name.equals("name") &&
                    targetNode.getParent().getType().name.equals("function"))
                targetNode = targetNode.getParent();
            if (variables.containsKey(updatePattern.getOld()) && !variables.get(updatePattern.getOld()).equals(updateAction.getNode().getLabel())) {
                return false;
            }
            if (variables.containsKey(updatePattern.getNew_()) && !variables.get(updatePattern.getNew_()).equals(updateAction.getValue())) {
                return false;
            }
            return JoernManager.checkNodeOfRequiredType(targetNode, targetNodePattern.getNodeType());
        }
        return false;
    }

    public static void populateVariables(Map<String, Object> variables, ChangePattern changePattern, Action action) {
        if (action instanceof com.github.gumtreediff.actions.model.Update updateAction &&
                changePattern instanceof UpdatePattern updatePattern) {
            variables.put(updatePattern.getOld(), updateAction.getNode().getLabel());
            variables.put(updatePattern.getNew_(), updateAction.getValue());
            Tree correspondingNode = updateAction.getNode();
            if (updatePattern.getTarget().getNodePattern() instanceof Call &&
                    updateAction.getNode().getType().name.equals("name") &&
                    updateAction.getNode().getParent().getType().name.equals("call"))
                correspondingNode = updateAction.getNode().getParent();
            if (updatePattern.getTarget().getNodePattern() instanceof Method &&
                    updateAction.getNode().getType().name.equals("name") &&
                    updateAction.getNode().getParent().getType().name.equals("function"))
                correspondingNode = updateAction.getNode().getParent();
            changePattern.getTarget().getNodePattern().setCorrespondingNode(correspondingNode);
            variables.put(updatePattern.getTarget().getLabel(), updatePattern.getTarget().getNodePattern());
        }
    }
}
