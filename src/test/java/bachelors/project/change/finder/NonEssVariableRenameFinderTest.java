package bachelors.project.change.finder;

import bachelors.project.util.DiffData;
import bachelors.project.util.GumTreeClient;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Update;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

public class NonEssVariableRenameFinderTest {
    @Test
    public void testFindChangesWithJavaFile() throws IOException {
        NonEssVariableRenameFinder finder = new NonEssVariableRenameFinder();
        DiffData diffData = GumTreeClient.getDiffData("src/test/data/rename_casualties/BeforeVariableRename.java", "src/test/data/rename_casualties/AfterVariableRename.java");

        List<Action> res = finder.findChanges(diffData);

        assertEquals(2, res.size());
        assertTrue(res.stream().anyMatch(action -> action instanceof Update upd && upd.getNode().getLabel().equals("y") && upd.getNode().getPos() == 122 && upd.getValue().equals("y1")));
        assertTrue(res.stream().anyMatch(action -> action instanceof Update upd && upd.getNode().getLabel().equals("y") && upd.getNode().getPos() == 126 && upd.getValue().equals("y1")));
    }

    @Test
    public void testFindChangesWithCppFile() throws IOException {
        NonEssVariableRenameFinder finder = new NonEssVariableRenameFinder();
        DiffData diffData = GumTreeClient.getDiffData("src/test/data/rename_casualties/BeforeVariableRename.cpp", "src/test/data/rename_casualties/AfterVariableRename.cpp");

        List<Action> res = finder.findChanges(diffData);

        assertEquals(2, res.size());
        assertTrue(res.stream().anyMatch(action -> action instanceof Update upd && upd.getNode().getLabel().equals("y") && upd.getNode().getPos() == 76 && upd.getValue().equals("y1")));
        assertTrue(res.stream().anyMatch(action -> action instanceof Update upd && upd.getNode().getLabel().equals("y") && upd.getNode().getPos() == 80 && upd.getValue().equals("y1")));
    }
}
