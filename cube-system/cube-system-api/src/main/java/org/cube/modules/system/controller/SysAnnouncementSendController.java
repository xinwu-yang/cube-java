package org.cube.modules.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cube.commons.annotations.DictMethod;
import org.cube.commons.base.CubeController;
import org.cube.commons.base.Result;
import org.cube.commons.utils.SystemContextUtil;
import org.cube.modules.system.entity.SysAnnouncementSend;
import org.cube.modules.system.model.AnnouncementSendModel;
import org.cube.modules.system.model.LoginUser;
import org.cube.modules.system.model.api.request.GetMyNewsRequest;
import org.cube.modules.system.model.api.request.HaveReadRequest;
import org.cube.modules.system.service.ISysAnnouncementSendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户通告发送与阅读标记
 *
 * @author 杨欣武
 * @version 2.5.3
 * @since 2022-08-12
 */
@Tag(name = "通告阅读标记相关接口")
@RestController
@RequestMapping("/sys/announcement/send")
public class SysAnnouncementSendController extends CubeController<SysAnnouncementSend, ISysAnnouncementSendService> {

    /**
     * 标记用户系统消息已读
     */
    @PutMapping("/haveRead")
    @Operation(summary = "系统消息已读")
    public Result<?> haveRead(@Validated @RequestBody HaveReadRequest request) {
        service.haveRead(request.getAnntId());
        return Result.ok();
    }

    /**
     * 获取我的消息
     */
    @DictMethod
    @GetMapping("/getMyAnnouncementSend")
    @Operation(summary = "获取我的消息")
    @Parameters({
            @Parameter(name = "title", description = "标题"),
            @Parameter(name = "sender", description = "发布人"),
            @Parameter(name = "readFlag", description = "阅读状态"),
            @Parameter(name = "msgCategory", description = "消息类型（1:通知公告、2:系统消息）"),
            @Parameter(name = "busType", description = "业务类型"),
            @Parameter(name = "pageNo", description = "页码"),
            @Parameter(name = "pageSize", description = "每页数量")
    })
    public Result<IPage<AnnouncementSendModel>> getMyAnnouncementSend(@Parameter(hidden = true) GetMyNewsRequest request) {
        LoginUser sysUser = SystemContextUtil.currentLoginUser();
        String userId = sysUser.getId();
        AnnouncementSendModel announcementSend = new AnnouncementSendModel();
        announcementSend.setUserId(userId);
        announcementSend.setMsgCategory(request.getMsgCategory());
        announcementSend.setTitle(request.getTitle());
        announcementSend.setSender(request.getSender());
        announcementSend.setReadFlag(request.getReadFlag());
        announcementSend.setBusType(request.getBusType());
        announcementSend.setPageNo((request.getPageNo() - 1) * request.getPageSize());
        announcementSend.setPageSize(request.getPageSize());
        Page<AnnouncementSendModel> pageList = new Page<>(request.getPageNo(), request.getPageSize());
        pageList = service.getMyAnnouncementSendPage(pageList, announcementSend);
        return Result.ok(pageList);
    }

    /**
     * 一键已读
     */
    @PutMapping("/readAll")
    @Operation(summary = "一键已读")
    public Result<?> readAll() {
        service.readAll();
        return Result.ok();
    }
}
