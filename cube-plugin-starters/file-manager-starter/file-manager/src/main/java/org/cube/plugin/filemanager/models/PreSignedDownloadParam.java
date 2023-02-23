package org.cube.plugin.filemanager.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PreSignedDownloadParam {
    private String key;
    private String bucket;
    private Duration duration;
    // default binary/octet-stream
    private String contentType;
}
