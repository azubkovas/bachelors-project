package bachelors.project.repr.cond;

import bachelors.project.repr.changepattern.ChangePattern;
import bachelors.project.repr.changepattern.UpdatePattern;
import bachelors.project.util.DiffData;
import bachelors.project.util.JoernManager;
import com.github.gumtreediff.actions.model.Action;

import java.util.HashMap;
import java.util.Map;

public class ExistentialQuantification extends Condition {
    private final ChangePattern pattern;
    private final Condition condition;

    public ExistentialQuantification(ChangePattern pattern, Condition condition) {
        this.pattern = pattern;
        this.condition = condition;
    }

    @Override
    public Object evaluate(Map<String, Object> variables, DiffData diffData) {
        for (Action action : diffData.getAllActions()) {
            if (pattern instanceof UpdatePattern updatePattern
                    && action instanceof com.github.gumtreediff.actions.model.Update updateAction
                    && JoernManager.checkNodeOfRequiredType(updateAction.getNode(), updatePattern.getTarget().getNodePattern().getNodeType())) {
                Map<String, Object> innerVariables = new HashMap<>(variables);
                if (!variables.containsKey(updatePattern.getOld())) {
                    innerVariables.put(updatePattern.getOld(), updateAction.getNode().getLabel());
                } else {
                    if (!innerVariables.get(updatePattern.getOld()).equals(updateAction.getNode().getLabel())) {
                        continue;
                    }
                }
                if (!variables.containsKey(updatePattern.getNew_())) {
                    innerVariables.put(updatePattern.getNew_(), updateAction.getValue());
                } else {
                    if (!innerVariables.get(updatePattern.getNew_()).equals(updateAction.getValue())) {
                        continue;
                    }
                }
                updatePattern.getTarget().getNodePattern().setCorrespondingNode(updateAction.getNode());
                innerVariables.put(updatePattern.getTarget().getLabel(), updatePattern.getTarget().getNodePattern());
                if ((Boolean) condition.evaluate(innerVariables, diffData)) return true;
            }
        }
        return false;
    }
}
