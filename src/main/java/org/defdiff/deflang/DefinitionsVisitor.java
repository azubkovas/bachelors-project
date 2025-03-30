package org.defdiff.deflang;

import org.antlr.v4.runtime.tree.Tree;
import org.defdiff.ChangeDefinitionsBaseVisitor;
import org.defdiff.ChangeDefinitionsParser.*;
import org.defdiff.deflang.changepattern.*;
import org.defdiff.deflang.cond.*;
import org.defdiff.deflang.cond.eval.Evaluatable;
import org.defdiff.deflang.cond.eval.Literal;
import org.defdiff.deflang.cond.eval.ParentEval;
import org.defdiff.deflang.cond.eval.Variable;
import org.defdiff.deflang.nodepattern.*;

import java.util.ArrayList;
import java.util.List;

public class DefinitionsVisitor extends ChangeDefinitionsBaseVisitor<Object> {
    @Override
    public SetterCondition visitSetterCondition(SetterConditionContext ctx) {
        return new SetterCondition((Evaluatable) ctx.evaluatable(0).accept(this), (Evaluatable) ctx.evaluatable(1).accept(this));
    }

    @Override
    public Object visitCompoundDefinition(CompoundDefinitionContext ctx) {
        List<SimpleDefinition> simpleDefinitions = new ArrayList<>();
        for (SimpleDefinitionContext simpleDefinitionContext : ctx.simpleDefinition()) {
            simpleDefinitions.add((SimpleDefinition) simpleDefinitionContext.accept(this));
        }
        return new CompoundDefinition(simpleDefinitions, printTree(ctx));
    }

    @Override
    public Definition visitDefinition(DefinitionContext ctx) {
        if (ctx.simpleDefinition() != null) {
            return (SimpleDefinition) ctx.simpleDefinition().accept(this);
        } else {
            return (CompoundDefinition) ctx.compoundDefinition().accept(this);
        }
    }

    @Override
    public Definitions visitDefinitions(DefinitionsContext ctx) {
        List<Definition> definitionList = new ArrayList<>();
        for (DefinitionContext definitionContext : ctx.definition()) {
            definitionList.add((Definition) definitionContext.accept(this));
        }
        return new Definitions(definitionList);
    }

    @Override
    public SimpleDefinition visitSimpleDefinition(SimpleDefinitionContext ctx) {
        ChangePattern changePattern = (ChangePattern) ctx.changePattern().accept(this);
        Condition condition = ctx.condition() == null ? null : (Condition) ctx.condition().accept(this);
        return new SimpleDefinition(changePattern, condition, printTree(ctx));
    }

    @Override
    public Object visitExprPattern(ExprPatternContext ctx) {
        return new ExprPattern();
    }

    @Override
    public InsertPattern visitInsertPattern(InsertPatternContext ctx) {
        NodePattern inserted = (NodePattern) ctx.nodePattern(0).accept(this);
        NodePattern parent = (NodePattern) ctx.nodePattern(1).accept(this);
        return new InsertPattern(inserted, parent);
    }

    @Override
    public DeletePattern visitDeletePattern(DeletePatternContext ctx) {
        NodePattern deletedPatern = (NodePattern) ctx.nodePattern().accept(this);
        return new DeletePattern(deletedPatern);
    }

    @Override
    public Object visitUpdatePattern(UpdatePatternContext ctx) {
        NodePattern target = (NodePattern) ctx.nodePattern().accept(this);
        String old = ctx.ID(0).getText();
        String new_ = ctx.ID(1).getText();
        return new UpdatePattern(target, old, new_);
    }

    @Override
    public Object visitMovePattern(MovePatternContext ctx) {
        NodePattern movedPattern = (NodePattern) ctx.nodePattern(0).accept(this);
        NodePattern oldParentPattern = (NodePattern) ctx.nodePattern(1).accept(this);
        NodePattern newParentPattern = (NodePattern) ctx.nodePattern(2).accept(this);
        return new MovePattern(movedPattern, oldParentPattern, newParentPattern);
    }

    @Override
    public BlockPattern visitBlockPattern(BlockPatternContext ctx) {
        return new BlockPattern();
    }

    @Override
    public ReturnPattern visitReturnPattern(ReturnPatternContext ctx) {
        return new ReturnPattern();
    }

    @Override
    public Object visitLiteralPattern(LiteralPatternContext ctx) {
        return new LiteralPattern();
    }

