package org.cube.commons.system.api.factory;

import org.cube.commons.system.api.fallback.SysBaseAPIFallback;
import org.cube.commons.system.api.ISysBaseAPI;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class SysBaseAPIFallbackFactory implements FallbackFactory<ISysBaseAPI> {

    @Override
    public ISysBaseAPI create(Throwable throwable) {
        SysBaseAPIFallback fallback = new SysBaseAPIFallback();
        fallback.setCause(throwable);
        return fallback;
    }
}