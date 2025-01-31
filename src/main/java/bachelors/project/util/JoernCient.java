package bachelors.project.util;

import com.github.gumtreediff.tree.Tree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

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

    // Returns query string that can be used to obtain the equivalent node in Joern todo: deal with possible multiple outputs
    public static String findDeclNode(Tree node, String filePath) {
        //cpg.declaration.name("x").where(_.method.name("Main").typeDecl.name("Program").filename("BeforeVariableRename.cs")).head
        if (node.getType().name.equals("decl")) {
            Optional<String> functionName = GumTreeClient.getFunctionName(node);
            Optional<String> className = GumTreeClient.getClassName(node);
            String name = node.getChildren().stream().filter(x -> x.getType().name.equals("name")).findFirst().get().getLabel();
            return "cpg.declaration.name(\"%s\").where(_.method.name(\"%s\").typeDecl.name(\"%s\").filename(\"%s\"))".formatted(
                    name,
                    functionName.get(),
                    className.orElse("<global>"),
                    filePath
            );
        } else throw new IllegalArgumentException("Node type should be 'decl'");
    }

    public static String findIdentifierNode(Tree node, String filePath) {
        //cpg.identifier.name("x").where(_.method.name("Main").typeDecl.name("Program").filename("BeforeVariableRename.cs")).head
        if (node.getType().name.equals("name")) {
            Optional<String> functionName = GumTreeClient.getFunctionName(node);
            Optional<String> className = GumTreeClient.getClassName(node);
            String name = node.getLabel();
            return "cpg.identifier.name(\"%s\").where(_.method.name(\"%s\").typeDecl.name(\"%s\").filename(\"%s\"))".formatted(
                    name,
                    functionName.get(),
                    className.orElse("<global>"),
                    filePath
            );
        } else throw new IllegalArgumentException("Node type should be 'name'");
    }

}