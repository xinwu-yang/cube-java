package org.cube.plugin.crypto.handler;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SM4;
import cn.hutool.setting.dialect.Props;
import org.cube.plugin.crypto.model.Algorithm;
import lombok.Data;

@Data
public class AlgorithmHandler {
    public static AlgorithmHandler INSTANCE;
    private static final Props PROPS = Props.getProp("crypto.properties");
    private SM4 sm4;
    private AES aes;
    private RSA rsa;
    private SM2 sm2;

    public AlgorithmHandler() {
        String aesKey = PROPS.getStr("cube.crypto.aes.key");
        if (StrUtil.isNotEmpty(aesKey)) {
            aes = SecureUtil.aes(HexUtil.decodeHex(aesKey));
        }
        String sm4Key = PROPS.getStr("cube.crypto.sm4.key");
        if (StrUtil.isNotEmpty(sm4Key)) {
            sm4 = SmUtil.sm4(HexUtil.decodeHex(sm4Key));
        }
        String rsaPublicKey = PROPS.getStr("cube.crypto.rsa.publicKey");
        String rsaPrivateKey = PROPS.getStr("cube.crypto.rsa.privateKey");
        if (!StrUtil.hasEmpty(rsaPublicKey, rsaPrivateKey)) {
            rsa = new RSA(rsaPrivateKey, rsaPublicKey);
        }
        String sm2PublicKey = PROPS.getStr("cube.crypto.sm2.publicKey");
        String sm2PrivateKey = PROPS.getStr("cube.crypto.sm2.privateKey");
        if (!StrUtil.hasEmpty(sm2PublicKey, sm2PrivateKey)) {
            sm2 = SmUtil.sm2(sm2PrivateKey, sm2PublicKey);
        }
        INSTANCE = this;
    }

    public AlgorithmHandler(SM4 sm4, AES aes, RSA rsa, SM2 sm2) {
        this.sm4 = sm4;
        this.aes = aes;
        this.rsa = rsa;
        this.sm2 = sm2;
        INSTANCE = this;
    }

    public String encrypt(Algorithm algorithm, String plainText) {
        String ciphertext;
        switch (algorithm) {
            case AES:
                ciphertext = aes.encryptHex(plainText);
                break;
            case SM4:
                ciphertext = sm4.encryptHex(plainText);
                break;
            case RSA:
                ciphertext = rsa.encryptHex(plainText, KeyType.PublicKey);
                break;
            case SM2:
                ciphertext = sm2.encryptHex(plainText, KeyType.PublicKey);
                break;
            case MD5:
                ciphertext = DigestUtil.md5Hex(plainText);
                break;
            case SHA_256:
                ciphertext = DigestUtil.sha256Hex(plainText);
                break;
            case SM3:
                ciphertext = SmUtil.sm3(plainText);
                break;
            default:
                ciphertext = plainText;
        }
        return ciphertext;
    }

    public String decrypt(Algorithm algorithm, String ciphertext) {
        String plainText;
        switch (algorithm) {
            case AES:
                plainText = aes.decryptStr(ciphertext);
                break;
            case SM4:
                plainText = sm4.decryptStr(ciphertext);
                break;
            case RSA:
                plainText = rsa.decryptStr(ciphertext, KeyType.PrivateKey);
                break;
            case SM2:
                plainText = sm2.decryptStr(ciphertext, KeyType.PrivateKey);
                break;
            default:
                plainText = ciphertext;
        }
        return plainText;
    }
}
