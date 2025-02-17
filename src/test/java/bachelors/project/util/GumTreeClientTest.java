package bachelors.project.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GumTreeClientTest {

    @Test
    void testGetDiffData() {
        DiffData diffData = GumTreeClient.getDiffData("src/test/data/pre_patch/SampleProject", "src/test/data/post_patch/SampleProject");
        assertEquals(1, diffData.getAllActions().size());
    }
}