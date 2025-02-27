package bachelors.project;

import bachelors.project.repr.Definition;
import bachelors.project.repr.changepattern.ChangePattern;
import bachelors.project.repr.changepattern.InsertPattern;
import bachelors.project.repr.cond.Condition;
import bachelors.project.repr.nodepattern.BlockPattern;
import bachelors.project.repr.nodepattern.NodePattern;
import bachelors.project.repr.nodepattern.ReturnPattern;

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
    public BlockPattern visitBlockPattern(ChangeDefinitionParser.BlockPatternContext ctx) {
        return new BlockPattern();
    }

    @Override
    public ReturnPattern visitReturnPattern(ChangeDefinitionParser.ReturnPatternContext ctx) {
        return new ReturnPattern();
    }
}
