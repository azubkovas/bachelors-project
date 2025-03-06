package bachelors.project.repr;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserClientTest {

    @Test
    void testParseDefinitions() {
        Definition definition = ParserClient.parseDefinition("INSERT LITERAL l INTO BLOCK | l == \"1\"");
        System.out.println(definition);
    }
}