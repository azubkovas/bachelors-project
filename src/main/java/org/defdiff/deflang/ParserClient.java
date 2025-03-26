package org.defdiff.deflang;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.defdiff.ChangeDefinitionsLexer;
import org.defdiff.ChangeDefinitionsParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ParserClient {
    public static Definitions parseDefinitions(Path definitionsFilePath) {
        try {
            String definitionsStr = Files.readString(definitionsFilePath);
            return parseDefinitions(definitionsStr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Definitions parseDefinitions(String definitionsStr) {
        ChangeDefinitionsLexer lexer = new ChangeDefinitionsLexer(CharStreams.fromString(definitionsStr));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ChangeDefinitionsParser parser = new ChangeDefinitionsParser(tokens);
        ParseTree tree = parser.definitions();
        DefinitionsVisitor visitor = new DefinitionsVisitor();
        return (Definitions) visitor.visit(tree);
    }


}
