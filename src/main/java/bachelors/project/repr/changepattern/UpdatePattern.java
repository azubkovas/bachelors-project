package bachelors.project.repr.changepattern;

import bachelors.project.repr.nodepattern.Literal;
import bachelors.project.repr.nodepattern.NodePattern;
import bachelors.project.repr.nodepattern.VariablePattern;
import bachelors.project.repr.nodepattern.VariableContainer;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Update;
import com.github.gumtreediff.tree.Tree;

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
                variables.addVariable(new VariablePattern(old, new Literal(upd.getNode().getLabel())));
            }
            if (!variables.contains(new_)) {
                variables.addVariable(new VariablePattern(new_, new Literal(upd.getValue())));
            }
            return variables.get(old).getCorrespondingNodePattern() instanceof Literal lit &&
                    lit.getValue().equals(upd.getNode().getLabel()) &&
                    variables.get(new_).getCorrespondingNodePattern() instanceof Literal lit2 &&
                    lit2.getValue().equals(upd.getValue());
        }
        return false;
    }
}
