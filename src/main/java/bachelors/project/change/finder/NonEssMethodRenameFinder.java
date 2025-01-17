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
            if (action instanceof Update update) {
                if (update.getNode().getParent().getType().name.equals("MethodDeclaration")) {
                    String prevName = update.getNode().getLabel();
                    String newName = update.getValue();
                    String className = update.getNode().getParent().getParent().getChild(2).getLabel();
                    for (Action act : actions) {
                        if (act instanceof Update upd && upd != update &&
                                upd.getNode().getParent().getType().name.equals("MethodInvocation") &&
                                upd.getNode().getLabel().equals(prevName) && upd.getValue().equals(newName)) {
                            Pair<Integer, Integer> pos = PositionConverter.getLineAndColumn(diffData.getBeforeFilePath(), upd.getNode().getPos());
                            String queryOutput = JoernCient.executeQuery(("cpg.call.name(\"%s\").lineNumber(%d)." +
                                    "columnNumber(%d).head.callee.head.typeDecl.head.name == \"%s\"").formatted(prevName, pos.first, pos.second, className), diffData.getBeforeFilePath());
                            if (queryOutput.trim().equals("true")) {
                                renameCasualtyChanges.add(upd);
                            }
                        }
                    }
                }
            }
        }
        return renameCasualtyChanges;

    }
}
