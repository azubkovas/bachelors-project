package org.defdiff.deflang.changepattern;

import org.defdiff.deflang.VariableContainer;
import org.defdiff.deflang.nodepattern.NodePattern;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.TreeDelete;

public class DeletePattern extends ChangePattern {
    private final NodePattern targetPattern;

    public DeletePattern(NodePattern targetPattern) {
        this.targetPattern = targetPattern;
    }

    @Override
    public boolean matchesAction(Action action, VariableContainer variables) {
        return (action instanceof Delete && targetPattern.matchesNode(action.getNode(), variables)) ||
                (action instanceof TreeDelete && targetPattern.matchesNode(action.getNode(), variables));
    }
}
