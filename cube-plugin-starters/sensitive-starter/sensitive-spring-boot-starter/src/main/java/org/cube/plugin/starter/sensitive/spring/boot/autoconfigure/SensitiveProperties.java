package org.cube.plugin.starter.sensitive.spring.boot.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 脱敏插件自定义参数
 *
 * @author xinwuy
 * @version 1.0.0
 * @since 2021-05-06
 */
@ConfigurationProperties(prefix = "cube.sensitive")
public class SensitiveProperties {
    private boolean enable;
    private boolean permissionEnable;
    private List<String> whitelist;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isPermissionEnable() {
        return permissionEnable;
    }

    public void setPermissionEnable(boolean permissionEnable) {
        this.permissionEnable = permissionEnable;
    }

    public List<String> getWhitelist() {
        return whitelist;
    }

    public void setWhitelist(List<String> whitelist) {
        this.whitelist = whitelist;
    }

    public SensitiveProperties() {
    }
}
