package org.defdiff;

import org.defdiff.deflang.Definition;
import org.defdiff.deflang.changepattern.*;
import org.defdiff.deflang.cond.*;
import org.defdiff.deflang.cond.eval.Evaluatable;
import org.defdiff.deflang.cond.eval.Literal;
import org.defdiff.deflang.cond.eval.ParentEval;
import org.defdiff.deflang.cond.eval.Variable;
import org.defdiff.deflang.nodepattern.*;

public class DefinitionVisitor extends ChangeDefinitionBaseVisitor<Object> {
    @Override
    public Definition visitDefinition(ChangeDefinitionParser.DefinitionContext ctx) {
        ChangePattern changePattern = (ChangePattern) ctx.changePattern().accept(this);
        Condition condition = ctx.condition() == null ? null : (Condition) ctx.condition().accept(this);
        return new Definition(changePattern, condition);
    }

    @Override
    public InsertPattern visitInsertPattern(ChangeDefinitionParser.InsertPatternContext ctx) {
        NodePattern inserted = (NodePattern) ctx.nodePattern(0).accept(this);
        NodePattern parent = (NodePattern) ctx.nodePattern(1).accept(this);
        return new InsertPattern(inserted, parent);
    }

    @Override
    public DeletePattern visitDeletePattern(ChangeDefinitionParser.DeletePatternContext ctx) {
        NodePattern deletedPatern = (NodePattern) ctx.nodePattern().accept(this);
        return new DeletePattern(deletedPatern);
    }

    @Override
    public Object visitUpdatePattern(ChangeDefinitionParser.UpdatePatternContext ctx) {
        NodePattern target = (NodePattern) ctx.nodePattern().accept(this);
        String old = ctx.ID(0).getText();
        String new_ = ctx.ID(1).getText();
        return new UpdatePattern(target, old, new_);
    }

    @Override
    public Object visitMovePattern(ChangeDefinitionParser.MovePatternContext ctx) {
        NodePattern movedPattern = (NodePattern) ctx.nodePattern(0).accept(this);
        NodePattern oldParentPattern = (NodePattern) ctx.nodePattern(1).accept(this);
        NodePattern newParentPattern = (NodePattern) ctx.nodePattern(2).accept(this);
        return new MovePattern(movedPattern, oldParentPattern, newParentPattern);
    }

    @Override
    public BlockPattern visitBlockPattern(ChangeDefinitionParser.BlockPatternContext ctx) {
        return new BlockPattern();
    }

    @Override
    public ReturnPattern visitReturnPattern(ChangeDefinitionParser.ReturnPatternContext ctx) {
        return new ReturnPattern();
    }

    @Override
    public Object visitLiteralPattern(ChangeDefinitionParser.LiteralPatternContext ctx) {
        return new LiteralPattern();
    }

    @Override
    public Object visitMemberPattern(ChangeDefinitionParser.MemberPatternContext ctx) {
        return new MemberPattern();
    }

    @Override
    public Object visitVariablePattern(ChangeDefinitionParser.VariablePatternContext ctx) {
        String name = ctx.ID().getText();
        if (ctx.nodePattern() == null) {
            return new VariablePattern(name);
        }
        NodePattern correspondingPattern = (NodePattern) ctx.nodePattern().accept(this);
        return new VariablePattern(name, correspondingPattern);
    }

    @Override
    public Object visitMethodPattern(ChangeDefinitionParser.MethodPatternContext ctx) {
        return new MethodPattern();
    }

    @Override
    public Object visitVariableEval(ChangeDefinitionParser.VariableEvalContext ctx) {
        return new Variable(ctx.ID().getText());
    }

    @Override
    public Object visitLiteralEval(ChangeDefinitionParser.LiteralEvalContext ctx) {
        String text = ctx.getText();
//        if (text.startsWith("\"") && text.endsWith("\"")) {
//            text = text.substring(1, text.length() - 1);
//        }
        return new Literal(text);
    }

    @Override
    public Object visitParentEval(ChangeDefinitionParser.ParentEvalContext ctx) {
        Evaluatable child = (Evaluatable) ctx.evaluatable().accept(this);
        return new ParentEval(child);
    }

    @Override
    public Object visitAndCondition(ChangeDefinitionParser.AndConditionContext ctx) {
        Condition left = (Condition) ctx.condition(0).accept(this);
        Condition right = (Condition) ctx.condition(1).accept(this);
        return new AndCondition(left, right);
    }

    @Override
    public Object visitExistentialQuantification(ChangeDefinitionParser.ExistentialQuantificationContext ctx) {
        Definition definition = (Definition) ctx.definition().accept(this);
        return new ExistentialQuantification(definition);
    }

    @Override
    public Object visitOrCondition(ChangeDefinitionParser.OrConditionContext ctx) {
        Condition left = (Condition) ctx.condition(0).accept(this);
        Condition right = (Condition) ctx.condition(1).accept(this);
        return new OrCondition(left, right);
    }

    @Override
    public Object visitRefersToCondition(ChangeDefinitionParser.RefersToConditionContext ctx) {
        Variable referer = new Variable(ctx.ID(0).getText());
        Variable referee = new Variable(ctx.ID(1).getText());
        return new RefCondition(referer, referee);
    }

    @Override
    public Object visitBinaryCondition(ChangeDefinitionParser.BinaryConditionContext ctx) {
        Evaluatable left = (Evaluatable) ctx.evaluatable(0).accept(this);
        Evaluatable right = (Evaluatable) ctx.evaluatable(1).accept(this);
        Operator operator = Operator.fromString(ctx.OPERATOR().getText());
        return new BinaryCondition(left, right, operator);
    }

    @Override
    public Object visitNotCondition(ChangeDefinitionParser.NotConditionContext ctx) {
        return new NotCondition((Condition) ctx.condition().accept(this));
    }

    @Override
    public Object visitNodeTypeCondition(ChangeDefinitionParser.NodeTypeConditionContext ctx) {
        Evaluatable target = (Evaluatable) ctx.evaluatable().accept(this);
        NodePattern pattern = (NodePattern) ctx.nodePattern().accept(this);
        return new NodeTypeCondition(target, pattern);
    }

    @Override
    public Object visitCallPattern(ChangeDefinitionParser.CallPatternContext ctx) {
        return new CallPattern();
    }

    @Override
    public Object visitAssignmentPattern(ChangeDefinitionParser.AssignmentPatternContext ctx) {
        return new AssignmentPattern((NodePattern) ctx.nodePattern(0).accept(this), (NodePattern) ctx.nodePattern(1).accept(this));
    }

    @Override
    public Object visitLocalPattern(ChangeDefinitionParser.LocalPatternContext ctx) {
        return new LocalPattern();
    }

    @Override
    public Object visitFieldAccessPattern(ChangeDefinitionParser.FieldAccessPatternContext ctx) {
        return new FieldAccessPattern();
    }

    @Override
    public Object visitIdentifierPattern(ChangeDefinitionParser.IdentifierPatternContext ctx) {
        return new IdentifierPattern();
    }

    @Override
    public Object visitAnyPattern(ChangeDefinitionParser.AnyPatternContext ctx) {
        return new AnyPattern();
    }
}
