package bachelors.project.util;

import com.github.gumtreediff.actions.EditScript;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.tree.Tree;

public class DiffData {
    private final Tree before, after;
    private final String beforeFilePath, afterFilePath;
    private final MappingStore mappings;
    private final EditScript editScript;
    
    public DiffData(Tree before, Tree after, String beforeFilePath, String afterFilePath, MappingStore mappings, EditScript editScript) {
        this.before = before;
        this.after = after;
        this.beforeFilePath = beforeFilePath;
        this.afterFilePath = afterFilePath;
        this.mappings = mappings;
        this.editScript = editScript;
    }
    
    public Tree getBefore() {
        return before;
    }
    
    public Tree getAfter() {
        return after;
    }

    public String getBeforeFilePath() {
        return beforeFilePath;
    }

    public String getAfterFilePath() {
        return afterFilePath;
    }
    
    public MappingStore getMappings() {
        return mappings;
    }
    
    public EditScript getEditScript() {
        return editScript;
    }
}
