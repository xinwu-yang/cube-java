package org.cube.commons.intf;

import cn.hutool.json.JSONObject;
import org.cube.modules.system.model.SmsType;

/**
 * 短信发送接口
 */
public interface ISmsSender {

    /**
     * 发送短信
     *
     * @param mobile 手机号
     * @param type   短信类型
     * @param params 参数
     */
    void send(String mobile, SmsType type, JSONObject params);
}
