package org.cube.plugin.sensitive;

import java.util.List;

/**
 * 脱敏插件全局配置自定义类
 *
 * @author xinwuy
 * @version 1.0.0
 * @since 2021-05-06
 */
public class SensitiveResolver {

    /**
     * 是否开启脱敏
     *
     * @param enable true 开启
     */
    public void enable(boolean enable) {
        SensitiveConfig.ENABLE = enable;
    }

    /**
     * 是否开启权限验证支持
     *
     * @param enable true 开启
     */
    public void enablePermissionCheck(boolean enable) {
        SensitiveConfig.PERMISSION_CHECK = enable;
    }

    /**
     * 提供设置当前登录的权限的API
     *
     * @param permission 用户名/部门/角色等
     */
    public void currentPermission(String permission) {
        SensitiveConfig.CURRENT_PERMISSION.set(permission);
    }

    /**
     * 设置白名单
     *
     * @param whitelist 白名单列表，建议ArrayList
     */
    public void whitelist(List<String> whitelist) {
        SensitiveConfig.WHITE_LIST = whitelist;
    }
}
