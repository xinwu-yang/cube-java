package org.cube.plugin.starter.crypto.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "cube.crypto")
public class CryptoProperties {

    private String aesKey;

    private String sm4Key;

    private String rsaPublicKey;

    private String rsaPrivateKey;

    private String sm2PublicKey;

    private String sm2PrivateKey;
}
