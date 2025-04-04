package org.defdiff.deflang.changepattern;

import org.defdiff.deflang.VariableContainer;
import org.defdiff.deflang.VariableValue;
import org.defdiff.deflang.nodepattern.LiteralPattern;
import org.defdiff.deflang.nodepattern.NodePattern;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Move;

public class MovePattern extends ChangePattern {
    private final NodePattern movedPattern;
    private final NodePattern oldParentPattern;
    private final NodePattern newParentPattern;
    private final String at;

    public MovePattern(NodePattern movedPattern, NodePattern oldParentPattern, NodePattern newParentPattern, String at) {
        this.movedPattern = movedPattern;
        this.oldParentPattern = oldParentPattern;
        this.newParentPattern = newParentPattern;
        this.at = at;
    }


    @Override
    public boolean matchesAction(Action action, VariableContainer variables) {
        if (action instanceof Move mv) {
            if (at != null && !at.isEmpty()) {
                if (isNumber(at)) {
                    if (mv.getPosition() != Integer.parseInt(at)) {
                        return false;
                    }
                } else {
                    if (variables.contains(at)) {
                        if (!(variables.get(at).getCorrespondingPattern() instanceof LiteralPattern))
                            throw new RuntimeException();
                        if (!((LiteralPattern) variables.get(at).getCorrespondingPattern()).getValue().equals(String.valueOf(mv.getPosition()))) {
                            return false;
                        }
                    } else {
                        variables.addVariable(at, new VariableValue(new LiteralPattern(String.valueOf(mv.getPosition()))));
                    }
                }
            }
            return movedPattern.matchesNode(mv.getNode(), variables) &&
                    (oldParentPattern == null || oldParentPattern.matchesNode(mv.getNode().getParent(), variables)) &&
                    (newParentPattern == null || newParentPattern.matchesNode(mv.getParent(), variables));
        } else return false;
    }

    private boolean isNumber (String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
