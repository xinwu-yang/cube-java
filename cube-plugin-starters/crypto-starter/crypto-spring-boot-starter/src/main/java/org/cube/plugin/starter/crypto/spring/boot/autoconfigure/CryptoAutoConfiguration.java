package org.cube.plugin.starter.crypto.spring.boot.autoconfigure;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SM4;
import org.cube.plugin.crypto.handler.AlgorithmHandler;
import org.cube.plugin.crypto.intercepts.CryptoInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableConfigurationProperties(CryptoProperties.class)
public class CryptoAutoConfiguration {
    @Autowired
    private CryptoProperties cryptoProperties;

    @Bean
    public AlgorithmHandler algorithmHandler() {
        SM4 sm4 = null;
        AES aes = null;
        RSA rsa = null;
        SM2 sm2 = null;
        if (StrUtil.isNotEmpty(cryptoProperties.getAesKey())) {
            log.info("Initializing crypto with AES");
            aes = SecureUtil.aes(HexUtil.decodeHex(cryptoProperties.getAesKey()));
        }
        if (StrUtil.isNotEmpty(cryptoProperties.getSm4Key())) {
            log.info("Initializing crypto with SM4");
            sm4 = SmUtil.sm4(HexUtil.decodeHex(cryptoProperties.getSm4Key()));
        }
        if (!StrUtil.hasEmpty(cryptoProperties.getRsaPrivateKey(), cryptoProperties.getRsaPublicKey())) {
            log.info("Initializing crypto with RSA");
            rsa = new RSA(cryptoProperties.getRsaPrivateKey(), cryptoProperties.getRsaPublicKey());
        }
        if (!StrUtil.hasEmpty(cryptoProperties.getSm2PrivateKey(), cryptoProperties.getSm2PublicKey())) {
            log.info("Initializing crypto with SM2");
            sm2 = SmUtil.sm2(cryptoProperties.getSm2PrivateKey(), cryptoProperties.getSm2PublicKey());
        }
        return new AlgorithmHandler(sm4, aes, rsa, sm2);
    }

    @Bean
    public CryptoInterceptor cryptoInterceptor(AlgorithmHandler algorithmHandler) {
        return new CryptoInterceptor(algorithmHandler);
    }
}
