package org.defdiff.deflang.changepattern;

import org.defdiff.deflang.VariableContainer;
import org.defdiff.deflang.VariableValue;
import org.defdiff.deflang.nodepattern.LiteralPattern;
import org.defdiff.deflang.nodepattern.NodePattern;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Update;

public class UpdatePattern extends ChangePattern {
    private final NodePattern target;
    private final String old, new_;

    public UpdatePattern(NodePattern target, String old, String new_) {
        this.target = target;
        this.old = old;
        this.new_ = new_;
    }

    @Override
    public boolean matchesAction(Action action, VariableContainer variables) {
        if (action instanceof Update upd && target.matchesNode(action.getNode(), variables)) {
            if (!variables.contains(old)) {
                variables.addVariable(old, new VariableValue(new LiteralPattern(upd.getNode().getLabel())));
            }
            if (!variables.contains(new_)) {
                variables.addVariable(new_, new VariableValue(new LiteralPattern(upd.getValue())));
            }
            return variables.get(old).getCorrespondingPattern() instanceof LiteralPattern lit &&
                    lit.getValue().equals(upd.getNode().getLabel()) &&
                    variables.get(new_).getCorrespondingPattern() instanceof LiteralPattern lit2 &&
                    lit2.getValue().equals(upd.getValue());
        }
        return false;
    }
}
