package bachelors.project.change.finder;

import bachelors.project.util.DiffData;
import bachelors.project.util.GumTreeClient;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NonEssThisAdditionToFieldFinderTest {

    @Test
    void testFindChanges() throws IOException {
//        NonEssThisAdditionToFieldFinder finder = new NonEssThisAdditionToFieldFinder();
//        DiffData diffData = GumTreeClient.getDiffData("src/test/data/trivial_keywords/WithoutThisKeyword.java", "src/test/data/trivial_keywords/WithThisKeyword.java");
//
//        List<Action> res = finder.findChanges(diffData);
//
//        assertEquals(4, res.size());
//        assertTrue(res.stream().anyMatch(action -> action instanceof Insert ins && ins.getNode().getLabel().equals("this") && ins.getNode().getPos() == 71));
    }
}