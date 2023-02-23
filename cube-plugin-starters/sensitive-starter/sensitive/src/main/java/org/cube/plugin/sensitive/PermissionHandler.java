package org.cube.plugin.sensitive;

/**
 * @author xinwuy
 * @version V2.3.x
 * @since 2022/1/12
 */
public interface PermissionHandler {

    /**
     * 获取当前用户的权限
     *
     * @return 权限标识符
     */
    String getPermission();
}
