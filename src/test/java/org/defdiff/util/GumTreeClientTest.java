package org.defdiff.util;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class GumTreeClientTest {

    @Test
    void testGetDiffData() {
        DiffData diffData = GumTreeClient.getDiffData(Path.of("src/test/data/pre_patch/SampleProject"), Path.of("src/test/data/post_patch/SampleProject"));
        assertEquals(1, diffData.getAllActions().size());
    }
}