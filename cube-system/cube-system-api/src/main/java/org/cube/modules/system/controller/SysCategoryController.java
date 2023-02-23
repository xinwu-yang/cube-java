package org.cube.modules.system.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cube.commons.base.CubeController;
import org.cube.commons.mybatisplus.QueryGenerator;
import org.cube.commons.base.Result;
import org.cube.modules.system.service.ISysCategoryService;
import org.cube.plugin.easyexcel.model.ImportExcel;
import org.cube.commons.utils.SystemContextUtil;
import org.cube.commons.utils.web.HttpServletUtil;
import org.cube.modules.system.entity.SysCategory;
import org.cube.modules.system.model.DictModel;
import org.cube.modules.system.model.TreeSelectModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 分类字典
 *
 * @author xinwuy
 * @version V1.0.0
 * @since 2019-05-29
 */
@Slf4j
@Tag(name = "分类字典相关接口")
@RestController
@RequestMapping("/sys/category")
public class SysCategoryController extends CubeController<SysCategory, ISysCategoryService> {

    /**
     * 指定pid分页列表查询
     *
     * @param pid      父级id
     * @param pageNo   页码
     * @param pageSize 每页大小
     */
    @GetMapping("/rootList")
    @Operation(summary = "指定pid分页列表查询")
    public Result<IPage<SysCategory>> rootListByPid(@Parameter(description = "父级id") @RequestParam(defaultValue = "0") Long pid,
                                                    @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNo,
                                                    @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize) {
        QueryWrapper<SysCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pid", pid);
        Page<SysCategory> page = new Page<>(pageNo, pageSize);
        IPage<SysCategory> pageList = service.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 查询所有子分类
     *
     * @param sysCategory 查询参数
     * @param req         请求
     * @return 分类列表
     */
    @GetMapping("/childList")
    @Operation(summary = "查询所有子分类")
    @Parameters({
            @Parameter(name = "pid", description = "父级id"),
            @Parameter(name = "name", description = "类型名称"),
            @Parameter(name = "code", description = "编码"),
            @Parameter(name = "column", description = "排序字段"),
            @Parameter(name = "order", description = "排序方式")
    })
    public Result<List<SysCategory>> childList(@Parameter(hidden = true) SysCategory sysCategory, HttpServletRequest req) {
        QueryWrapper<SysCategory> queryWrapper = QueryGenerator.initQueryWrapper(sysCategory, req.getParameterMap());
        List<SysCategory> list = service.list(queryWrapper);
        return Result.ok(list);
    }

    /**
     * 新增
     */
    @PostMapping("/add")
    @Operation(summary = "新增")
    public Result<?> add(@RequestBody SysCategory sysCategory) {
        service.addSysCategory(sysCategory);
        return Result.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/edit")
    @Operation(summary = "修改")
    public Result<?> edit(@RequestBody SysCategory sysCategory) {
        SysCategory sysCategoryEntity = service.getById(sysCategory.getId());
        if (sysCategoryEntity == null) {
            return Result.error("未找到对应数据！");
        }
        service.updateSysCategory(sysCategory);
        return Result.ok();
    }

    /**
     * 删除
     *
     * @param id 主键
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除")
    public Result<?> delete(@RequestParam String id) {
        SysCategory sysCategory = service.getById(id);
        if (sysCategory == null) {
            return Result.error("未找到对应数据！");
        }
        service.removeById(id);
        return Result.ok();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/deleteBatch")
    @Operation(summary = "批量删除")
    public Result<?> deleteBatch(@Parameter(description = "id数组，逗号分割") @RequestParam String ids) {
        if (StrUtil.isEmpty(ids)) {
            return Result.error("参数不识别！");
        }
        service.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok();
    }

    /**
     * 导出Excel
     *
     * @param sysCategory 查询参数
     * @param request     请求参数
     * @param response    接口响应
     * @download
     */
    @GetMapping("/exportXls")
    @Operation(summary = "导出Excel")
    @Parameters({
            @Parameter(name = "pid", description = "父级id"),
            @Parameter(name = "name", description = "类型名称"),
            @Parameter(name = "code", description = "编码"),
            @Parameter(name = "column", description = "排序字段"),
            @Parameter(name = "order", description = "排序方式")
    })
    public void exportXls(@Parameter(hidden = true) SysCategory sysCategory, HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<SysCategory> exportList;
        // Step.1 组装查询条件查询数据
        QueryWrapper<SysCategory> queryWrapper = QueryGenerator.initQueryWrapper(sysCategory, request.getParameterMap());
        List<SysCategory> pageList = service.list(queryWrapper);
        // 过滤选中数据
        String selections = request.getParameter("selections");
        if (StrUtil.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            exportList = pageList.stream().filter(item -> selectionList.contains(String.valueOf(item.getId()))).collect(Collectors.toList());
        } else {
            exportList = pageList;
        }
        //导出文件名称
        String date = DateUtil.format(new Date(), "yyyyMMddHHmmss");
        HttpServletUtil.addDownloadHeader(response, "分类字典-" + date + easyExcel.getExtension());
        easyExcel.export(exportList, response.getOutputStream(), SystemContextUtil.dictTranslator());
    }

    /**
     * Excel导入
     */
    @PostMapping("/importExcel")
    @Operation(summary = "Excel导入")
    public Result<?> importExcel(HttpServletRequest request) throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            try (InputStream inputStream = file.getInputStream()) {
                ImportExcel excel = new ImportExcel();
                excel.setInputStream(inputStream);
                List<SysCategory> sysCategories = easyExcel.read(SysCategory.class, excel, SystemContextUtil.dictTranslator());
                //按照编码长度排序
                Collections.sort(sysCategories);
                for (SysCategory sysCategoryExcel : sysCategories) {
                    String code = sysCategoryExcel.getCode();
                    if (code.length() > 3) {
                        String pCode = sysCategoryExcel.getCode().substring(0, code.length() - 3);
                        Long pId = service.queryIdByCode(pCode);
                        if (pId != null) {
                            sysCategoryExcel.setPid(pId);
                        }
                    } else {
                        sysCategoryExcel.setPid(ISysCategoryService.ROOT_PID_VALUE);
                    }
                    service.save(sysCategoryExcel);
                }
                return Result.ok("文件导入成功！数据行数：" + sysCategories.size());
            }
        }
        return Result.error("文件导入失败！");
    }

