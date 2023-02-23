package org.cube.application.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 部门数据权限
 */
@Data
@ConfigurationProperties(prefix = "cube.permission.orgcode")
public class OrgCodeViewProperties {

    /**
     * 需要替换到视图的表名
     */
    private List<String> tables;

    /**
     * 是否替换sql中全部表名
     */
    private boolean replaceAll;

    /**
     * 视图后缀
     */
    private String suffix = "_view";
}
