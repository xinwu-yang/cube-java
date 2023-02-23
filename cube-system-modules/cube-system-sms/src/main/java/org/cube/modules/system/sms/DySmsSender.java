package org.cube.modules.system.sms;

import cn.hutool.json.JSONObject;
import org.cube.commons.intf.ISmsSender;
import org.cube.modules.system.model.SmsType;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * 短信发送的默认实现
 */
@Component
@ConditionalOnMissingBean
public class DySmsSender implements ISmsSender {

    @Override
    @SneakyThrows
    public void send(String mobile, SmsType type, JSONObject params) {
        switch (type) {
            case LOGIN_TEMPLATE:
                DySmsHelper.sendSms(mobile, params, DySmsEnum.LOGIN_TEMPLATE_CODE);
                break;
            case REGISTER_TEMPLATE:
                DySmsHelper.sendSms(mobile, params, DySmsEnum.REGISTER_TEMPLATE_CODE);
                break;
            default:
                DySmsHelper.sendSms(mobile, params, DySmsEnum.FORGET_PASSWORD_TEMPLATE_CODE);
        }
    }
}
