package bachelors.project.util;

import com.github.gumtreediff.tree.Tree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JoernCient {
    public static String executeQuery(String query, String filePath) throws IOException {
        Files.deleteIfExists(Path.of("tmp/script.cpgql"));
        Files.deleteIfExists(Path.of("tmp/output.log"));
        String script = """
                import java.nio.file.{Files, Path, StandardOpenOption}
                import replpp.Operators.#>
                given replpp.Colors = replpp.Colors.BlackWhite
                
                @main def exec() = {
                   if (openForInputPath("%s").isEmpty) {
                      importCode("%s")
                   }
                   (%s) #> "tmp/output.log"
                   close
                }
                """.formatted(filePath, filePath, query);
        Files.writeString(Path.of("tmp/script.cpgql"), script, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("joern", "--script", "tmp/script.cpgql");
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
            int exitCode = process.waitFor();
//            System.out.println("Process finished with exit code " + exitCode);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        String queryOutput = Files.readString(Path.of("tmp/output.log"));
        return queryOutput;
    }

    // Returns query string that can be used to obtain the equivalent node in Joern todo: deal with possible multiple outputs, add support for multiple files
    public static String findLocal(Tree node, String relativeFilePath, String name) {
        String condition = getParentCondition(node);
        return "cpg.local.name(\"%s\").where(%s)".formatted(name, condition);
    }

    public static String findIdentifier(Tree node, String relativeFilePath, String name) {
        String condition = getParentCondition(node);
        return "cpg.identifier.name(\"%s\").where(%s)".formatted(name, condition);
    }

    public static String findMethod(Tree node, String relativeFilePath, String name) {
        String condition = getParentCondition(node);
        return "cpg.method.name(\"%s\").where(%s)".formatted(name, condition);
    }

    public static String findCall(Tree node, String relativeFilePath, String name) {
        String condition = getParentCondition(node);
        return "cpg.call.name(\"%s\").where(%s)".formatted(name, condition);
    }

    private static String getParentCondition(Tree node) {
        List<String> steps = new ArrayList<>();
        List<Tree> parents = node.getParents();
        for (int i = 0; i < parents.size(); i++) {
            Tree parent = parents.get(i);
            String name;
            switch (parent.getType().name) {
                case "block":
                    steps.add(".astParent.isBlock");
                    break;
                case "function":
                    name = GumTreeClient.getFirstChildOfType(parent, "name").getLabel();
                    steps.add(".astParent.isMethod.name(\"%s\")".formatted(name));
                    break;
                case "class":
                    assert steps.get(steps.size() - 1).equals(".astParent.isBlock");
                    steps.remove(steps.size() - 1);
                    name = GumTreeClient.getFirstChildOfType(parent, "name").getLabel();
                    steps.add(".astParent.isTypeDecl.name(\"%s\")".formatted(name));
                    break;
                case "call":
                    name = GumTreeClient.getFirstChildOfType(parent, "name").getLabel();
                    steps.add(".astParent.isCall.name(\"%s\")".formatted(name));
                    break;
                case "if":
                    steps.add(".astParent.isControlStructure.controlStructureType(\"IF\")");
                    break;
                case "else":
                    steps.add(".astParent.isControlStructure.controlStructureType(\"ELSE\")");
                    break;
                case "while":
                    steps.add(".astParent.isControlStructure.controlStructureType(\"WHILE\")");
                    break;
                case "for":
                    steps.add(".astParent.isControlStructure.controlStructureType(\"FOR\")");
                    break;
                case "expr":
                    Tree operator = GumTreeClient.getFirstChildOfType(parent, "operator");
                    if (operator != null && Set.of("+", "-", "*", "/", "%", "//").contains(operator.getLabel())) {
                        int pos = parent.getChildPosition((i > 0) ? parents.get(i - 1) : node);
                        assert pos != -1;
                        steps.add(".order(%d).astParent.isCall".formatted((pos == 0) ? 1 : 2));
                    }
                    break;
                case "return":
                    steps.add(".astParent.isReturn");
                    break;
                case "init":
                    steps.add(".astParent.isCall");
                    break;
            }
        }
        if (steps.isEmpty()) {
            return "x => x";
        }
        return steps.stream().reduce("_", (a, b) -> a + b);
    }

}