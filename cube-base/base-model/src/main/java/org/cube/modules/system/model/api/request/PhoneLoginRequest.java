package org.cube.modules.system.model.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 手机验证码登录
 */
@Data
public class PhoneLoginRequest {

    @NotBlank
    @Schema(title = "手机号码")
    private String mobile;

    @NotBlank
    @Schema(title = "验证码")
    private String captcha;
}
