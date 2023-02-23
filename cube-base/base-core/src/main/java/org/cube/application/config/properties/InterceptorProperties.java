package org.cube.application.config.properties;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "cube.interceptor")
public class InterceptorProperties {

    /**
     * 拦截配置映射
     */
    private Map<String, InterceptorConfig> configs = MapUtil.newHashMap(1);

    @Data
    @NoArgsConstructor
    public static class InterceptorConfig {

        /**
         * 包含
         */
        private List<String> includes = ListUtil.of("/**");

        /**
         * 排除
         */
        private List<String> excludes = new ArrayList<>();
    }
}
