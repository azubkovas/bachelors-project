package bachelors.project.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GumTreeClientTest {

    @Test
    void testGetDiffData() {
        DiffData diffData = GumTreeClient.getDiffData("/Users/arsenijuszubkovas/Documents/UoE/Year_4/Final Project/playground/prepatch_sample/SampleProject", "/Users/arsenijuszubkovas/Documents/UoE/Year_4/Final Project/playground/postpatchSample/SampleProject");
        assertEquals(1, diffData.getAllActions().size());
    }
}