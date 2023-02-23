package org.cube.modules.system.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import org.cube.commons.base.CubeController;
import org.cube.commons.mybatisplus.QueryGenerator;
import org.cube.commons.utils.FillRuleUtil;
import org.cube.commons.annotations.AutoLog;
import org.cube.commons.base.Result;
import org.cube.modules.system.entity.SysFillRule;
import org.cube.modules.system.service.ISysFillRuleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * 填值规则
 *
 * @author 杨欣武
 * @version V1.0.0
 * @since 2019-11-07
 */
@Slf4j
@Tag(name = "填值规则相关接口")
@RestController
@RequestMapping("/sys/fill/rule")
public class SysFillRuleController extends CubeController<SysFillRule, ISysFillRuleService> {

    /**
     * 分页列表查询
     *
     * @param sysFillRule 查询条件
     * @param pageNo      页码
     * @param pageSize    每页数量
     */
    @GetMapping("/list")
    public Result<IPage<SysFillRule>> queryPageList(SysFillRule sysFillRule, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        QueryWrapper<SysFillRule> queryWrapper = QueryGenerator.initQueryWrapper(sysFillRule, req.getParameterMap());
        Page<SysFillRule> page = new Page<>(pageNo, pageSize);
        IPage<SysFillRule> pageList = service.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 测试填值规则
     *
     * @param ruleCode 规则编码
     */
    @GetMapping("/testFillRule")
    public Result<?> testFillRule(@RequestParam String ruleCode, @RequestParam(required = false) String formData) {
        Object result = FillRuleUtil.executeRule(ruleCode, formData);
        return Result.ok(result);
    }

    /**
     * 新增
     *
     * @param sysFillRule 新增参数
     */
    @AutoLog("填值规则-添加")
    @PostMapping("/add")
    public Result<?> add(@RequestBody SysFillRule sysFillRule) {
        service.save(sysFillRule);
        return Result.ok();
    }

    /**
     * 编辑
     *
     * @param sysFillRule 编辑参数
     */
    @AutoLog("填值规则-编辑")
    @PutMapping("/edit")
    public Result<?> edit(@RequestBody SysFillRule sysFillRule) {
        service.updateById(sysFillRule);
        return Result.ok();
    }

    /**
     * 通过id删除
     *
     * @param id 主键id
     */
    @AutoLog("填值规则-通过id删除")
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
    @AutoLog("填值规则-批量删除")
    @DeleteMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestParam String ids) {
        this.service.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok();
    }

    /**
     * 导出excel
     */
    @GetMapping("/exportXls")
    public void exportXls(HttpServletRequest request, HttpServletResponse response, SysFillRule sysFillRule) throws IOException {
        super.exportXls(request, response, sysFillRule, "填值规则数据");
    }

    /**
     * 通过excel导入数据
     */
    @PostMapping("/importExcel")
    public Result<?> importExcel(HttpServletRequest request) throws Exception {
        return super.importExcel(request, SysFillRule.class);
    }

    /**
     * 通过 ruleCode 执行自定义填值规则
     *
     * @param ruleCode 要执行的填值规则编码
     * @param formData 表单数据，可根据表单数据的不同生成不同的填值结果
     * @return 运行后的结果
     */
    @Deprecated
    @PutMapping("/executeRuleByCode/{ruleCode}")
    public Result<?> executeByRuleCode(@PathVariable("ruleCode") String ruleCode, @RequestBody JsonNode formData) {
        Object result = FillRuleUtil.executeRule(ruleCode, formData.asText());
        return Result.ok(result);
    }

    /**
     * 批量通过ruleCode执行自定义填值规则
     *
     * @param ruleData 要执行的填值规则JSON数组
     * @return 运行后的结果，返回示例： [{"ruleCode": "order_num_rule", "result": "CN2019111117212984"}]
     * @apiNote 参数示例： { "commonFormData": {}, rules: [ { "ruleCode": "xxx", "formData": null } ] }
     */
    @Deprecated
    @PutMapping("/executeRuleByCodeBatch")
    public Result<?> executeByRuleCodeBatch(@RequestBody JsonNode ruleData) {
        JsonNode commonFormData = ruleData.get("commonFormData");
        JsonNode rules = ruleData.get("rules");
        // 遍历 rules ，批量执行规则
        JSONArray results = JSONUtil.createArray();
        //JSONArray results = new JSONArray(rules.size());
        for (int i = 0; i < rules.size(); i++) {
            JsonNode rule = rules.get(i);
            String ruleCode = rule.get("ruleCode").asText();
            JsonNode formData = rule.get("formData");
            // 如果没有传递 formData，就用common的
            if (formData == null) {
                formData = commonFormData;
            }
            // 执行填值规则
            Object result = FillRuleUtil.executeRule(ruleCode, formData.asText());
            JSONObject resultJson = JSONUtil.createObj();
            resultJson.set("ruleCode", ruleCode);
            resultJson.set("result", result);
            results.add(resultJson);
        }
        return Result.ok(results);
    }
}