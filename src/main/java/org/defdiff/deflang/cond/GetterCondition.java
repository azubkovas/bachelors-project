package org.defdiff.deflang.cond;

import com.github.gumtreediff.tree.Tree;
import org.defdiff.deflang.VariableContainer;
import org.defdiff.deflang.VariableValue;
import org.defdiff.deflang.cond.eval.Evaluatable;
import org.defdiff.deflang.nodepattern.NodeType;
import org.defdiff.util.DiffData;
import org.defdiff.util.JoernClient;

public class GetterCondition extends Condition {
    private final Evaluatable method, field;

    public GetterCondition(Evaluatable method, Evaluatable field) {
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
                    method.parameter.indexGt(0).isEmpty &&
                    method.call.size == 1 &&
                    method.callee.fullName.toSet == Set("<operator>.fieldAccess") &&
                    method.ast.isReturn.fieldAccess.referencedMember.id.toSet == field.toSet.id.toSet
                    }
                    """.formatted(methodQuery, fieldQuery);
            return JoernClient.getInstance().executeQuery(query).equals("true");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
