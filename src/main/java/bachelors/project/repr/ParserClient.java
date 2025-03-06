package bachelors.project.repr;

import bachelors.project.ChangeDefinitionLexer;
import bachelors.project.ChangeDefinitionParser;
import bachelors.project.DefinitionVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ParserClient {
    public static List<Definition> parseDefinitions(Path definitionsFilePath) {
        List<Definition> definitions = new ArrayList<Definition>();
        try (BufferedReader reader = new BufferedReader(new FileReader(definitionsFilePath.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                definitions.add(parseDefinition(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return definitions;
    }

    protected static Definition parseDefinition(String definitionStr) {
        ChangeDefinitionLexer lexer = new ChangeDefinitionLexer(CharStreams.fromString(definitionStr));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ChangeDefinitionParser parser = new ChangeDefinitionParser(tokens);
        ParseTree tree = parser.definition();
        DefinitionVisitor visitor = new DefinitionVisitor();
        Definition result = (Definition) visitor.visit(tree);
        return result;
    }
}
