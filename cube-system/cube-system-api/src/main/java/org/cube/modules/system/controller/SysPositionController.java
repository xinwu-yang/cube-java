package org.cube.modules.system.controller;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cube.commons.base.CubeController;
import org.cube.commons.mybatisplus.QueryGenerator;
import org.cube.commons.annotations.AutoLog;
import org.cube.commons.annotations.DictMethod;
import org.cube.commons.base.Result;
import org.cube.modules.system.service.ISysPositionService;
import org.cube.plugin.easyexcel.model.ImportExcel;
import org.cube.commons.utils.SystemContextUtil;
import org.cube.commons.utils.web.HttpServletUtil;
import org.cube.modules.system.entity.SysPosition;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 职务管理
 *
 * @author xinwuy
 * @version V1.0.0
 * @since 2019-09-19
 */
@Slf4j
@Tag(name = "职务管理相关接口")
@RestController
@RequestMapping("/sys/position")
public class SysPositionController extends CubeController<SysPosition, ISysPositionService> {

    /**
     * 分页列表查询
     *
     * @param sysPosition 查询参数
     * @param pageNo      页码
     * @param pageSize    每页大小
     */
    @DictMethod
    @GetMapping("/list")
    public Result<IPage<SysPosition>> queryPageList(SysPosition sysPosition, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        QueryWrapper<SysPosition> queryWrapper = QueryGenerator.initQueryWrapper(sysPosition, req.getParameterMap());
        Page<SysPosition> page = new Page<>(pageNo, pageSize);
        IPage<SysPosition> pageList = service.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 新增
     *
     * @param sysPosition 新增参数
     */
    @AutoLog("职务管理-添加")
    @PostMapping("/add")
    public Result<?> add(@RequestBody SysPosition sysPosition) {
        service.save(sysPosition);
        return Result.ok();
    }

    /**
     * 更新
     *
     * @param sysPosition 更新参数
     */
    @AutoLog("职务管理-编辑")
    @PutMapping("/edit")
    public Result<?> edit(@RequestBody SysPosition sysPosition) {
        service.updateById(sysPosition);
        return Result.ok();
    }

    /**
     * 通过id删除
     *
     * @param id 主键id
     */
    @AutoLog("职务管理-通过id删除")
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam String id) {
        return service.delete(ListUtil.of(id));
    }

    /**
     * 批量删除
     *
     * @param ids 主键（多个逗号分隔）
     */
    @AutoLog("职务管理-批量删除")
    @DeleteMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestParam String ids) {
        if (StrUtil.isBlank(ids)) {
            return Result.error("参数不识别！");
        }
        return service.delete(Arrays.asList(ids.split(",")));
    }

    /**
     * 导出数据到Excel
     */
    @GetMapping("/exportXls")
    public void exportXls(HttpServletRequest request, HttpServletResponse response) throws IOException {
        QueryWrapper<SysPosition> queryWrapper = null;
        String paramsStr = request.getParameter("paramsStr");
        if (StrUtil.isNotEmpty(paramsStr)) {
            String deString = URLDecoder.decode(paramsStr, "UTF-8");
            SysPosition sysPosition = JSONUtil.toBean(deString, SysPosition.class);
            queryWrapper = QueryGenerator.initQueryWrapper(sysPosition, request.getParameterMap());
        }
        List<SysPosition> pageList = service.list(queryWrapper);
        String date = DateUtil.format(new Date(), "yyyyMMddHHmmss");
        HttpServletUtil.addDownloadHeader(response, "职务数据-" + date + easyExcel.getExtension());
        easyExcel.export(pageList, response.getOutputStream(), SystemContextUtil.dictTranslator());
    }

    /**
     * 通过Excel导入职位数据
     */
    @PostMapping("/importExcel")
    public Result<?> importExcel(HttpServletRequest request) throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportExcel excel = new ImportExcel();
            excel.setInputStream(file.getInputStream());
            List<SysPosition> sysPositions = easyExcel.read(SysPosition.class, excel, SystemContextUtil.dictTranslator());
            service.saveBatch(sysPositions);
        }
        return Result.ok();
    }
}
