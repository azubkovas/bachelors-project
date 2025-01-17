package bachelors.project.change.finder;

import bachelors.project.util.DiffData;
import bachelors.project.util.JoernCient;
import bachelors.project.util.PositionConverter;
import com.github.gumtreediff.actions.EditScript;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.utils.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NonEssThisAdditionToFieldFinder extends ChangeFinder{
    @Override
    public List<Action> findChanges(DiffData diffData) throws IOException {
        List<Action> trivialThisKeywordChanges = new ArrayList<>();
        EditScript actions = diffData.getEditScript();
        for (Action action : actions) {
            Optional<Action> parentInsert, siblingMove;
            if (action instanceof Insert insert && insert.getNode().getType().name.equals("ThisExpression") &&
                    insert.getParent().getType().name.equals("FieldAccess") && (parentInsert = actions.asList().stream().filter(act -> act instanceof Insert ins && ins.getNode() == insert.getParent()).findFirst()).isPresent() &&
                    (siblingMove = actions.asList().stream().filter(act -> act instanceof Move mov && mov.getParent() == insert.getParent()).findFirst()).isPresent()) {
                System.out.println("Found");
                Pair<Integer, Integer> positionInBefore = PositionConverter.getLineAndColumn(diffData.getBeforeFilePath(), siblingMove.get().getNode().getPos());
                String queryOutputBefore = JoernCient.executeQuery("cpg.fieldAccess.lineNumber(%d).columnNumber(%d).head.referencedMember.head.typeDecl.fullName"
                        .formatted(positionInBefore.first, positionInBefore.second), diffData.getBeforeFilePath());
                System.out.println(queryOutputBefore);
                Pair<Integer, Integer> positionInAfter = PositionConverter.getLineAndColumn(diffData.getAfterFilePath(), insert.getNode().getPos());
                String queryOutputAfter = JoernCient.executeQuery("cpg.fieldAccess.lineNumber(%d).columnNumber(%d).head.referencedMember.head.typeDecl.fullName"
                        .formatted(positionInAfter.first, positionInAfter.second), diffData.getAfterFilePath());
                System.out.println(queryOutputAfter);
                if (queryOutputBefore.equals(queryOutputAfter)) {
                    trivialThisKeywordChanges.addAll(List.of(insert, parentInsert.get(), siblingMove.get()));
                }
            }
        }
        return trivialThisKeywordChanges;

    }
}
