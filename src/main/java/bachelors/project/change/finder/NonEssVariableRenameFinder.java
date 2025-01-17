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

public class NonEssVariableRenameFinder extends ChangeFinder {
    @Override
    public List<Action> findChanges(DiffData diffData) throws IOException {
        List<Action> renameCasualtyChanges = new ArrayList<>();
        EditScript actions = diffData.getEditScript();
        for (Action action : actions) {
            if (action instanceof Update update) {
                if (update.getNode().getParent().getType().name.equals("VariableDeclarationFragment") && update.getNode().getParent().getChildPosition(update.getNode()) == 0) {
                    String prevName = update.getNode().getLabel();
                    String newName = update.getValue();
                    Pair<Integer, Integer> declLocation = PositionConverter.getLineAndColumn(diffData.getBeforeFilePath(), update.getNode().getParent().getParent().getPos());
                    for (Action act : actions) {
                        if (act instanceof Update upd && upd != update && upd.getNode().getLabel().equals(prevName) &&
                                upd.getValue().equals(newName)) {
                            Pair<Integer, Integer> usageLocation = PositionConverter.getLineAndColumn(diffData.getBeforeFilePath(), upd.getNode().getPos());
                            String queryOutput = JoernCient.executeQuery(("cpg.identifier.lineNumber(%d)." +
                                    "columnNumber(%d).head.refsTo.toSet == cpg.local.lineNumber(%d).columnNumber(%d).toSet").formatted(
                                    usageLocation.first, usageLocation.second, declLocation.first, declLocation.second
                            ), diffData.getBeforeFilePath());
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
