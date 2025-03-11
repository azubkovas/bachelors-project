package bachelors.project.repr.changepattern;

import bachelors.project.repr.VariableContainer;
import bachelors.project.repr.nodepattern.NodePattern;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.TreeInsert;

public class InsertPattern extends ChangePattern {
    private final NodePattern insertedPattern, parentPattern;

    public InsertPattern(NodePattern insertedPattern, NodePattern parentPattern) {
        this.insertedPattern = insertedPattern;
        this.parentPattern = parentPattern;
    }

    @Override
    public boolean matchesAction(Action action, VariableContainer variables) {
        return (
                action instanceof Insert ins && insertedPattern.matchesNode(action.getNode(), variables) &&
                        parentPattern.matchesNode(ins.getParent(), variables)
        ) ||
                (action instanceof TreeInsert insTree && insertedPattern.matchesNode(action.getNode(), variables) &&
                        parentPattern.matchesNode(insTree.getParent(), variables));
    }
}
