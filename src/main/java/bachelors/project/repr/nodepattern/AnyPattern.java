package bachelors.project.repr.nodepattern;

import bachelors.project.repr.VariableContainer;
import com.github.gumtreediff.tree.Tree;

public class AnyPattern extends NodePattern {
    @Override
    public NodeType getNodeType() {
        return null;
    }

    @Override
    public boolean matchesNode(Tree node, VariableContainer variables) {
        return true;
    }
}
