package org.defdiff.deflang.nodepattern;

import org.defdiff.deflang.VariableContainer;
import org.defdiff.util.JoernClient;
import com.github.gumtreediff.tree.Tree;

import java.io.IOException;

import static org.defdiff.util.JoernClient.getConditionBasedOnParents;

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
        boolean simpleName = true;
        if (!nodeToCheck.getLabel().isEmpty()) fieldIdentifier.append(nodeToCheck.getLabel());
        else if (!nodeToCheck.getChildren().isEmpty()) {
            simpleName = false;
            for (Tree child : nodeToCheck.getChildren()) {
                fieldIdentifier.append(child.getLabel());            }
        }
        try {
            JoernClient.setProjectToNodePath(nodeToCheck);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return simpleName ? "cpg.fieldAccess.where(%s).where(_.fieldIdentifier.canonicalName(\"%s\"))"
                .formatted(getConditionBasedOnParents(node), fieldIdentifier.toString()) :
                "cpg.fieldAccess.where(%s).code(\"%s\")".formatted(getConditionBasedOnParents(node), fieldIdentifier.toString());
    }

    @Override
    public boolean matchesNode(Tree node, VariableContainer variables) {
        if (!node.getType().name.equals("name")) {
            return false;
        }
        return checkNodeOfRequiredType(node);
    }
}
