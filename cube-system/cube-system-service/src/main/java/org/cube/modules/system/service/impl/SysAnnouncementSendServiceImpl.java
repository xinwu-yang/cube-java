package org.cube.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cube.commons.constant.CommonConst;
import org.cube.commons.utils.SystemContextUtil;
import org.cube.modules.system.mapper.SysAnnouncementSendMapper;
import org.cube.modules.system.model.AnnouncementSendModel;
import org.cube.modules.system.model.LoginUser;
import org.cube.modules.system.service.ISysAnnouncementSendService;
import org.cube.modules.system.entity.SysAnnouncementSend;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SysAnnouncementSendServiceImpl extends ServiceImpl<SysAnnouncementSendMapper, SysAnnouncementSend> implements ISysAnnouncementSendService {

    @Override
    public Page<AnnouncementSendModel> getMyAnnouncementSendPage(Page<AnnouncementSendModel> page, AnnouncementSendModel announcementSendModel) {
        return page.setRecords(baseMapper.getMyAnnouncementSendList(page, announcementSendModel));
    }

    @Override
    public void haveRead(Long announcementId) {
        LoginUser user = SystemContextUtil.currentLoginUser();
        SysAnnouncementSend sysAnnouncementSend = new SysAnnouncementSend();
        sysAnnouncementSend.setReadFlag(CommonConst.HAS_READ_FLAG);
        sysAnnouncementSend.setReadTime(new Date());
        UpdateWrapper<SysAnnouncementSend> wrapper = new UpdateWrapper<>();
        wrapper.eq("annt_id", announcementId);
        wrapper.eq("user_id", user.getId());
        baseMapper.update(sysAnnouncementSend, wrapper);
    }

    @Override
    public void readAll() {
        LoginUser sysUser = SystemContextUtil.currentLoginUser();
        String userId = sysUser.getId();
        SysAnnouncementSend sysAnnouncementSend = new SysAnnouncementSend();
        sysAnnouncementSend.setReadFlag(CommonConst.HAS_READ_FLAG);
        sysAnnouncementSend.setReadTime(new Date());
        LambdaUpdateWrapper<SysAnnouncementSend> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysAnnouncementSend::getUserId, userId);
        baseMapper.update(sysAnnouncementSend, wrapper);
    }
}
