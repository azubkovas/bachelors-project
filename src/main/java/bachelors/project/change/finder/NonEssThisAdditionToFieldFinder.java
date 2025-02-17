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

public class NonEssThisAdditionToFieldFinder extends ChangeFinder {
//    @Override
//    public List<Action> findChanges(DiffData diffData) throws IOException {
//        List<Action> trivialThisKeywordChanges = new ArrayList<>();
//        EditScript actions = diffData.getEditScript();
//        for (Action action : actions) {
//            if (action instanceof Insert insert && insert.getNode().getType().name.equals("name") &&
//                    insert.getNode().getLabel().equals("this") && insert.getParent().getType().name.equals("name") &&
//                    actions.asList().stream().anyMatch(act -> act instanceof Insert ins && ins.getNode() == insert.getParent()) &&
//                    actions.asList().stream().anyMatch(act -> act instanceof Move mov && mov.getParent() == insert.getParent()) &&
//                    actions.asList().stream().anyMatch(act -> act instanceof Insert ins && ins.getParent() == insert.getParent())) {
//                Action parentInsert = actions.asList().stream().filter(act -> act instanceof Insert ins && ins.getNode() == insert.getParent()).findFirst().get();
//                Action siblingMove = actions.asList().stream().filter(act -> act instanceof Move mov && mov.getParent() == insert.getParent()).findFirst().get();
//                Action siblingInsert = actions.asList().stream().filter(act -> act instanceof Insert ins && ins.getParent() == insert.getParent()).findFirst().get();
//                Pair<Integer, Integer> positionInBefore = PositionConverter.getLineAndColumn(diffData.getBeforeFilePath(), siblingMove.getNode().getPos());
//                String queryOutputBefore = JoernCient.executeQuery("cpg.fieldAccess.lineNumber(%d).columnNumber(%d).head.referencedMember.head.typeDecl.fullName"
//                        .formatted(positionInBefore.first, positionInBefore.second), diffData.getBeforeFilePath());
//                System.out.println(queryOutputBefore);
//                Pair<Integer, Integer> positionInAfter = PositionConverter.getLineAndColumn(diffData.getAfterFilePath(), insert.getNode().getPos());
//                String queryOutputAfter = JoernCient.executeQuery("cpg.fieldAccess.lineNumber(%d).columnNumber(%d).head.referencedMember.head.typeDecl.fullName"
//                        .formatted(positionInAfter.first, positionInAfter.second), diffData.getAfterFilePath());
//                System.out.println(queryOutputAfter);
//                if (queryOutputBefore.equals(queryOutputAfter)) {
//                    trivialThisKeywordChanges.addAll(List.of(insert, parentInsert, siblingMove, siblingInsert));
//                }
//            }
//        }
//        return trivialThisKeywordChanges;
//
//    }
}
