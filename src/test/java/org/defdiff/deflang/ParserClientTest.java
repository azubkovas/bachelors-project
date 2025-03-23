package org.defdiff.deflang;

import org.junit.jupiter.api.Test;

class ParserClientTest {

    @Test
    void testParseDefinitions() {
        Definition definition = ParserClient.parseDefinition("INSERT LITERAL l INTO BLOCK | l == \"1\"");
        System.out.println(definition);
    }
}