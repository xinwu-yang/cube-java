### sensitive-starter
支持权限控制的字符串脱敏的SpringBoot插件

##### 版本: 1.2.1

##### Quick Start:
1. 在Maven的pom.xml配置公司的私服，并引入sensitive-spring-boot-starter依赖
```
<repositories>
    <!-- 公司私服-->
    <repository>
        <id>tievd</id>
        <url>http://125.71.201.14:8999/repository/maven-public/</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.tievd.cube.starter</groupId>
    <artifactId>sensitive-spring-boot-starter</artifactId>
    <version>1.2.1</version>
</dependency>
```
2. 如何使用 
- 核心注解参数说明
```
// value 脱敏处理方式，提供DEFAULT和CUSTOM。DEFAULT基本可以满足所有字符串脱敏，主要参数是start，length
// start 默认敏感处理：脱敏字符开始位置，默认值：3，参数要求：下标0开始，长度不得大于处理字符串的长度，否则不会进行脱敏处理
// length 默认敏感处理：脱敏字符长度，默认值：100，参数要求：必须大于0
// custom 实现了ISensitiveCustom接口的字节码对象，当value值为CUSTOM时，会调用custom的实例进行处理
@Sensitive(value = SensitiveType.DEFAULT, start = 3, length = 100, custom = DefaultSensitiveImpl.class)
```
- 定义实体类，并使用 @Sensitive 注解
```java
@Data
public class SysUser implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    // 自定义脱敏方法
    @Sensitive(value = SensitiveType.CUSTOM, custom = PhoneSensitive.class)
    private String username;
    // 默认
    @Sensitive
    private String email;
    // 自定义长度脱敏
    @Sensitive(start = 4, length = 4)
    private String phone;
}
```
- 自定义脱敏方法
```java
// 实现ISensitiveCustom接口
import org.cube.plugin.sensitive.ISensitiveCustom;

public class PhoneSensitive implements ISensitiveCustom {
    @Override
    public String sensitive(String value) {
        return value.replace("ABC","***");
    }
}
```
3. 如何控制权限
- application.yml中配置
```yaml
cube:
  sensitive:
    # 开启字段脱敏，默认未开启（false）
    enable: true
    # 开启字段脱敏权限控制，默认未开启（false）
    permission-enable: true
    # 白名单列表，权限支持角色/部门/用户的三种粒度，且不限于这三种概念，也可在项目中动态加载数据库中的数据
    whitelist: ["user", "depart", "role"]
```

- 实现接口 **org.cube.plugin.sensitive.PermissionHandler** 定义获取当前登录用户权限的方法
- 添加 **interceptor.web.org.cube.plugin.starter.sensitive.spring.SensitiveInterceptor** 拦截器拦截API，确保在序列化前得到用户权限信息