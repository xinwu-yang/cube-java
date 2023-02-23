package org.cube.modules.system.sms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cube.commons.base.CubeController;
import org.cube.commons.mybatisplus.QueryGenerator;
import org.cube.commons.base.Result;
import org.cube.modules.system.sms.entity.SysMessage;
import org.cube.modules.system.sms.service.ISysMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * 消息中心-消息管理
 */
@Slf4j
@RestController
@RequestMapping("/sys/message/sysMessage")
public class SysMessageController extends CubeController<SysMessage, ISysMessageService> {

    @Autowired
    private ISysMessageService sysMessageService;

    /**
     * 分页列表查询
     */
    @GetMapping("/list")
    public Result<?> queryPageList(SysMessage sysMessage, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        QueryWrapper<SysMessage> queryWrapper = QueryGenerator.initQueryWrapper(sysMessage, req.getParameterMap());
        Page<SysMessage> page = new Page<>(pageNo, pageSize);
        IPage<SysMessage> pageList = sysMessageService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     */
    @PostMapping("/add")
    public Result<?> add(@RequestBody SysMessage sysMessage) {
        sysMessageService.save(sysMessage);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     */
    @PutMapping("/edit")
    public Result<?> edit(@RequestBody SysMessage sysMessage) {
        sysMessageService.updateById(sysMessage);
        return Result.ok("修改成功!");
    }

    /**
     * 通过id删除
     */
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        sysMessageService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.sysMessageService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     */
    @GetMapping("/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SysMessage sysMessage = sysMessageService.getById(id);
        return Result.ok(sysMessage);
    }

    /**
     * 导出excel
     */
    @GetMapping("/exportXls")
    public void exportXls(HttpServletRequest request, HttpServletResponse response, SysMessage sysMessage) throws IOException {
        super.exportXls(request, response, sysMessage, "推送消息模板");
    }

    /**
     * excel导入
     */
    @PostMapping(value = "/importExcel")
    public Result<?> importExcel(HttpServletRequest request) throws Exception {
        return super.importExcel(request, SysMessage.class);
    }
}
