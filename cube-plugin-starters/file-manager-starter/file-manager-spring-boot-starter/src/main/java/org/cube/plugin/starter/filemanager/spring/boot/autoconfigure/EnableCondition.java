package org.cube.plugin.starter.filemanager.spring.boot.autoconfigure;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

/**
 * 如果协议不为空则启动
 *
 * @author xinwuy
 * @version V2.3.x
 * @since 2022/1/5
 */
public class EnableCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String protocol = context.getEnvironment().getProperty("cube.file-manager.protocol");
        return !StringUtils.isEmpty(protocol);
    }
}
