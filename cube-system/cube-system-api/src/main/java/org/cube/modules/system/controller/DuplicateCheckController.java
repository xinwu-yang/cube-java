package org.cube.modules.system.controller;

import org.cube.commons.base.Result;
import org.cube.commons.utils.SqlInjectionUtil;
import org.cube.modules.system.model.api.request.DuplicateCheckRequest;
import org.cube.modules.system.service.ISysDictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 重复数据校验
 *
 * @author 杨欣武
 * @version 2.5.2
 * @since 2022-08-11
 */
@Tag(name = "重复数据校验")
@RestController
@RequestMapping("/sys/duplicate")
public class DuplicateCheckController {

    @Autowired
    private ISysDictService sysDictService;

    /**
     * 校验数据是否已在系统
     */
    @GetMapping("/check")
    @Operation(summary = "校验数据是否已在系统")
    @Parameters({
            @Parameter(name = "tableName", description = "表名", required = true),
            @Parameter(name = "fieldName", description = "字段名", required = true),
            @Parameter(name = "fieldVal", description = "字段值", required = true),
            @Parameter(name = "dataId", description = "要排除的数据id")
    })
    public Result<?> doDuplicateCheck(@Parameter(hidden = true) @Validated DuplicateCheckRequest duplicateCheckRequest) {
        final String[] sqlInjCheck = {duplicateCheckRequest.getTableName(), duplicateCheckRequest.getFieldName()};
        SqlInjectionUtil.filterContent(sqlInjCheck);
        Long num = sysDictService.duplicateCheckCountSql(duplicateCheckRequest);
        if (num != null && num > 0) {
            return Result.error("该值已使用！");
        }
        return Result.ok("该值可用！");
    }
}
