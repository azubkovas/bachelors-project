package bachelors.project.change.finder;

import bachelors.project.util.DiffData;
import bachelors.project.util.GumTreeClient;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Update;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NonEssMethodRenameFinderTest {

    @Test
    void testFindChangesWithJavaFile() throws IOException {
        NonEssMethodRenameFinder finder = new NonEssMethodRenameFinder();
        DiffData diffData = GumTreeClient.getDiffData("src/test/data/rename_casualties/java/BeforeMethodRename.java", "src/test/data/rename_casualties/java/AfterMethodRename.java");

        List<Action> res = finder.findChanges(diffData);

        assertEquals(2, res.size());
        assertTrue(res.stream().anyMatch(action -> action instanceof Update upd && upd.getNode().getLabel().equals("getX") && upd.getNode().getPos() == 157 && upd.getValue().equals("readX")));
        assertTrue(res.stream().anyMatch(action -> action instanceof Update upd && upd.getNode().getLabel().equals("getY") && upd.getNode().getPos() == 166 && upd.getValue().equals("readY")));
    }

    @Test
    void testFindChangesWithCppFile() throws IOException {
        NonEssMethodRenameFinder finder = new NonEssMethodRenameFinder();
        DiffData diffData = GumTreeClient.getDiffData("src/test/data/rename_casualties/c++/BeforeMethodRename.cpp", "src/test/data/rename_casualties/c++/AfterMethodRename.cpp");

        List<Action> res = finder.findChanges(diffData);

        assertEquals(2, res.size());
        assertTrue(res.stream().anyMatch(action -> action instanceof Update upd && upd.getNode().getLabel().equals("getX") && upd.getNode().getPos() == 223 && upd.getValue().equals("readX")));
        assertTrue(res.stream().anyMatch(action -> action instanceof Update upd && upd.getNode().getLabel().equals("getY") && upd.getNode().getPos() == 232 && upd.getValue().equals("readY")));
    }
}