package org.cube.modules.system.model.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 短信发送VO
 */
@Data
public class SendSMSRequest {

    @NotBlank
    @Schema(title = "手机号码")
    private String mobile;

    @NotBlank
    @Schema(title = "短信验证码模式", description = "登录模式:0 | 注册模式:1 | 忘记密码:2 | 安全验证:3")
    private String smsmode;
}
