package bachelors.project.util;

import com.github.gumtreediff.actions.EditScript;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.utils.Pair;

public class SingleFileDiffData {
    private final Pair<Tree, Tree> treePair;
    private final Pair<String, String> filePathPair;
    private final MappingStore mappings;
    private final EditScript editScript;

    public SingleFileDiffData(Tree before, Tree after, String beforeFilePath, String afterFilePath, MappingStore mappings, EditScript editScript) {
        treePair = new Pair<>(before, after);
        filePathPair = new Pair<>(beforeFilePath, afterFilePath);
        this.mappings = mappings;
        this.editScript = editScript;
    }

    public Pair<Tree, Tree> getTreePair() {
        return treePair;
    }

    public Pair<String, String> getFilePathPair() {
        return filePathPair;
    }

    public MappingStore getMappings() {
        return mappings;
    }

    public EditScript getEditScript() {
        return editScript;
    }
}
