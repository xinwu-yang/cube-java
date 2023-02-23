package org.cube.modules.system.sms.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.cube.modules.system.sms.entity.SysMessage;
import org.cube.modules.system.sms.handle.ISendMsgHandler;
import org.cube.modules.system.sms.handle.enums.SendMsgStatusEnum;
import org.cube.modules.system.sms.handle.enums.SendMsgTypeEnum;
import org.cube.modules.system.sms.handle.impl.EmailSendMsgHandler;
import org.cube.modules.system.sms.handle.impl.SmsSendMsgHandler;
import org.cube.modules.system.sms.service.ISysMessageService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * 发送消息任务
 */

@Slf4j
public class SendMsgJob implements Job {

    @Autowired
    private ISysMessageService sysMessageService;

    @Value(value = "${spring.mail.username}")
    private String emailFrom;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        log.info("发送消息任务开始执行! ");
        // 1.读取消息中心数据，只查询未发送的和发送失败不超过次数的
        QueryWrapper<SysMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("es_send_status", SendMsgStatusEnum.WAIT.getCode()).or(i -> i.eq("es_send_status", SendMsgStatusEnum.FAIL.getCode()).lt("es_send_num", 6));
        List<SysMessage> sysMessages = sysMessageService.list(queryWrapper);
        // 2.根据不同的类型走不通的发送实现类
        for (SysMessage sysMessage : sysMessages) {
            ISendMsgHandler sendMsgHandle = null;
            if (sysMessage.getEsType().equals(SendMsgTypeEnum.EMAIL.getType())) {
                EmailSendMsgHandler.setEmailFrom(emailFrom);
                sendMsgHandle = new EmailSendMsgHandler();
            } else if (sysMessage.getEsType().equals(SendMsgTypeEnum.SMS.getType())) {
                sendMsgHandle = new SmsSendMsgHandler();
            }
            Integer sendNum = sysMessage.getEsSendNum();
            try {
                sendMsgHandle.sendMsg(sysMessage.getEsReceiver(), sysMessage.getEsTitle(), sysMessage.getEsContent());
                // 发送消息成功
                sysMessage.setEsSendStatus(SendMsgStatusEnum.SUCCESS.getCode());
            } catch (Exception e) {
                // 发送消息出现异常
                sysMessage.setEsSendStatus(SendMsgStatusEnum.FAIL.getCode());
            }
            sysMessage.setEsSendNum(++sendNum);
            // 发送结果回写到数据库
            sysMessageService.updateById(sysMessage);
        }
    }
}
