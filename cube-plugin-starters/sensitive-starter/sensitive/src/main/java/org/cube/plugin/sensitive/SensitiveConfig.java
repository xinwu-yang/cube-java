package org.cube.plugin.sensitive;

import java.util.List;

/**
 * 脱敏插件全局配置
 *
 * @author xinwuy
 * @version 1.0.0
 * @since 2021-05-06
 */
public class SensitiveConfig {
    // 是否开启脱敏
    protected static boolean ENABLE = false;
    // 是否开启带权限的脱敏
    protected static boolean PERMISSION_CHECK = false;
    // 脱敏白名单
    protected static List<String> WHITE_LIST;
    // 当前线程的权限信息，需要和白名单匹配
    protected static final ThreadLocal<String> CURRENT_PERMISSION = new ThreadLocal<>();
}
