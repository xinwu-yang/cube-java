package org.cube.commons.utils.crypto;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

/**
 * 密码加密解密工具类
 *
 * @author xinwuy
 * @version 2.3.6
 * @since 2022-02-08
 */
public class SecurityUtil {

    /**
     * 加密key
     */
    private static final byte[] key = HexUtil.decodeHex("e02454fa3b359437d71d1f7380eec452");

    /**
     * 加密
     */
    public static String encrypt(String content) {
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
        return aes.encryptHex(content);
    }

    /**
     * 解密
     */
    public static String decrypt(String encryptResultStr) {
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
        //解密为字符串
        return aes.decryptStr(encryptResultStr, CharsetUtil.CHARSET_UTF_8);
    }
}
