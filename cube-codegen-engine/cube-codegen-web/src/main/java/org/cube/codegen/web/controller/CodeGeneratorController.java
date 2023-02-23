package org.cube.codegen.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.cube.codegen.core.CodeGenerator;
import org.cube.codegen.core.models.Table;
import org.cube.commons.base.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

/**
 * 新版代码生成器
 */
@Tag(name = "新版代码生成器")
@RestController
@RequestMapping("/codegen")
public class CodeGeneratorController {

    /**
     * 根据Java类生成前端代码配置
     *
     * @param className 类路径
     */
    @GetMapping("/generate")
    @Operation(summary = "根据Java类生成前端代码配置")
    public Result<Table> generate(@Parameter(description = "类路径") @RequestParam String className) throws ClassNotFoundException, SQLException {
        CodeGenerator codeGenerator = new CodeGenerator(className);
        Table table = codeGenerator.generate();
        return Result.ok(table);
    }
}
