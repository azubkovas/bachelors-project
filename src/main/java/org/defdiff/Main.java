package org.defdiff;

import org.defdiff.deflang.Definitions;
import org.defdiff.deflang.ParserClient;
import org.defdiff.util.DiffData;
import org.defdiff.util.GumTreeClient;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        Path prePatchRevisionPath = Path.of("experimental_data/experiment3/patch_data/CVE-2011-1088/ece65c1a428094b1c6c17de3d7593f64e1bb1286/pre_patch");
        Path postPatchRevisionPath = Path.of("experimental_data/experiment3/patch_data/CVE-2011-1088/ece65c1a428094b1c6c17de3d7593f64e1bb1286/post_patch");
        Path nonEssentialChangeDefinitionsFilePath = Path.of("experimental_data/experiment3/definitions.txt");

        Definitions definitions = ParserClient.parseDefinitions(nonEssentialChangeDefinitionsFilePath);

        DiffData diffData = GumTreeClient.getDiffData(prePatchRevisionPath, postPatchRevisionPath);
        ChangesContainer nonEssentialChanges = ChangeFinder.findChanges(diffData, definitions);
        System.out.println("Total number of non-essential changes: " + nonEssentialChanges.getAllChanges().size());
        nonEssentialChanges.printChangeCounts();
        System.out.println();
        nonEssentialChanges.printChanges();
        diffData.removeNonEssentialChanges(nonEssentialChanges.getAllChanges());
        new MyWebDiff(new String[]{prePatchRevisionPath.toString(), postPatchRevisionPath.toString()}, diffData).run();
    }
}