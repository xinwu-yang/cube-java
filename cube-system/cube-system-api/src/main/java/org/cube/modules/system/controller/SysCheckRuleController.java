package org.cube.modules.system.controller;

import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cube.commons.annotations.AutoLog;
import org.cube.commons.base.Result;
import org.cube.modules.system.entity.SysCheckRule;
import org.cube.modules.system.service.ISysCheckRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.cube.commons.base.CubeController;
import org.cube.commons.mybatisplus.QueryGenerator;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * 编码校验规则
 *
 * @author xinwuy
 * @version V1.0.0
 * @since 2020-02-04
 */
@Slf4j
@Tag(name = "编码校验规则相关接口")
@RestController
@RequestMapping("/sys/rule")
public class SysCheckRuleController extends CubeController<SysCheckRule, ISysCheckRuleService> {

    /**
     * 分页列表查询
     */
    @GetMapping("/list")
    @Operation(summary = "分页列表查询")
    @Parameters({
            @Parameter(name = "ruleName", description = "规则名称"),
            @Parameter(name = "ruleCode", description = "规则编码")
    })
    public Result<IPage<SysCheckRule>> queryPageList(@Parameter(hidden = true) SysCheckRule sysCheckRule,
                                                     @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNo,
                                                     @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
                                                     HttpServletRequest request) {
        QueryWrapper<SysCheckRule> queryWrapper = QueryGenerator.initQueryWrapper(sysCheckRule, request.getParameterMap());
        Page<SysCheckRule> page = new Page<>(pageNo, pageSize);
        IPage<SysCheckRule> pageList = service.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 通过Code校验传入的值
     *
     * @param ruleCode 规则编码
     * @param value    值
     */
    @GetMapping("/checkByCode")
    public Result<?> checkByCode(@Parameter(description = "规则编码") @RequestParam String ruleCode,
                                 @Parameter(description = "值") @RequestParam String value) {
        SysCheckRule sysCheckRule = service.getByCode(ruleCode);
        if (sysCheckRule == null) {
            return Result.error("该编码不存在！");
        }
        JSONObject errorResult = service.checkValue(sysCheckRule, URLUtil.decode(value));
        if (errorResult != null) {
            return Result.error(errorResult.getStr("message"));
        }
        return Result.ok();
    }

    /**
     * 添加
     *
     * @param sysCheckRule 参数
     */
    @AutoLog("编码校验规则-新增")
    @PostMapping("/add")
    @Operation(summary = "新增")
    public Result<?> add(@RequestBody SysCheckRule sysCheckRule) {
        service.save(sysCheckRule);
        return Result.ok();
    }

    /**
     * 编辑
     *
     * @param sysCheckRule 参数
     */
    @AutoLog("编码校验规则-编辑")
    @PutMapping("/edit")
    public Result<?> edit(@RequestBody SysCheckRule sysCheckRule) {
        service.updateById(sysCheckRule);
        return Result.ok();
    }

    /**
     * 通过id删除
     *
     * @param id 主键id
     */
    @AutoLog("编码校验规则-通过id删除")
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam String id) {
        service.removeById(id);
        return Result.ok();
    }

    /**
     * 批量删除
     *
     * @param ids 主键id（多个逗号分隔）
     */
    @AutoLog("编码校验规则-批量删除")
    @DeleteMapping("/deleteBatch")
    public Result<?> deleteBatch(@Parameter(description = "id数组，逗号分割") @RequestParam String ids) {
        this.service.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok();
    }

    /**
     * 导出excel
     */
    @GetMapping("/exportXls")
    public void exportXls(HttpServletRequest request, HttpServletResponse response, @Parameter(hidden = true) SysCheckRule sysCheckRule) throws IOException {
        super.exportXls(request, response, sysCheckRule, "编码校验规则");
    }

    /**
     * 通过excel导入数据
     */
    @PostMapping("/importExcel")
    public Result<?> importExcel(HttpServletRequest request) throws Exception {
        return super.importExcel(request, SysCheckRule.class);
    }
}
