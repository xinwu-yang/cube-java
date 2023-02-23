package org.cube.modules.system.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.NamingCase;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cube.commons.constant.CommonConst;
import org.cube.commons.base.CubeController;
import org.cube.commons.mybatisplus.QueryGenerator;
import org.cube.commons.annotations.AutoLog;
import org.cube.commons.annotations.DictMethod;
import org.cube.commons.base.Result;
import org.cube.commons.utils.SystemContextUtil;
import org.cube.commons.utils.web.HttpServletUtil;
import org.cube.modules.system.entity.SysAnnouncement;
import org.cube.modules.system.model.api.request.AddAnnouncementRequest;
import org.cube.modules.system.model.api.request.ListAnnouncementRequest;
import org.cube.modules.system.model.api.request.UpdateAnnouncementRequest;
import org.cube.modules.system.model.api.response.AnnouncementListByUserResponse;
import org.cube.modules.system.service.ISysAnnouncementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 系统通告
 *
 * @author 杨欣武
 * @version 2.5.3
 * @since 2022-08-12
 */
@Slf4j
@Tag(name = "系统通告相关接口")
@RestController
@RequestMapping("/sys/announcement")
public class SysAnnouncementController extends CubeController<SysAnnouncement, ISysAnnouncementService> {

    /**
     * 分页列表查询
     */
    @DictMethod
    @GetMapping("/list")
    @Operation(summary = "分页列表查询")
    @Parameters({@Parameter(name = "title", description = "标题"),
            @Parameter(name = "sender", description = "发送人"),
            @Parameter(name = "priority", description = "优先级"),
            @Parameter(name = "msgCategory", description = "消息分类"),
            @Parameter(name = "msgType", description = "消息类型"),
            @Parameter(name = "sendStatus", description = "发送状态"),
            @Parameter(name = "pageNo", description = "页数"),
            @Parameter(name = "pageSize", description = "每页数量"),
            @Parameter(name = "column", description = "排序字段"),
            @Parameter(name = "order", description = "排序方式")
    })
    public Result<IPage<SysAnnouncement>> queryPageList(@Parameter(hidden = true) @Validated ListAnnouncementRequest request) {
        SysAnnouncement announcement = new SysAnnouncement();
        announcement.setDelFlag(CommonConst.NOT_DELETED);
        BeanUtil.copyProperties(request, announcement);
        QueryWrapper<SysAnnouncement> queryWrapper = new QueryWrapper<>(announcement);
        Page<SysAnnouncement> page = new Page<>(request.getPageNo(), request.getPageSize());
        String column = request.getColumn();
        String order = request.getOrder();
        if (StrUtil.isNotEmpty(column)) {
            String orderBy = NamingCase.toUnderlineCase(column).toLowerCase();
            if ("asc".equals(order)) {
                queryWrapper.orderByAsc(orderBy);
            } else {
                queryWrapper.orderByDesc(orderBy);
            }
        }
        IPage<SysAnnouncement> pageList = service.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 新增
     */
    @AutoLog("系统通告-新增")
    @PostMapping("/add")
    @Operation(summary = "新增")
    public Result<?> add(@RequestBody AddAnnouncementRequest request) {
        SysAnnouncement announcement = new SysAnnouncement();
        BeanUtil.copyProperties(request, announcement);
        announcement.setSendStatus(CommonConst.NO_SEND);
        service.saveAnnouncement(announcement);
        return Result.ok();
    }

    /**
     * 更新
     */
    @AutoLog("系统通告-更新")
    @PutMapping("/edit")
    @Operation(summary = "更新")
    public Result<?> edit(@Validated @RequestBody UpdateAnnouncementRequest request) {
        SysAnnouncement announcement = new SysAnnouncement();
        BeanUtil.copyProperties(request, announcement);
        service.updateAnnouncement(announcement);
        return Result.ok();
    }

    /**
     * 通过id删除
     *
     * @param id 通告id
     */
    @AutoLog("系统通告-通过id删除")
    @DeleteMapping("/delete")
    @Operation(summary = "通过id删除")
    public Result<?> delete(@RequestParam String id) {
        service.removeById(id);
        return Result.ok();
    }

    /**
     * 批量删除
     *
     * @param ids 通告id（多个逗号分隔）
     */
    @AutoLog("系统通告-批量删除")
    @DeleteMapping("/deleteBatch")
    @Operation(summary = "批量删除")
    public Result<?> deleteBatch(@Parameter(description = "通告id（多个逗号分隔）") @RequestParam String ids) {
        service.removeBatchByIds(ListUtil.of(ids.split(",")));
        return Result.ok();
    }

    /**
     * 通过id查询
     *
     * @param id 通告id
     */
    @DictMethod
    @GetMapping("/queryById")
    @Operation(summary = "通过id查询")
    public Result<SysAnnouncement> queryById(@RequestParam String id) {
        SysAnnouncement sysAnnouncement = service.getById(id);
        if (sysAnnouncement == null) {
            return Result.error("未找到对应数据！");
        }
        return Result.ok(sysAnnouncement);
    }

    /**
     * 发布通告
     *
     * @param id 通告id
     */
    @AutoLog("系统通告-发布通告")
    @GetMapping("/doReleaseData")
    @Operation(summary = "发布通告")
    public Result<?> doReleaseData(@RequestParam Long id) {
        service.release(id);
        return Result.ok();
    }

    /**
     * 撤回通告
     *
     * @param id 通告id
     */
    @AutoLog("系统通告-撤回通告")
    @GetMapping("/doRevokeData")
    @Operation(summary = "撤回通告")
    public Result<?> doRevokeData(@RequestParam Long id) {
        service.revoke(id);
        return Result.ok();
    }

    /**
     * 查询用户数据并返回系统消息
     */
    @GetMapping("/listByUser")
    @Operation(summary = "查询用户数据并返回系统消息")
    public Result<AnnouncementListByUserResponse> listByUser() {
        AnnouncementListByUserResponse response = service.listByUser();
        return Result.ok(response);
    }

    /**
     * 同步消息
     *
     * @param anntId 通告id
     */
    @AutoLog("系统通告-同步消息")
    @GetMapping("/syncNotice")
    @Operation(summary = "同步消息")
    public Result<?> syncNotice(@RequestParam(required = false) Long anntId) {
        service.syncNotice(anntId);
        return Result.ok();
    }

    /**
     * 导出系统通告为Excel
     *
     * @param sysAnnouncement 查询参数
     */
    @GetMapping("/exportXls")
    @Operation(summary = "导出系统通告为Excel")
    public void exportXls(SysAnnouncement sysAnnouncement, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Step.1 组装查询条件
        QueryWrapper<SysAnnouncement> queryWrapper = QueryGenerator.initQueryWrapper(sysAnnouncement, request.getParameterMap());
        List<SysAnnouncement> sysAnnouncements = service.list(queryWrapper);
        //导出文件名称
        String date = DateUtil.format(new Date(), "yyyyMMddHHmmss");
        HttpServletUtil.addDownloadHeader(response, "系统通告-" + date + easyExcel.getExtension());
        easyExcel.export(sysAnnouncements, response.getOutputStream(), SystemContextUtil.dictTranslator());
    }
}
