package org.cube.plugin.filemanager.models;

import lombok.Data;

import java.time.Instant;

@Data
public class StsCredentials {
    private String accessKeyId;
    private String secretAccessKey;
    private String sessionToken;
    private Instant expiration;
}
