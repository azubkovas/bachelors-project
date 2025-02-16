package bachelors.project.change.finder;

import bachelors.project.util.DiffData;
import bachelors.project.util.JoernCient;
import bachelors.project.util.PositionConverter;
import com.github.gumtreediff.actions.EditScript;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Update;
import com.github.gumtreediff.utils.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NonEssMethodRenameFinder extends ChangeFinder{
    @Override
    public List<Action> findChanges(DiffData diffData) throws IOException {
        List<Action> renameCasualtyChanges = new ArrayList<>();
        EditScript actions = diffData.getEditScript();
        for (Action action : actions) {
            if (action instanceof Update update && update.getNode().getParent().getType().name.equals("function")) {
                String prevName = update.getNode().getLabel();
                String newName = update.getValue();
//                Pair<Integer, Integer> declPosInFile = PositionConverter.getLineAndColumn(diffData.getBeforeFilePath(), update.getNode().getParent().getPos());
                String methodDecl = JoernCient.findMethod(update.getNode().getParent(), diffData.getBeforeFilePath(), prevName);
//                String methodFullName = JoernCient.executeQuery("cpg.method.name(\"%s\").lineNumber(%d).head.fullName".formatted(prevName, declPosInFile.first), diffData.getBeforeFilePath()).trim();
                for (Action act : actions) {
                    if (act instanceof Update upd && upd != update &&
                            upd.getNode().getParent().getType().name.equals("call") &&
                            upd.getNode().getLabel().equals(prevName) && upd.getValue().equals(newName)) {
//                        Pair<Integer, Integer> usagePosInFile = PositionConverter.getLineAndColumn(diffData.getBeforeFilePath(), upd.getNode().getParent().getPos());
//                        String calledMethodFullName = JoernCient.executeQuery(("cpg.call.name(\"%s\").lineNumber(%d)." +
//                                "columnNumber(%d).head.methodFullName").formatted(prevName, usagePosInFile.first, usagePosInFile.second), diffData.getBeforeFilePath()).trim();
                        String call = JoernCient.findCall(upd.getNode().getParent(), diffData.getBeforeFilePath(), prevName);
                        String queryOutput = JoernCient.executeQuery("%s.callee.toSet == %s.toSet".formatted(call, methodDecl), diffData.getBeforeFilePath());
                        if (queryOutput.trim().equals("true")) {
                            renameCasualtyChanges.add(upd);
                        }
                    }
                }
            }
        }
        return renameCasualtyChanges;

    }
}
