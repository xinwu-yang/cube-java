package org.cube.plugin.filemanager.models;

import lombok.Data;

@Data
public class SignStsCredentialsParam {
    // 角色策略arn
    private String roleArn;
    // 角色访问session名称
    private String roleSession;
    // 角色权限策略
    private String policy;
    // 过期时效
    private int durationSeconds;
}
