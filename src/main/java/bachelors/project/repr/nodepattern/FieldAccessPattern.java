package bachelors.project.repr.nodepattern;

import bachelors.project.repr.VariableContainer;
import bachelors.project.util.JoernClient;
import com.github.gumtreediff.tree.Tree;

import java.io.IOException;

import static bachelors.project.util.JoernClient.getConditionBasedOnParents;

public class FieldAccessPattern extends NodePattern {
    @Override
    public NodeType getNodeType() {
        return NodeType.FIELD_ACCESS;
    }

    @Override
    public String getJoernQuery(Tree node) {
        Tree nodeToCheck = node;
        if (node.getParent().getType().name.equals("name")) nodeToCheck = node.getParent();
        StringBuilder fieldIdentifier = new StringBuilder();
        if (!nodeToCheck.getLabel().isEmpty()) fieldIdentifier.append(nodeToCheck.getLabel());
        else if (!nodeToCheck.getChildren().isEmpty()) {
            for (Tree child : nodeToCheck.getChildren()) {
                fieldIdentifier.append(child.getLabel());            }
        }
        try {
            JoernClient.setProjectToNodePath(nodeToCheck);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "cpg.fieldAccess.where(%s).where(_.fieldIdentifier.canonicalName(\"%s\"))".formatted(getConditionBasedOnParents(node), fieldIdentifier.toString());
    }

    @Override
    public boolean matchesNode(Tree node, VariableContainer variables) {
        if (!node.getType().name.equals("name")) {
            return false;
        }
        return checkNodeOfRequiredType(node);
    }
}
