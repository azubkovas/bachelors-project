package bachelors.project.util;

import com.github.gumtreediff.actions.model.Action;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DiffData {
    private final Map<String, SingleFileDiffData> fileDiffs;
    private final String prePatchRevisionPath, postPatchRevisionPath;

    public DiffData(String prePatchRevisionPath, String postPatchRevisionPath) {
        fileDiffs = new HashMap<>();
        this.prePatchRevisionPath = prePatchRevisionPath;
        this.postPatchRevisionPath = postPatchRevisionPath;
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

    public Set<Action> getAllActions() {
        return fileDiffs.values().stream()
                .map(SingleFileDiffData::getEditScript)
                .flatMap(editScript -> editScript.asList().stream())
                .collect(Collectors.toSet());
    }

    public String getPrePatchRevisionPath() {
        return prePatchRevisionPath;
    }

    public String getPostPatchRevisionPath() {
        return postPatchRevisionPath;
    }
}
