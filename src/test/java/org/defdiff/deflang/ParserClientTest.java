package org.defdiff.deflang;

import org.junit.jupiter.api.Test;

class ParserClientTest {

    @Test
    void testParseDefinitions() {
        Definitions simpleDefinition = ParserClient.parseDefinitions("INSERT LITERAL l INTO BLOCK | l == \"1\"");
        System.out.println(simpleDefinition);
    }
}