package bachelors.project.util;

import com.github.gumtreediff.tree.Tree;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JoernCientTest {

    @Test
    void testFindLocal() throws IOException {
        String filePath = "src/test/data/rename_casualties/java/BeforeVariableRename.java";
        Tree tree = GumTreeClient.getTree(filePath);
        Tree declNode = GumTreeClient.findFirst(tree, t -> t.getType().name.equals("decl") && t.getChild(1).getLabel().equals("x"));
        assertNotNull(declNode);
        String local = JoernCient.findLocal(declNode, filePath, "x");
        String queryOutput = JoernCient.executeQuery("%s.size".formatted(local), filePath);
        assertEquals("1", queryOutput.trim());
    }
}