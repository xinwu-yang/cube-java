package org.cube.commons.utils;

import cn.hutool.core.util.StrUtil;
import org.cube.modules.system.entity.SysPermission;

import java.util.List;

/**
 * 智能处理错误数据
 *
 * @author scott
 * @since 2019-04-03
 */
public class PermissionDataUtil {

    /**
     * 智能处理错误数据，简化用户失误操作
     */
    public static SysPermission intelligentProcessData(SysPermission permission) {
        if (permission == null) {
            return null;
        }
        // 组件
        if (StrUtil.isNotEmpty(permission.getComponent())) {
            String component = permission.getComponent();
            if (component.startsWith("/")) {
                component = component.substring(1);
            }
            if (component.startsWith("views/")) {
                component = component.replaceFirst("views/", "");
            }
            if (component.startsWith("src/views/")) {
                component = component.replaceFirst("src/views/", "");
            }
            if (component.endsWith(".vue")) {
                component = component.replace(".vue", "");
            }
            permission.setComponent(component);
        }

        // 请求URL
        if (StrUtil.isNotEmpty(permission.getUrl())) {
            String url = permission.getUrl();
            if (url.endsWith(".vue")) {
                url = url.replace(".vue", "");
            }
            if (!url.startsWith("http") && !url.startsWith("/") && !url.trim().startsWith("{{")) {
                url = "/" + url;
            }
            permission.setUrl(url);
        }

        // 一级菜单默认组件
        if (0 == permission.getMenuType() && StrUtil.isEmpty(permission.getComponent())) {
            // 一级菜单默认组件
            permission.setComponent("layouts/RouteView");
        }
        return permission;
    }

    /**
     * 如果没有index页面 需要new 一个放到list中
     */
    public static void addIndexPage(List<SysPermission> metaList) {
        boolean hasIndexMenu = false;
        for (SysPermission sysPermission : metaList) {
            if ("首页".equals(sysPermission.getName())) {
                hasIndexMenu = true;
                break;
            }
        }
        if (!hasIndexMenu) {
            metaList.add(0, new SysPermission(true));
        }
    }

    /**
     * 判断是否授权首页
     */
    public static boolean hasIndexPage(List<SysPermission> metaList) {
        boolean hasIndexMenu = false;
        for (SysPermission sysPermission : metaList) {
            if ("首页".equals(sysPermission.getName())) {
                hasIndexMenu = true;
                break;
            }
        }
        return hasIndexMenu;
    }
}
