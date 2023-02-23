package org.cube.plugin.crypto.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CryptoField {
    // 加密算法
    private Algorithm algorithm;
    // 密文
    private String ciphertext;
    // 明文
    private String plaintext;
}
