package bachelors.project;

import bachelors.project.repr.Definition;
import bachelors.project.repr.ParserClient;
import bachelors.project.util.DiffData;
import bachelors.project.util.GumTreeClient;
import com.github.gumtreediff.actions.model.Action;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {
        Path prePatchRevisionPath = Path.of("experimental_data/experiment1/patch_data/CVE-2013-4322/d6a9898125f34e593de426e8c7dabb0f224fc00f/pre_patch");
        Path postPatchRevisionPath = Path.of("experimental_data/experiment1/patch_data/CVE-2013-4322/d6a9898125f34e593de426e8c7dabb0f224fc00f/post_patch");
        Path nonEssentialChangeDefinitionsFilePath = Path.of("experimental_data/experiment1/definitions_for_exp.txt");

        List<Definition> definitions = ParserClient.parseDefinitions(nonEssentialChangeDefinitionsFilePath);

        DiffData diffData = GumTreeClient.getDiffData(prePatchRevisionPath, postPatchRevisionPath);
        Set<Action> nonEssentialChanges = ChangeFinder.findChanges(diffData, definitions);
        System.out.println("Number of non-essential changes: " + nonEssentialChanges.size());
        nonEssentialChanges.forEach(System.out::println);
//        diffData.removeNonEssentialChanges(nonEssentialChanges);
//        new MyWebDiff(new String[]{prePatchRevisionPath.toString(), postPatchRevisionPath.toString()}, diffData).run();
    }
}