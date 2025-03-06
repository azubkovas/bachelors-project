package bachelors.project.repr.nodepattern;

import bachelors.project.repr.VariableContainer;
import com.github.gumtreediff.tree.Tree;

public class CallPattern extends NodePattern {
    @Override
    public NodeType getNodeType() {
        return NodeType.CALL;
    }

    @Override
    public boolean matchesNode(Tree node, VariableContainer variables) {
        return getNodeType().matches(node.getType().name);
    }
}