    @Override
    public Object visitMemberPattern(MemberPatternContext ctx) {
        return new MemberPattern();
    }

    @Override
    public Object visitVariablePattern(VariablePatternContext ctx) {
        String name = ctx.ID().getText();
        if (ctx.nodePattern() == null) {
            return new VariablePattern(name);
        }
        NodePattern correspondingPattern = (NodePattern) ctx.nodePattern().accept(this);
        return new VariablePattern(name, correspondingPattern);
    }

    @Override
    public Object visitMethodPattern(MethodPatternContext ctx) {
        return new MethodPattern();
    }

    @Override
    public Object visitVariableEval(VariableEvalContext ctx) {
        return new Variable(ctx.ID().getText());
    }

    @Override
    public Object visitLiteralEval(LiteralEvalContext ctx) {
        String text = ctx.getText();
//        if (text.startsWith("\"") && text.endsWith("\"")) {
//            text = text.substring(1, text.length() - 1);
//        }
        return new Literal(text);
    }

    @Override
    public Object visitParentEval(ParentEvalContext ctx) {
        Evaluatable child = (Evaluatable) ctx.evaluatable().accept(this);
        return new ParentEval(child);
    }

    @Override
    public Object visitAndCondition(AndConditionContext ctx) {
        Condition left = (Condition) ctx.condition(0).accept(this);
        Condition right = (Condition) ctx.condition(1).accept(this);
        return new AndCondition(left, right);
    }

    @Override
    public Object visitExistentialQuantification(ExistentialQuantificationContext ctx) {
        SimpleDefinition simpleDefinition = (SimpleDefinition) ctx.simpleDefinition().accept(this);
        return new ExistentialQuantification(simpleDefinition);
    }

    @Override
    public Object visitTypePattern(TypePatternContext ctx) {
        return new TypePattern();
    }

    @Override
    public Object visitParametersPattern(ParametersPatternContext ctx) {
        return new ParametersPattern();
    }

    @Override
    public Object visitOrCondition(OrConditionContext ctx) {
        Condition left = (Condition) ctx.condition(0).accept(this);
        Condition right = (Condition) ctx.condition(1).accept(this);
        return new OrCondition(left, right);
    }

    @Override
    public Object visitRefersToCondition(RefersToConditionContext ctx) {
        Variable referer = new Variable(ctx.evaluatable(0).getText());
        Variable referee = new Variable(ctx.evaluatable(1).getText());
        return new RefCondition(referer, referee);
    }

    @Override
    public Object visitBinaryCondition(BinaryConditionContext ctx) {
        Evaluatable left = (Evaluatable) ctx.evaluatable(0).accept(this);
        Evaluatable right = (Evaluatable) ctx.evaluatable(1).accept(this);
        Operator operator = Operator.fromString(ctx.OPERATOR().getText());
        return new BinaryCondition(left, right, operator);
    }

    @Override
    public Object visitNotCondition(NotConditionContext ctx) {
        return new NotCondition((Condition) ctx.condition().accept(this));
    }

    @Override
    public Object visitNodeTypeCondition(NodeTypeConditionContext ctx) {
        Evaluatable target = (Evaluatable) ctx.evaluatable().accept(this);
        NodePattern pattern = (NodePattern) ctx.nodePattern().accept(this);
        return new NodeTypeCondition(target, pattern);
    }

    @Override
    public Object visitCallPattern(CallPatternContext ctx) {
        return new CallPattern();
    }

    @Override
    public Object visitAssignmentPattern(AssignmentPatternContext ctx) {
        return new AssignmentPattern((NodePattern) ctx.nodePattern(0).accept(this), (NodePattern) ctx.nodePattern(1).accept(this));
    }

    @Override
    public Object visitLocalPattern(LocalPatternContext ctx) {
        return new LocalPattern();
    }

    @Override
    public Object visitFieldAccessPattern(FieldAccessPatternContext ctx) {
        return new FieldAccessPattern();
    }

    @Override
    public Object visitIdentifierPattern(IdentifierPatternContext ctx) {
        return new IdentifierPattern();
    }

    @Override
    public Object visitAnyPattern(AnyPatternContext ctx) {
        return new AnyPattern();
    }

    private String printTree(Tree t) {
        if (t.getChildCount() == 0) {
            return t.toString();
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < t.getChildCount(); i++) {
            sb.append(printTree(t.getChild(i))).append(" ");
        }
        return sb.toString();
    }
}
