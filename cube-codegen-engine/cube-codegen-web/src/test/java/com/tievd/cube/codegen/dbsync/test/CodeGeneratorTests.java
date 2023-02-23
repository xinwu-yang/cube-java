package com.tievd.cube.codegen.dbsync.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cube.codegen.core.CodeGenerator;
import org.cube.codegen.core.models.Table;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class CodeGeneratorTests {

    @Test
    public void testGenerate() throws ClassNotFoundException, JsonProcessingException, SQLException {
        CodeGenerator codeGenerator = new CodeGenerator("com.tievd.cube.codegen.dbsync.test.entity.Tester");
        Table table = codeGenerator.generate();
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(table));
    }
}
