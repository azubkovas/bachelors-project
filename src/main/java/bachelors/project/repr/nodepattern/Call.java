package bachelors.project.repr.nodepattern;

import com.github.gumtreediff.tree.Tree;

public class Call extends NodePattern {
    @Override
    public NodeType getNodeType() {
        return NodeType.CALL;
    }

    @Override
    public boolean matchesNode(Tree node, VariableContainer variables) {
        return getNodeType().matches(node.getType().name);
    }
}
