package bachelors.project.repr.cond;

import bachelors.project.repr.VariableContainer;
import bachelors.project.repr.cond.eval.Evaluatable;
import bachelors.project.repr.nodepattern.NodePattern;
import bachelors.project.util.DiffData;
import com.github.gumtreediff.tree.Tree;

public class NodeTypeCondition extends Condition {
    private final Evaluatable target;
    private final NodePattern pattern;

    public NodeTypeCondition(Evaluatable target, NodePattern pattern) {
        this.target = target;
        this.pattern = pattern;
    }

    @Override
    public Object evaluate(VariableContainer variables, DiffData diffData) {
        return pattern.matchesNode((Tree) target.evaluate(variables, diffData), variables);
    }
}
