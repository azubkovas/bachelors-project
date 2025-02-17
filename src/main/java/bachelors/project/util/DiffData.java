package bachelors.project.util;

import com.github.gumtreediff.actions.model.Action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiffData {
    private final Map<String, SingleFileDiffData> fileDiffs;

    public DiffData() {
        fileDiffs = new HashMap<>();
    }

    public SingleFileDiffData getSingleFileDiffData(String filePath) {
        return fileDiffs.get(filePath);
    }

    public void addSingleFileDiffData(SingleFileDiffData singleFileDiffData) {
        if (singleFileDiffData == null) {
            return;
        }
        fileDiffs.put(singleFileDiffData.getFilePathPair().first, singleFileDiffData);
    }

    public List<Action> getAllActions() {
        return fileDiffs.values().stream()
                .map(SingleFileDiffData::getEditScript)
                .flatMap(editScript -> editScript.asList().stream())
                .toList();
    }
}
