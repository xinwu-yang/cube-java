package org.cube.modules.system.sms.handle.impl;

import cn.hutool.extra.spring.SpringUtil;
import org.cube.modules.system.sms.handle.ISendMsgHandler;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class EmailSendMsgHandler implements ISendMsgHandler {
    static String emailFrom;

    public static void setEmailFrom(String emailFrom) {
        EmailSendMsgHandler.emailFrom = emailFrom;
    }

    @Override
    public void sendMsg(String receiver, String title, String content) {
        JavaMailSender mailSender = SpringUtil.getBean("mailSender");
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);
            // 设置发送方邮箱地址
            helper.setFrom(emailFrom);
            helper.setTo(receiver);
            helper.setSubject(title);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
