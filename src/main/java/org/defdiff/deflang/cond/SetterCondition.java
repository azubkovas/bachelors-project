package org.defdiff.deflang.cond;

import com.github.gumtreediff.tree.Tree;
import org.defdiff.deflang.VariableContainer;
import org.defdiff.deflang.VariableValue;
import org.defdiff.deflang.cond.eval.Evaluatable;
import org.defdiff.deflang.nodepattern.NodeType;
import org.defdiff.util.DiffData;
import org.defdiff.util.JoernClient;

public class SetterCondition extends Condition {
    private final Evaluatable method, field;

    public SetterCondition(Evaluatable method, Evaluatable field) {
        this.method = method;
        this.field = field;
    }

    @Override
    public Boolean evaluate(VariableContainer variables, DiffData diffData) {
        try {
            Tree methodNode = ((VariableValue) method.evaluate(variables, diffData)).getCorrespondingNode();
            Tree fieldNode = ((VariableValue) field.evaluate(variables, diffData)).getCorrespondingNode();
            String methodQuery = JoernClient.getNodeQueryOfRequiredType(methodNode, NodeType.METHOD);
            String fieldQuery = JoernClient.getNodeQueryOfRequiredType(fieldNode, NodeType.MEMBER);
            String query = """
                    {
                    val method = %s.l
                    val field = %s.l
                    val assignment = method.iterator.assignment.l
                    val fieldAccess = method.iterator.fieldAccess.l
                    val parameter = method.parameter.index(1).l
                    method.call.size == 2 &&
                    method.callee.fullName.toSet == Set("<operator>.assignment", "<operator>.fieldAccess") &&
                    method.parameter.indexGt(0).size == 1 &&
                    assignment.astChildren.order(2).isIdentifier.refsTo.l == parameter &&
                    assignment.astChildren.order(1).fieldAccess.refOut.l == field
                    }
                    """.formatted(methodQuery, fieldQuery);
            return JoernClient.getInstance().executeQuery(query).equals("true");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
