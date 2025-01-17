package bachelors.project.change.finder;

import bachelors.project.util.DiffData;
import com.github.gumtreediff.actions.model.Action;

import java.io.IOException;
import java.util.List;

public abstract class ChangeFinder {
    public abstract List<Action> findChanges(DiffData diffData) throws IOException;
}
