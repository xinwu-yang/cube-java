package org.cube.plugin.sensitive;

/**
 * 脱敏插件自定义脱敏方式接口
 *
 * @author xinwuy
 * @version 1.0.0
 * @since 2021-05-06
 */
public interface ISensitiveCustom {

    /**
     * 自定义敏感数据处理方式，默认是仅显示前三位
     *
     * @param value 原始值
     * @return 处理后的值
     */
    String sensitive(String value);
}
