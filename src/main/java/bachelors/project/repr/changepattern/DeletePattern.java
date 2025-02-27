package bachelors.project.repr.changepattern;

import bachelors.project.repr.VariableContainer;
import bachelors.project.repr.nodepattern.NodePattern;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;

public class DeletePattern extends ChangePattern {
    private final NodePattern targetPattern;

    public DeletePattern(NodePattern targetPattern) {
        this.targetPattern = targetPattern;
    }

    @Override
    public boolean matchesAction(Action action, VariableContainer variables) {
        if (action instanceof Delete del && targetPattern.matchesNode(action.getNode(), variables)) {
            return true;
        }
        return false;
    }
}
