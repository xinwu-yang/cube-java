### crypto-starter

- 提供常用加解密/国密算法
- 支持注解字段自动加密
- 支持的对称加密: AES/SM4
- 支持的非对称加密: RSA/SM2
- 摘要算法: MD5/SHA256/SM3(不可解密)

#### Quick Start:

##### 1. 引入依赖

在Maven的pom.xml配置公司的私服，并引入sensitive-spring-boot-starter依赖

```xml
<repositories>
    <!-- 公司私服-->
    <repository>
        <id>tievd</id>
        <url>http://125.71.201.6:8081/content/groups/public</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.tievd.cube.starter</groupId>
    <artifactId>crypto-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

##### 2. 如何使用

2.1 设置密钥

- spring-boot 集成

```yaml
cube:
  crypto:
    aes-key: '3c261ba23741fc4536295bb46c01eda5'
    sm4-key: ''
    rsa-public-key: ''
    rsa-private-key: ''
    sm2-public-key: ''
    sm2-private-key: ''
```

- 单独使用 
> resources目录下创建crypto.properties

```properties
cube.crypto.aes.key=
cube.crypto.sm4.key=
cube.crypto.rsa.publicKey=
cube.crypto.rsa.privateKey=
cube.crypto.sm2.publicKey=
cube.crypto.sm2.privateKey=
```

2.2 只加密, 不解密

- 核心注解参数说明

```
// value Algorithm 算法名称
@Crypto
```

- 定义实体类，并使用 @Crypto 注解

```java

@Data
public class SysUser implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String username;
    @Crypto(Algorithm.AES)
    private String password;
    // 默认
    @Sensitive
    private String email;
    // 自定义长度脱敏
    @Sensitive(start = 4, length = 4)
    private String phone;
}
```

2.3 加密入库/解密出库

- 定义实体类，并将字段类型设置为 CryptoField
- CryptoField.algorithm insert时需要设置加密算法
- CryptoField.plaintext 明文
- CryptoField.ciphertext 密文

```java

@Data
public class SysUser implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String username;
    private CryptoField password;
    // 默认
    @Sensitive
    private String email;
    // 自定义长度脱敏
    @Sensitive(start = 4, length = 4)
    private String phone;
}
```