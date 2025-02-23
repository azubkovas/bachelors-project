package bachelors.project.repr.nodepattern;

import bachelors.project.repr.VariableContainer;
import com.github.gumtreediff.tree.Tree;

public class BlockPattern extends NodePattern{
    @Override
    public NodeType getNodeType() {
        return NodeType.BLOCK;
    }

    @Override
    public boolean matchesNode(Tree node, VariableContainer variables) {
        return getNodeType().matches(node.getType().name);
    }
}
