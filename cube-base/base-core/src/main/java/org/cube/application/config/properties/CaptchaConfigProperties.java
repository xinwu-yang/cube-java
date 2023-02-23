package org.cube.application.config.properties;

import org.cube.commons.utils.captcha.CaptchaType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "cube.login.captcha")
public class CaptchaConfigProperties {

    /**
     * 是否启用验证码验证
     */
    private boolean enable = true;

    /**
     * 验证码类型
     */
    private CaptchaType type = CaptchaType.GIF;

    /**
     * 随机验证码取值范围
     */
    private String code;

    /**
     * 是否启用四则运算
     */
    private boolean math = false;

    /**
     * 验证码红色
     */
    private Integer red;

    /**
     * 验证码绿色
     */
    private Integer green;

    /**
     * 验证码蓝
     */
    private Integer blue;
}