    /**
     * 加载单个数据
     *
     * @param field 字段
     * @param val   字段值
     * @return 分类数据
     */
    @GetMapping("/loadOne")
    @Operation(summary = "加载单个数据")
    public Result<?> loadOne(@Parameter(description = "字段") @RequestParam String field,
                             @Parameter(description = "字段值") @RequestParam String val) {
        QueryWrapper<SysCategory> query = new QueryWrapper<>();
        query.eq(field, val);
        List<SysCategory> sysCategories = service.list(query);
        if (sysCategories == null || sysCategories.size() == 0) {
            return Result.error("查询无果！");
        } else if (sysCategories.size() > 1) {
            return Result.error("查询数据异常，[" + field + "]存在多个值:" + val + "。");
        }
        return Result.ok(sysCategories.get(0));
    }

    /**
     * 加载子节点数据
     */
    @GetMapping("/loadTreeChildren")
    @Operation(summary = "加载子节点数据")
    public Result<List<TreeSelectModel>> loadTreeChildren(@Parameter(description = "父id") @RequestParam Long pid) {
        List<TreeSelectModel> list = service.queryListByPid(pid);
        return Result.ok(list);
    }

    /**
     * 根据编码加载节点
     */
    @GetMapping("/loadTreeRoot")
    @Operation(summary = "根据编码加载节点", description = "同步则加载该节点下所有数据")
    public Result<List<TreeSelectModel>> loadTreeRoot(@Parameter(description = "是否异步") @RequestParam Boolean async,
                                                      @Parameter(description = "父级编码") @RequestParam String pcode) {
        List<TreeSelectModel> treeSelectModels = service.queryListByCode(pcode);
        if (!async) {
            loadAllCategoryChildren(treeSelectModels);
        }
        return Result.ok(treeSelectModels);
    }

