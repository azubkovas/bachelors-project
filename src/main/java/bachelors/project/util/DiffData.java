package bachelors.project.util;

import com.github.gumtreediff.actions.Diff;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.io.DirectoryComparator;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DiffData {
    private final DirectoryComparator comparator;
    private final Map<Path, Diff> fileDiffs;

    public DiffData(Path prePatchRevisionPath, Path postPatchRevisionPath) {
        fileDiffs = new HashMap<>();
        comparator = new DirectoryComparator(prePatchRevisionPath.toString(), postPatchRevisionPath.toString());
        comparator.compare();
    }

    public Diff getFileDiff(Path prePatchFilePath) {
        return fileDiffs.get(prePatchFilePath);
    }

    public void addFileDiff(Path prePatchFilePath, Diff diff) {
        fileDiffs.put(prePatchFilePath, diff);
    }

    public Set<Action> getAllActions() {
        return fileDiffs.values().stream()
                .map(x -> x.editScript)
                .flatMap(editScript -> editScript.asList().stream())
                .collect(Collectors.toSet());
    }

    public DirectoryComparator getComparator() {
        return comparator;
    }

    public void removeNonEssentialChanges(Set<Action> nonEssentialChanges) {
        for (Diff diff : fileDiffs.values()) {
            diff.editScript.asList().removeAll(nonEssentialChanges);
        }
    }

    public void removeEssentialChanges(Set<Action> nonEssentialChanges) {
        Set<Action> essentialChanges = getAllActions();
        essentialChanges.removeAll(nonEssentialChanges);
        for (Diff diff : fileDiffs.values()) {
            diff.editScript.asList().removeAll(essentialChanges);
        }
    }
}
