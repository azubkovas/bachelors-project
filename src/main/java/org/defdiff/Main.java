package org.defdiff;

import org.defdiff.deflang.Definitions;
import org.defdiff.deflang.ParserClient;
import org.defdiff.util.DiffData;
import org.defdiff.util.GumTreeClient;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        Path prePatchRevisionPath = Path.of("experimental_data/experiment3/patch_data/e28dd578fad90a6d5726ec34f3245c9f99d909a5 (CVE-2014-0230)/pre_patch");
        Path postPatchRevisionPath = Path.of("experimental_data/experiment3/patch_data/e28dd578fad90a6d5726ec34f3245c9f99d909a5 (CVE-2014-0230)/post_patch");
        Path nonEssentialChangeDefinitionsFilePath = Path.of("experimental_data/experiment3/definitions.txt");

        Definitions definitions = ParserClient.parseDefinitions(nonEssentialChangeDefinitionsFilePath);

        DiffData diffData = GumTreeClient.getDiffData(prePatchRevisionPath, postPatchRevisionPath);
        ChangesContainer nonEssentialChanges = ChangeFinder.findChanges(diffData, definitions);
        System.out.println("Total number of non-essential changes: " + nonEssentialChanges.getAllChanges().size());
        nonEssentialChanges.printChangeCounts();
        diffData.removeNonEssentialChanges(nonEssentialChanges.getAllChanges());
        new MyWebDiff(new String[]{prePatchRevisionPath.toString(), postPatchRevisionPath.toString()}, diffData).run();
    }
}