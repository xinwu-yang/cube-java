package org.cube.commons.utils;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 多租户ID存储器
 *
 * @author 杨欣武
 * @version 2.0.0
 * @since 2022-05-09
 */
@Slf4j
public class TenantContext {

    private static final ThreadLocal<String> currentTenant = ThreadUtil.createThreadLocal(false);

    public static void setTenant(String tenant) {
        log.debug(" setting tenant to " + tenant);
        currentTenant.set(tenant);
    }

    public static String getTenant() {
        return currentTenant.get();
    }

    public static void clear() {
        currentTenant.remove();
    }
}
