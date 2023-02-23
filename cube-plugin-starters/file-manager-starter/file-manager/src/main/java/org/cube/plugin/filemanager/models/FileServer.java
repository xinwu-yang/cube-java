package org.cube.plugin.filemanager.models;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * 文件服务器
 */
@Data
@Builder
public class FileServer {
    //IP or Host
    private String hostname;
    private int port;
    private String accessKey;
    private String secretKey;
    private boolean isHttps;
    private Map<String, String> params;
}
