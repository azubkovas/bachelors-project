package bachelors.project.repr.nodepattern;

import bachelors.project.repr.VariableContainer;
import bachelors.project.util.JoernClient;
import com.github.gumtreediff.tree.Tree;

public class Identifier extends NodePattern {
    private NodePattern referee;

    @Override
    public NodeType getNodeType() {
        return NodeType.IDENTIFIER;
    }

    @Override
    public boolean matchesNode(Tree node, VariableContainer variables) {
        return getNodeType().matches(node.getType().name) && JoernClient.checkNodeOfRequiredType(node, getNodeType());
    }
}
