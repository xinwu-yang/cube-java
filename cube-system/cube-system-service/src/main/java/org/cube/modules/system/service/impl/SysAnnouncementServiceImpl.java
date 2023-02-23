package org.cube.modules.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cube.commons.constant.CommonConst;
import org.cube.commons.constant.WebSocketConst;
import org.cube.commons.exception.CubeAppException;
import org.cube.commons.utils.SystemContextUtil;
import org.cube.modules.system.entity.SysAnnouncement;
import org.cube.modules.system.entity.SysAnnouncementSend;
import org.cube.modules.system.mapper.SysAnnouncementMapper;
import org.cube.modules.system.mapper.SysAnnouncementSendMapper;
import org.cube.modules.system.model.LoginUser;
import org.cube.modules.system.model.api.response.AnnouncementListByUserResponse;
import org.cube.modules.system.service.ISysAnnouncementService;
import org.cube.modules.system.extra.ws.AppWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SysAnnouncementServiceImpl extends ServiceImpl<SysAnnouncementMapper, SysAnnouncement> implements ISysAnnouncementService {

    @Autowired
    private SysAnnouncementSendMapper sysAnnouncementSendMapper;
    @Autowired
    private AppWebSocketHandler appWebSocketHandler;

    @Override
    public void saveAnnouncement(SysAnnouncement announcement) {
        baseMapper.insert(announcement);
        if (announcement.getMsgType().equals(CommonConst.MSG_TYPE_UESR)) {
            // 2.插入用户通告阅读标记表记录
            String userId = announcement.getUserIds();
            String[] userIds = userId.substring(0, (userId.length() - 1)).split(",");
            Date refDate = new Date();
            for (String id : userIds) {
                SysAnnouncementSend announcementSend = new SysAnnouncementSend();
                announcementSend.setAnntId(announcement.getId());
                announcementSend.setUserId(id);
                announcementSend.setReadFlag(CommonConst.NO_READ_FLAG);
                announcementSend.setReadTime(refDate);
                sysAnnouncementSendMapper.insert(announcementSend);
            }
        }
    }

    @Override
    public void updateAnnouncement(SysAnnouncement announcement) {
        // 1.更新系统信息表数据
        baseMapper.updateById(announcement);
        String userId = announcement.getUserIds();
        if (StrUtil.isNotEmpty(userId) && announcement.getMsgType().equals(CommonConst.MSG_TYPE_UESR)) {
            // 2.补充新的通知用户数据
            String[] userIds = userId.substring(0, (userId.length() - 1)).split(",");
            Date refDate = new Date();
            for (String id : userIds) {
                LambdaQueryWrapper<SysAnnouncementSend> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(SysAnnouncementSend::getAnntId, announcement.getId());
                queryWrapper.eq(SysAnnouncementSend::getUserId, id);
                List<SysAnnouncementSend> announcementSends = sysAnnouncementSendMapper.selectList(queryWrapper);
                if (announcementSends.size() == 0) {
                    SysAnnouncementSend announcementSend = new SysAnnouncementSend();
                    announcementSend.setAnntId(announcement.getId());
                    announcementSend.setUserId(id);
                    announcementSend.setReadFlag(CommonConst.NO_READ_FLAG);
                    announcementSend.setReadTime(refDate);
                    sysAnnouncementSendMapper.insert(announcementSend);
                }
            }
            // 3. 删除多余通知用户数据
            Collection<String> delUserIds = Arrays.asList(userIds);
            LambdaQueryWrapper<SysAnnouncementSend> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.notIn(SysAnnouncementSend::getUserId, delUserIds);
            queryWrapper.eq(SysAnnouncementSend::getAnntId, announcement.getId());
            sysAnnouncementSendMapper.delete(queryWrapper);
        }
    }

    @Override
    public Page<SysAnnouncement> queryAnnouncementByUserId(Page<SysAnnouncement> page, String userId, int msgCategory) {
        return page.setRecords(baseMapper.querySysCementListByUserId(page, userId, msgCategory));
    }

    @Override
    public void release(Long announcementId) {
        SysAnnouncement announcement = baseMapper.selectById(announcementId);
        if (announcement == null) {
            throw new CubeAppException("未找到对应实体！");
        }
        announcement.setSendTime(new Date());
        announcement.setSendStatus(CommonConst.HAS_SEND);//发布中
        String currentUserName = StpUtil.getLoginIdAsString();
        announcement.setSender(currentUserName);
        int ok = baseMapper.updateById(announcement);
        if (ok >= 1) {
            JSONObject msgData = JSONUtil.createObj();
            if (announcement.getMsgType().equals(CommonConst.MSG_TYPE_ALL)) {
                msgData.set(WebSocketConst.MSG_CMD, WebSocketConst.CMD_TOPIC);
                msgData.set(WebSocketConst.MSG_ID, announcement.getId());
                msgData.set(WebSocketConst.MSG_TXT, announcement.getTitle());
                appWebSocketHandler.sendAllMessage(msgData.toString());
            } else {
                // 2.插入用户通告阅读标记表记录
                String userId = announcement.getUserIds();
                String[] userIds = userId.substring(0, (userId.length() - 1)).split(",");
                msgData.set(WebSocketConst.MSG_CMD, WebSocketConst.CMD_USER);
                msgData.set(WebSocketConst.MSG_ID, announcement.getId());
                msgData.set(WebSocketConst.MSG_TXT, announcement.getTitle());
                appWebSocketHandler.sendMoreMessage(userIds, msgData.toString());
            }
        }
    }

    @Override
    public void revoke(Long announcementId) {
        SysAnnouncement announcement = baseMapper.selectById(announcementId);
        if (announcement == null) {
            throw new CubeAppException("未找到对应数据！");
        }
        announcement.setCancelTime(new Date());
        announcement.setSendStatus(CommonConst.HAS_CANCEL);
        baseMapper.updateById(announcement);
    }

    @Override
    public AnnouncementListByUserResponse listByUser() {
        LoginUser sysUser = SystemContextUtil.currentLoginUser();
        String userId = sysUser.getId();
        // 1.将系统消息补充到用户通告阅读标记表中
        List<String> anntIds = sysAnnouncementSendMapper.queryByUserId(userId);
        LambdaQueryWrapper<SysAnnouncement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysAnnouncement::getMsgType, CommonConst.MSG_TYPE_ALL); // 全部人员
        wrapper.eq(SysAnnouncement::getDelFlag, CommonConst.NOT_DELETED.toString());  // 未删除
        wrapper.eq(SysAnnouncement::getSendStatus, CommonConst.HAS_SEND); //已发布
        wrapper.ge(SysAnnouncement::getEndTime, sysUser.getCreateTime()); //新注册用户不看结束通知
        if (anntIds != null && anntIds.size() > 0) {
            wrapper.notIn(SysAnnouncement::getId, anntIds);
        }
        List<SysAnnouncement> announcements = baseMapper.selectList(wrapper);
        if (announcements.size() > 0) {
            for (SysAnnouncement announcement : announcements) {
                // update-begin--Author:wangshuai  Date:20200803  for： 通知公告消息重复
                // 因为websocket没有判断是否存在这个用户，要是判断会出现问题，故在此判断逻辑
                LambdaQueryWrapper<SysAnnouncementSend> query = new LambdaQueryWrapper<>();
                query.eq(SysAnnouncementSend::getAnntId, announcement.getId());
                query.eq(SysAnnouncementSend::getUserId, userId);
                SysAnnouncementSend one = sysAnnouncementSendMapper.selectOne(query);
                if (null == one) {
                    SysAnnouncementSend announcementSend = new SysAnnouncementSend();
                    announcementSend.setAnntId(announcement.getId());
                    announcementSend.setUserId(userId);
                    announcementSend.setReadFlag(CommonConst.NO_READ_FLAG);
                    sysAnnouncementSendMapper.insert(announcementSend);
                }
                // update-end--Author:wangshuai  Date:20200803  for： 通知公告消息重复
            }
        }
        // 2.查询用户未读的系统消息
        Page<SysAnnouncement> anntMsgList = new Page<>(0, 5);
        // 通知公告消息
        anntMsgList = this.queryAnnouncementByUserId(anntMsgList, userId, CommonConst.MSG_CATEGORY_NOTICE);
        Page<SysAnnouncement> sysMsgList = new Page<>(0, 5);
        // 系统消息
        sysMsgList = this.queryAnnouncementByUserId(sysMsgList, userId, CommonConst.MSG_CATEGORY_SYSTEM);
        AnnouncementListByUserResponse response = new AnnouncementListByUserResponse();
        response.setSysMsgTotal(sysMsgList.getTotal());
        response.setAnntMsgTotal(anntMsgList.getTotal());
        response.setSysMsgList(sysMsgList.getRecords());
        response.setAnntMsgList(anntMsgList.getRecords());
        return response;
    }

    @Override
    public void syncNotice(Long announcementId) {
        JSONObject message = JSONUtil.createObj();
        if (announcementId != null) {
            SysAnnouncement announcement = baseMapper.selectById(announcementId);
            if (announcement == null) {
                throw new CubeAppException("未找到对应数据！");
            }
            message.set(WebSocketConst.MSG_ID, announcement.getId());
            message.set(WebSocketConst.MSG_TXT, announcement.getTitle());
            if (announcement.getMsgType().equals(CommonConst.MSG_TYPE_ALL)) {
                message.set(WebSocketConst.MSG_CMD, WebSocketConst.CMD_TOPIC);
                appWebSocketHandler.sendAllMessage(message.toString());
            } else {
                // 2.插入用户通告阅读标记表记录
                String userId = announcement.getUserIds();
                if (StrUtil.isNotEmpty(userId)) {
                    message.set(WebSocketConst.MSG_CMD, WebSocketConst.CMD_USER);
                    String[] userIds = userId.substring(0, (userId.length() - 1)).split(",");
                    appWebSocketHandler.sendMoreMessage(userIds, message.toString());
                }
            }
        } else {
            message.set(WebSocketConst.MSG_CMD, WebSocketConst.CMD_TOPIC);
            message.set(WebSocketConst.MSG_TXT, "批量设置已读");
            appWebSocketHandler.sendAllMessage(message.toString());
        }
    }
}
