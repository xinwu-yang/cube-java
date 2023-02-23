package org.cube.modules.system.sms.utils;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.cube.modules.system.sms.entity.SysMessage;
import org.cube.modules.system.sms.entity.SysMessageTemplate;
import org.cube.modules.system.sms.handle.enums.SendMsgStatusEnum;
import org.cube.modules.system.sms.service.ISysMessageService;
import org.cube.modules.system.sms.service.ISysMessageTemplateService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;

/**
 * 消息生成工具
 */

@Component
public class PushMsgUtil {
    @Autowired
    private ISysMessageService sysMessageService;
    @Autowired
    private ISysMessageTemplateService sysMessageTemplateService;
    @Autowired
    private Configuration freemarkerConfig;

    /**
     * @param msgType      消息类型 1短信 2邮件 3微信
     * @param templateCode 消息模板码
     * @param params          消息参数
     * @param sentTo       接收消息方
     */
    public boolean sendMessage(String msgType, String templateCode, JSONObject params, String sentTo) {
        List<SysMessageTemplate> sysSmsTemplates = sysMessageTemplateService.selectByCode(templateCode);
        SysMessage sysMessage = new SysMessage();
        if (sysSmsTemplates.size() > 0) {
            SysMessageTemplate sysSmsTemplate = sysSmsTemplates.get(0);
            sysMessage.setEsType(msgType);
            sysMessage.setEsReceiver(sentTo);
            //模板标题
            String title = sysSmsTemplate.getTemplateName();
            //模板内容
            String content = sysSmsTemplate.getTemplateContent();
            StringWriter stringWriter = new StringWriter();
            Template template;
            try {
                template = new Template("SysMessageTemplate", content, freemarkerConfig);
                template.process(params, stringWriter);
            } catch (IOException | TemplateException e) {
                e.printStackTrace();
                return false;
            }
            content = stringWriter.toString();
            sysMessage.setEsTitle(title);
            sysMessage.setEsContent(content);
            sysMessage.setEsParam(JSONUtil.toJsonStr(params));
            sysMessage.setEsSendTime(new Date());
            sysMessage.setEsSendStatus(SendMsgStatusEnum.WAIT.getCode());
            sysMessage.setEsSendNum(0);
            return sysMessageService.save(sysMessage);
        }
        return false;
    }
}
