package org.defdiff;

import org.defdiff.deflang.Definitions;
import org.defdiff.deflang.ParserClient;
import org.defdiff.util.DiffData;
import org.defdiff.util.GumTreeClient;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        Path prePatchRevisionPath = Path.of("experimental_data/experiment3/patch_data/3e1010b1a2f648581fac3d68afbf18f2979f6bf6 (CVE-2009-2901, CVE-2009-2902, CVE-2009-2693)/pre_patch/java/org/apache/catalina/loader/WebappClassLoader.java");
        Path postPatchRevisionPath = Path.of("experimental_data/experiment3/patch_data/3e1010b1a2f648581fac3d68afbf18f2979f6bf6 (CVE-2009-2901, CVE-2009-2902, CVE-2009-2693)/post_patch/java/org/apache/catalina/loader/WebappClassLoader.java");
        Path nonEssentialChangeDefinitionsFilePath = Path.of("experimental_data/experiment3/definitions.txt");

//        Definitions definitions = ParserClient.parseDefinitions(nonEssentialChangeDefinitionsFilePath);

        DiffData diffData = GumTreeClient.getDiffData(prePatchRevisionPath, postPatchRevisionPath);
//        ChangesContainer nonEssentialChanges = ChangeFinder.findChanges(diffData, definitions);
//        System.out.println("Total number of non-essential changes: " + nonEssentialChanges.getAllChanges().size());
//        nonEssentialChanges.printChangeCounts();
//        diffData.removeNonEssentialChanges(nonEssentialChanges.getAllChanges());
        new MyWebDiff(new String[]{prePatchRevisionPath.toString(), postPatchRevisionPath.toString()}, diffData).run();
    }
}