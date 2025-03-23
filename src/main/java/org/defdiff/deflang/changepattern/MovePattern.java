package org.defdiff.deflang.changepattern;

import org.defdiff.deflang.VariableContainer;
import org.defdiff.deflang.nodepattern.NodePattern;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Move;

public class MovePattern extends ChangePattern {
    private final NodePattern movedPattern;
    private final NodePattern oldParentPattern;
    private final NodePattern newParentPattern;

    public MovePattern(NodePattern movedPattern, NodePattern oldParentPattern, NodePattern newParentPattern) {
        this.movedPattern = movedPattern;
        this.oldParentPattern = oldParentPattern;
        this.newParentPattern = newParentPattern;
    }


    @Override
    public boolean matchesAction(Action action, VariableContainer variables) {
        return action instanceof Move mv &&
                movedPattern.matchesNode(mv.getNode(), variables) &&
                (oldParentPattern == null || oldParentPattern.matchesNode(mv.getNode().getParent(), variables)) &&
                (newParentPattern == null || newParentPattern.matchesNode(mv.getParent(), variables));
    }
}
