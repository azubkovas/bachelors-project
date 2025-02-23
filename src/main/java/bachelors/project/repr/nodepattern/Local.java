package bachelors.project.repr.nodepattern;

import bachelors.project.repr.VariableContainer;
import bachelors.project.util.JoernClient;
import com.github.gumtreediff.tree.Tree;

public class Local extends NodePattern {
    @Override
    public NodeType getNodeType() {
        return NodeType.LOCAL;
    }

    @Override
    public boolean matchesNode(Tree node, VariableContainer variables) {
        return getNodeType().matches(node.getType().name) && JoernClient.checkNodeOfRequiredType(node, getNodeType());
    }
}
