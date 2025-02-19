package bachelors.project.repr.cond;

import bachelors.project.repr.nodepattern.NodePattern;
import bachelors.project.util.DiffData;
import bachelors.project.util.JoernManager;
import com.github.gumtreediff.tree.Tree;

import java.io.IOException;
import java.util.Map;

public class RefCondition extends Condition {
    Variable referer;
    Variable referee;

    public RefCondition(Variable referer, Variable referee) {
        this.referer = referer;
        this.referee = referee;
    }

    @Override
    public Object evaluate(Map<String, Object> variables, DiffData diffData) {
        NodePattern refererPattern = (NodePattern) referer.evaluate(variables, diffData);
        NodePattern refereePattern = (NodePattern) referee.evaluate(variables, diffData);
        Tree refererNode = refererPattern.getCorrespondingNode();
        Tree refereeNode = refereePattern.getCorrespondingNode();
        assert (refererNode != null && refereeNode != null);
        String refererQuery = JoernManager.getNodeQueryOfRequiredType(refererNode, refererPattern.getNodeType());
        String refereeQuery = JoernManager.getNodeQueryOfRequiredType(refereeNode, refereePattern.getNodeType());
        String funcName = switch (refererPattern.getNodeType()) {
            case IDENTIFIER -> "refsTo";
            case CALL -> "callee";
            default -> throw new RuntimeException("Unsupported node type");
        };
        try {
            return JoernManager.getInstance().executeQuery(refererQuery + ".%s.toSet == ".formatted(funcName) + refereeQuery + ".toSet").equals("true");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
