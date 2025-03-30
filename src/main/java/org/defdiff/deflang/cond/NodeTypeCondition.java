package org.defdiff.deflang.cond;

import org.defdiff.deflang.VariableContainer;
import org.defdiff.deflang.cond.eval.Evaluatable;
import org.defdiff.deflang.nodepattern.NodePattern;
import org.defdiff.util.DiffData;
import com.github.gumtreediff.tree.Tree;

public class NodeTypeCondition extends Condition {
    private final Evaluatable target;
    private final NodePattern pattern;

    public NodeTypeCondition(Evaluatable target, NodePattern pattern) {
        this.target = target;
        this.pattern = pattern;
    }

    @Override
    public Boolean evaluate(VariableContainer variables, DiffData diffData) {
        return pattern.matchesNode((Tree) target.evaluate(variables, diffData), variables);
    }
}
