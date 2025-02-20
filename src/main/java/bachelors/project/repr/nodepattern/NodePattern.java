package bachelors.project.repr.nodepattern;

import com.github.gumtreediff.tree.Tree;

public abstract class NodePattern {

    public abstract NodeType getNodeType();

    public abstract boolean matchesNode(Tree node, VariableContainer variables);
}
