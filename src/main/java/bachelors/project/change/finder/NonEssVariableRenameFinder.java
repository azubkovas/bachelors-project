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
//    @Override
//    public List<Action> findChanges(DiffData diffData) throws IOException {
////        List<Action> renameCasualtyChanges = new ArrayList<>();
////        EditScript actions = diffData.getEditScript();
////        for (Action action : actions) {
////            if (action instanceof Update update && update.getNode().getType().name.equals("name") && update.getNode().getParent().getType().name.equals("decl")) {
////                String prevName = update.getNode().getLabel();
////                String newName = update.getValue();
////                String declNode = JoernCient.findLocal(update.getNode().getParent(), diffData.getBeforeFilePath(), prevName);
////                for (Action act : actions) {
////                    if (act instanceof Update upd && upd != update && upd.getNode().getLabel().equals(prevName) &&
////                            upd.getValue().equals(newName)) {
////                        String usageNode = JoernCient.findIdentifier(upd.getNode(), diffData.getBeforeFilePath(), prevName);
////                        String queryOutput = JoernCient.executeQuery(("%s.refsTo.toSet == %s.toSet").formatted(
////                                usageNode, declNode
////                        ), diffData.getBeforeFilePath());
////                        if (queryOutput.trim().equals("true")) {
////                            renameCasualtyChanges.add(upd);
////                        }
////                    }
////                }
////            }
////        }
////        return renameCasualtyChanges;
//    }
}