    /**
     * 递归求子节点 同步加载用到
     */
    private void loadAllCategoryChildren(List<TreeSelectModel> ls) {
        for (TreeSelectModel tsm : ls) {
            List<TreeSelectModel> temp = service.queryListByPid(Convert.toLong(tsm.getKey()));
            if (temp != null && temp.size() > 0) {
                tsm.setChildren(temp);
                loadAllCategoryChildren(temp);
            }
        }
    }

    /**
     * 校验编码
     *
     * @param pid  父id
     * @param code 编码
     * @return 是否符合规范
     */
    @GetMapping("/checkCode")
    @Operation(summary = "校验编码")
    public Result<?> checkCode(@Parameter(description = "父id") @RequestParam(required = false) Long pid,
                               @Parameter(description = "编码") @RequestParam(required = false) String code) {
        if (StrUtil.isEmpty(code)) {
            return Result.error("父id或编码为空！");
        }
        if (pid == null) {
            return Result.ok();
        }
        SysCategory parent = service.getById(pid);
        if (!code.startsWith(parent.getCode())) {
            return Result.error("编码不符合规范，须以【" + parent.getCode() + "】开头！");
        }
        return Result.ok();
    }

    /**
     * 分类字典树控件专用接口
     *
     * @param pid       父id
     * @param pcode     父code
     * @param condition 条件
     * @apiNote 用于加载节点
     */
    @GetMapping("/loadTreeData")
    @Operation(summary = "分类字典树控件专用接口")
    public Result<List<TreeSelectModel>> loadTreeData(@Parameter(description = "父id") @RequestParam(required = false) Long pid,
                                                      @Parameter(description = "父编码") @RequestParam(required = false) String pcode,
                                                      @Parameter(description = "条件参数") @RequestParam(required = false) String condition) {
        List<TreeSelectModel> list = service.loadTreeData(pid, pcode, condition);
        return Result.ok(list);
    }

    /**
     * 分类字典控件专用接口
     *
     * @apiNote 表单页面-数据回显
     */
    @GetMapping("/loadDictItem")
    @Operation(summary = "分类字典控件专用接口", description = "j-category-select")
    public Result<List<String>> loadDictItem(@Parameter(description = "id数组，逗号分割") @RequestParam String ids) {
        // 非空判断
        if (StrUtil.isEmpty(ids)) {
            return Result.error("ids不能为空！");
        }
        String[] idArray = ids.split(",");
        LambdaQueryWrapper<SysCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysCategory::getId, Arrays.asList(idArray));
        // 查询数据
        List<SysCategory> list = service.list(wrapper);
        // 取出name并返回
        List<String> textList = list.stream().map(SysCategory::getName).collect(Collectors.toList());
        return Result.ok(textList);
    }

    /**
     * 加载分类字典数据
     */
    @GetMapping("/loadAllData")
    @Operation(summary = "加载分类字典数据", description = "用于列表页面值的替换")
    public Result<List<DictModel>> loadAllData(@Parameter(description = "编码") @RequestParam String code) {
        LambdaQueryWrapper<SysCategory> query = new LambdaQueryWrapper<>();
        if (StrUtil.isNotEmpty(code) && !"0".equals(code)) {
            query.likeRight(SysCategory::getCode, code);
        }
        List<SysCategory> list = service.list(query);
        List<DictModel> rdList = new ArrayList<>();
        for (SysCategory sysCategory : list) {
            rdList.add(new DictModel(String.valueOf(sysCategory.getId()), sysCategory.getName()));
        }
        return Result.ok(rdList);
    }
}
