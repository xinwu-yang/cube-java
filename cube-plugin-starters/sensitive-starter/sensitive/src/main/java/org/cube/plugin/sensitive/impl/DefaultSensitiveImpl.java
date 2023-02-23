package org.cube.plugin.sensitive.impl;

import org.cube.plugin.sensitive.ISensitiveCustom;
import lombok.extern.slf4j.Slf4j;

/**
 * 脱敏插件的默认实现
 *
 * @author xinwuy
 * @version 1.2.0
 * @since 2022-01-10
 */
@Slf4j
public class DefaultSensitiveImpl implements ISensitiveCustom {

    // 开始下标
    private final int startIndex;
    // 脱敏长度
    private int length;

    public DefaultSensitiveImpl(int startIndex, int length) {
        this.startIndex = startIndex;
        this.length = length;
    }

    @Override
    public String sensitive(String value) {
        int strLength = value.length();
        if (this.startIndex < 0 || this.startIndex > strLength - 1 || this.length < 1) {
            log.warn("脱敏的字符串开始下标大于字符串本身长度！脱敏值：{}，开始下标：{}，脱敏长度：{}", value, this.startIndex, this.length);
            return value;
        }
        // 要处理的字符串长度大于显示的字符串长度才处理
        int realLength = strLength - startIndex;
        if (this.length > realLength) {
            this.length = realLength;
        }
        StringBuilder sensitiveStr = new StringBuilder();
        // 截取出前面的明文
        sensitiveStr.append(value, 0, startIndex);
        for (int i = 0; i < this.length; i++) {
            sensitiveStr.append("*");
        }
        sensitiveStr.append(value.substring(startIndex + length));
        return sensitiveStr.toString();
    }
}
