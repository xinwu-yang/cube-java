package org.cube.commons.utils.captcha;

import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.captcha.generator.RandomGenerator;

import java.awt.*;

/**
 * 图片验证码生成器
 */
public class CaptchaGenerator {
    private final AbstractCaptcha captcha;
    private String currentCode;

    /**
     * 设置背景颜色
     *
     * @param background 背景色
     */
    public void setBackground(Color background) {
        captcha.setBackground(background);
    }

    /**
     * 设置随机生产验证码的取值范围
     *
     * @param code 候选
     */
    public void setCode(String code) {
        RandomGenerator randomGenerator = new RandomGenerator(code, 4);
        captcha.setGenerator(randomGenerator);
    }

    /**
     * 获取generate之后产生的验证码
     *
     * @return 验证码
     */
    public String getCode() {
        return this.currentCode;
    }

    /**
     * 生成的图片验证码
     *
     * @return base64字符串
     */
    public String generate() {
        captcha.createCode();
        this.currentCode = captcha.getCode();
        return captcha.getImageBase64Data();
    }

    public CaptchaGenerator(CaptchaType type, CodeGenerator generator) {
        switch (type) {
            case LINE:
                captcha = CaptchaUtil.createLineCaptcha(105, 35, 4, 30);
                break;
            case CIRCLE:
                captcha = CaptchaUtil.createCircleCaptcha(105, 35, 4, 10);
                break;
            case SHEAR:
                captcha = CaptchaUtil.createShearCaptcha(105, 35, 4, 4);
                break;
            default:
                captcha = CaptchaUtil.createGifCaptcha(105, 35, 4);
        }
        captcha.setGenerator(generator);
    }
}
