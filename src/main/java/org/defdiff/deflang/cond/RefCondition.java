package org.defdiff.deflang.cond;

import org.defdiff.deflang.VariableContainer;
import org.defdiff.deflang.VariableValue;
import org.defdiff.deflang.cond.eval.Variable;
import org.defdiff.deflang.nodepattern.NodeType;
import org.defdiff.util.DiffData;
import org.defdiff.util.GumTreeClient;
import org.defdiff.util.JoernClient;

import java.io.IOException;

public class RefCondition extends Condition {
    Variable referer;
    Variable referee;

    public RefCondition(Variable referer, Variable referee) {
        this.referer = referer;
        this.referee = referee;
    }

    @Override
    public Boolean evaluate(VariableContainer variables, DiffData diffData) {
        VariableValue refererVal = (VariableValue) referer.evaluate(variables, diffData);
        VariableValue refereeVal = (VariableValue) referee.evaluate(variables, diffData);
        assert (refererVal != null && refereeVal != null);
        String refererQuery = refererVal.getCorrespondingPattern().getJoernQuery(refererVal.getCorrespondingNode());
        String refereeQuery = refereeVal.getCorrespondingPattern().getJoernQuery(refereeVal.getCorrespondingNode());
        String query = switch (refererVal.getCorrespondingPattern().getNodeType()) {
            case IDENTIFIER -> refererQuery + ".%s.toSet == ".formatted("refsTo") + refereeQuery + ".toSet";
            case CALL -> {
                String superClass = GumTreeClient.getSuperClass(GumTreeClient.getFirstParentOfType(refererVal.getCorrespondingNode(), "class"));
                if (superClass == null) {
                    superClass = "";
                }
                yield """
                        {
                            val call = %s.l;
                            val method = %s.l;
                            val potentialSuperType = "%s";
                            def typesMatch(t1: List[String], t2: List[String]): Boolean = t1.zip(t2).map((x, y) => x == "null" || y == "null" || x == y).reduce(_ && _)
                            def signaturesMatch(call: List[io.shiftleft.codepropertygraph.generated.nodes.Call], method: List[io.shiftleft.codepropertygraph.generated.nodes.Method]): Boolean = {
                                if (call.argument.size == method.parameter.size) return typesMatch(call.argument.typ.typeDeclFullName.l, method.parameter.typ.typeDeclFullName.l);
                                else if (call.argument.size == method.parameter.size + 1 && call.argument(0).typ.typeDeclFullName.toSet == method.typeDecl.fullName.toSet) return typesMatch(call.argument.typ.typeDeclFullName.l.tail, method.parameter.typ.typeDeclFullName.l);
                                else return false;
                            }
                            def checkRef(call: List[io.shiftleft.codepropertygraph.generated.nodes.Call], method: List[io.shiftleft.codepropertygraph.generated.nodes.Method]): Boolean = {
                                if (call.methodFullName.toSet == method.fullName.toSet)
                                    return true;
                                else if (
                                    call.signature.map(_.startsWith("<unresolvedSignature>")).toSet == Set(true) &&
                                    call.methodFullName.map(_.split(":")(0)).l == method.fullName.map(_.split(":")(0)).l &&
                                    signaturesMatch(call, method)
                                ) return true;
                                return (
                                    (call.method.typeDecl.inheritsFromTypeFullName.toSet == method.typeDecl.fullName.toSet || method.typeDecl.name.toSet == Set(potentialSuperType)) &&
                                    call.name.toSet.size == 1 &&
                                    method.nameExact(call.name.head).nonEmpty &&
                                    call.head.typeFullName == method.head.methodReturn.typeFullName &&
                                    call.head.argument.typ.l.tail == method.head.parameter.typ.l.tail
                                )
                            }
                            if (call.isEmpty || method.isEmpty) false else checkRef(call, method)
                        }
                        """.formatted(refererQuery, refereeQuery, superClass);
            }
            case FIELD_ACCESS -> refererQuery + ".%s.toSet == ".formatted("referencedMember") + refereeQuery + ".toSet";
            default -> throw new RuntimeException("Unsupported node type");
        };
        try {
            return JoernClient.getInstance().executeQuery(query).equals("true");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
