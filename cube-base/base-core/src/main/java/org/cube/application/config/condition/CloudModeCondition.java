package org.cube.application.config.condition;

import org.cube.commons.constant.CommonConst;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 微服务环境加载条件
 */
public class CloudModeCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Object object = context.getEnvironment().getProperty(CommonConst.CLOUD_SERVER_KEY);
        //如果没有服务注册发现的配置 说明是单体应用
        return object != null;
    }
}
