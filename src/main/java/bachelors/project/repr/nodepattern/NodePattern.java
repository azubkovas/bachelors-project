package bachelors.project.repr.nodepattern;

import bachelors.project.repr.VariableContainer;
import bachelors.project.util.JoernClient;
import com.github.gumtreediff.tree.Tree;

import java.io.IOException;

public abstract class NodePattern {

    public abstract NodeType getNodeType();

    public abstract boolean matchesNode(Tree node, VariableContainer variables);

    public String getJoernQuery(Tree node) {
        return JoernClient.getNodeQueryOfRequiredType(node, getNodeType());
    }

    public boolean checkNodeOfRequiredType(Tree node) {
        try {
            String query = getJoernQuery(node);
            String result = JoernClient.getInstance().executeQuery("""
                    %s.hasNext""".formatted(query));
            return result.equals("true");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
            return false;
        }
    }
}
