package org.cube.modules.system.model.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 登录请求
 *
 * @author scott
 * @since 2019-01-18
 */
@Data
public class LoginRequest implements Serializable {

    @NotBlank
    @Schema(title = "账号")
    private String username;

    @NotBlank
    @Schema(title = "密码")
    private String password;

    @Schema(title = "验证码")
    private String captcha;

    @Schema(title = "验证码key")
    private String checkKey;
}