package org.cube.commons.utils.crypto;

import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class PasswordTransportUtil {
    private static final byte[] KEY = HexUtil.decodeHex(SecureUtil.sha256("cube-password-key"));

    /**
     * 获取密码明文
     *
     * @param password 加密密码
     * @return 明文密码
     */
    public static String getPlaintext(String password) {
        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, Arrays.copyOfRange(KEY, 0, 16), Arrays.copyOfRange(KEY, 16, 32));
        return aes.decryptStr(password);
    }

    public static void main(String[] args) {
        String key = "2456380c7870a1ed8a223cdee6e9a5bb";
        String iv = "dee1a4484a7be25c75912492825fe5c4";
        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, HexUtil.decodeHex(key), HexUtil.decodeHex(iv));
        String signature = aes.encryptHex("{\"expired\":1000, \"signTime\":" + System.currentTimeMillis() + "}");
        System.out.println("signature = " + key + "." + signature);
    }
}
