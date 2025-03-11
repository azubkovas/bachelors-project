package bachelors.project.repr.nodepattern;

import bachelors.project.repr.VariableContainer;
import com.github.gumtreediff.tree.Tree;

public class AssignmentPattern extends NodePattern {
    private final NodePattern left;
    private final NodePattern right;

    public AssignmentPattern(NodePattern left, NodePattern right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.ASSIGNMENT;
    }

    @Override
    public boolean matchesNode(Tree node, VariableContainer variables) {
        if (!node.getType().name.equals("expr") || node.getChildren().size() < 3) {
            return false;
        }
        Tree firstChild = node.getChild(0);
        Tree secondChild = node.getChild(1);
        Tree thirdChild = node.getChild(2);
        return left.matchesNode(firstChild, variables) &&
                secondChild.getType().name.equals("operator") &&
                secondChild.getLabel().equals("=") &&
                right.matchesNode(thirdChild, variables);
    }
}
