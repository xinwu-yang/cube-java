package org.cube.application.config.mybatis;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 多租户tenantId存储器
 */
@Slf4j
public class TenantContext {
    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();

    public static void setTenant(String tenant) {
        log.debug("set tenant to " + tenant);
        currentTenant.set(tenant);
    }

    public static String getTenant() {
        String tenantId = currentTenant.get();
        if (ObjectUtil.isEmpty(tenantId)) {
            return "0";
        }
        return tenantId;
    }

    public static void clear() {
        currentTenant.remove();
    }
}