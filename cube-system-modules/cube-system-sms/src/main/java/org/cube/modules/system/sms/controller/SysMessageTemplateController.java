package org.cube.modules.system.sms.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cube.commons.base.CubeController;
import org.cube.commons.mybatisplus.QueryGenerator;
import org.cube.commons.base.Result;
import org.cube.modules.system.sms.entity.MsgParams;
import org.cube.modules.system.sms.entity.SysMessageTemplate;
import org.cube.modules.system.sms.service.ISysMessageTemplateService;
import org.cube.modules.system.sms.utils.PushMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * 消息中心-模版管理
 */
@Slf4j
@RestController
@RequestMapping("/sys/message/sysMessageTemplate")
public class SysMessageTemplateController extends CubeController<SysMessageTemplate, ISysMessageTemplateService> {

    @Autowired
    private ISysMessageTemplateService sysMessageTemplateService;
    @Autowired
    private PushMsgUtil pushMsgUtil;

    /**
     * 分页列表查询
     */
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SysMessageTemplate sysMessageTemplate, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        QueryWrapper<SysMessageTemplate> queryWrapper = QueryGenerator.initQueryWrapper(sysMessageTemplate, req.getParameterMap());
        Page<SysMessageTemplate> page = new Page<>(pageNo, pageSize);
        IPage<SysMessageTemplate> pageList = sysMessageTemplateService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     */
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SysMessageTemplate sysMessageTemplate) {
        sysMessageTemplateService.save(sysMessageTemplate);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     */
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SysMessageTemplate sysMessageTemplate) {
        sysMessageTemplateService.updateById(sysMessageTemplate);
        return Result.ok("更新成功！");
    }

    /**
     * 通过id删除
     */
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        sysMessageTemplateService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     */
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.sysMessageTemplateService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     */
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        SysMessageTemplate sysMessageTemplate = sysMessageTemplateService.getById(id);
        return Result.ok(sysMessageTemplate);
    }

    /**
     * 导出excel
     */
    @GetMapping(value = "/exportXls")
    public void exportXls(HttpServletRequest request, HttpServletResponse response, SysMessageTemplate sysMessageTemplate) throws IOException {
        super.exportXls(request, response, sysMessageTemplate, "推送消息模板");
    }

    /**
     * excel导入
     */
    @PostMapping("/importExcel")
    public Result<?> importExcel(HttpServletRequest request) throws Exception {
        return super.importExcel(request, SysMessageTemplate.class);
    }

    /**
     * 发送消息
     */
    @PostMapping("/sendMsg")
    public Result<?> sendMessage(@RequestBody MsgParams msgParams) {
        JSONObject params = JSONUtil.parseObj(msgParams.getTestData());
        boolean result = pushMsgUtil.sendMessage(msgParams.getMsgType(), msgParams.getTemplateCode(), params, msgParams.getReceiver());
        if (!result) {
            return Result.error("发送消息任务添加失败！");
        }
        return Result.ok("发送消息任务添加成功！");
    }
}
