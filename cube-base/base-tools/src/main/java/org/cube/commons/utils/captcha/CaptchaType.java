package org.cube.commons.utils.captcha;

/**
 * 验证码类型
 *
 * @author xinwuy
 * @version V2.3.x
 * @since 2022/1/5
 */
public enum CaptchaType {
    //Gif图像
    GIF,
    //线段干扰的验证码
    LINE,
    //圆圈干扰验证码
    CIRCLE,
    //扭曲干扰验证码
    SHEAR
}
