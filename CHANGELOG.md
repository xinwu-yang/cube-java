### 2.6.5 2022-12-15

##### 🔥依赖升级

- 升级 mybatis-plus to 3.5.3.1
- 升级 hutool to 5.8.11

##### 🐞缺陷修复

- 修复了登录未传验证码导致空指针
- 新增用户接口角色和部门不是必填
- 修复用户修改密码可能改到别人的密码

---

### 2.6.4 2022-12-01

##### 🌟新功能

- Excel 支持指定列读取数据

##### 🐞缺陷修复

- 修复我的消息无法全部已读
- 修复我的消息无法根据条件检索
- 我的消息添加字典翻译
- 修复登出时需要先删除用户缓存再登出
- 修复重新选择登录部门时未删除原有用户缓存

##### 🔨代码重构

- `com.tievd.cube.modules.system.websocket`to`com.tievd.cube.modules.system.extra.ws` `破坏性更新`
- `com.tievd.cube.modules.system.rule`to`com.tievd.cube.modules.system.extra.fillrules` `破坏性更新`

---

### 2.6.3 2022-11-17

##### 🌟新功能

- 完善魔方接口文档
- 字典翻译支持实体类继承

##### 🔥依赖升级

- 升级 spring-boot to 2.6.14
- 升级 sa-token to 1.33.0
- 升级 hutool to 5.8.10
- 升级 springdoc to 1.6.13

##### 🔨代码重构

- 重构包名`com.tievd.cube.common`to`com.tievd.cube.commons` `破坏性更新`

---

### 2.6.2 2022-11-04

##### 🌟新功能

- 完善魔方接口文档
- 通用地图服务插件：新增基于SQL的magic-api
- Easy-Excel 支持错误回显，数据校验，字段忽略

##### 🔥依赖升级

- 升级 sa-token to 1.32.0
- 升级 easy-excel to 1.2.1

##### 🐞缺陷修复

- 修复文件上传中文件路径和文件名称命名bug

---

### 2.6.1 2022-10-28

##### 🌟新功能

- 通用地图服务插件：新增地图坐标系转换算法 [详见](/doc/240)
- 接入springdoc-openapi(基于swagger-core)，提供基于**OpenAPI 3**标准的魔方接口文档 [详见](/doc/243)
- 代码生成器新增自定义拓展参数

##### 🔥依赖升级

- 升级 spring-boot to 2.6.13
- 升级 hutool to 5.8.9
- 升级 redisson to 3.17.7
- 升级 versions-maven-plugin to 2.13.0
- 升级 maven-resources-plugin to 3.2.0
- 替换 mysql 新驱动包名(com.mysql:mysql-connector-j)

##### 🚫废弃移除

- 移除 smart-doc-maven-plugin

---

### 2.6.0 2022-10-21

##### 🌟新功能

- 通用地图服务插件发布，支持三个通用API（地图点/区域/轨迹）[详见](/doc/240)
- 支持自定义**Mvc拦截器**注入和配置。[详见](/doc/241) `需要修改配置文件`
- 支持自定义**Mvc Formatter**配置。[详见](/doc/242)

##### 🐞缺陷修复

- 修复字典翻译对null值的兼容性

---

### 2.5.6 2022-10-08

##### 🌟新功能

- 接入`magic-api`

##### 🔥依赖升级

- 升级 spring-boot to 2.6.12
- 升级 dynamic-datasource to 3.5.2
- 升级 hutool to 5.8.8
- 升级 smart-doc-maven-plugin to 2.5.3

##### 🔨代码重构

- 重构包名`com.tievd.cube.common.util`to`com.tievd.cube.commons.utils` `破坏性更新`

---

### 2.5.5 2022-09-13

##### 🐞缺陷修复

- 防止 `AutoInjectBasicDataInterceptor` 因为没有Web环境而报错

##### 🔥依赖升级

- 升级 sa-token to 1.31.0
- 升级 hutool to 5.8.6
- 升级 spring-cloud to 2021.04
- 升级 spring-cloud-alibaba to 2021.0.4.0
- 升级 druid to 1.2.12

---

### 2.5.4 2022-08-26

##### 🔥依赖升级

- 升级 SpringBoot to 2.6.11
- 升级 XXL-Job to 2.3.1
- 升级 ShardingSphere to 5.1.2
- 升级 spring-boot-admin to 2.6.9

##### 📚文档完善

- 优化`xxl-job`开发文档
- 新增`shardingsphere`开发文档和Demo
- 新增`seata`开发文档和Demo

---

### 2.5.3 2022-08-18

##### 🌟新功能

- 接入`JavaMelody`提供全面监控（SQL、Spring Beans、API、System、JVM等）

##### 🔥依赖升级

- 升级 cube-db with `2.5.3`

##### 🔨代码重构

- 所有实体类Controller继承CubeController简化代码
- 重构 SysAnnouncementSendController、SysAnnouncementController
- 重构 SysDataLogController、SysDepartPermissionController
- 重构 SysCategoryController

##### 📚文档完善

- 新增多租户开发方案

---

### 2.5.2 2022-08-11

##### 🐞缺陷修复

- 修复`CommonAPI.getUserByName()`无法使用`@Cacheable`缓存

##### 🔨代码重构

- 删除SysUserCacheInfo，统一使用LoginUser
- `SystemContextUtil.log()`区分登录日志和操作日志
- `@AutoLog`在未登录情况下打印警告日志
- 重构 CommonController、LoginController、DuplicateCheckController

##### 🚫废弃移除

- 移除 `CommonAPI.currentLoginUser()` 函数

##### ✅功能增强

- `@LimitSubmit` 功能增强：支持读取实体类中的参数，支持定制消息提示

##### 📚文档完善

- 14项文档增强、修复

---

### 2.5.1 2022-08-03

##### 🌟新功能

- 新增AK/SK鉴权方式签发API调用凭证
- 支持Tendis，进行深度Tendis测试 `国产化`
- 支持TiDB，进行深度TiDB测试 `国产化`

##### 🔥依赖升级

- 升级 SpringBoot to 2.6.10
- 升级 HuTool to 5.8.5
- 升级 versions-maven-plugin to 2.11.0

---

### 2.5.0 2022-07-19

##### 🌟新功能

- 支持达梦8，提供达梦数据库脚本 `国产化`
- 新增`@LimitSubmit`注解防止接口/函数重复提交
- 新增`SystemContextUtil.log()`方法添加系统日志

##### 🐞缺陷修复

- 在线用户统计同IP下多端登录无法正常显示在线时长

##### 🔨代码重构

- 删除SysLog无用字段

##### 🔥依赖升级

- 升级 SpringBoot to 2.6.9
- 升级 easy-excel to 1.2.0
- 升级 HuTool to 5.8.4
- 升级 smart-doc-maven-plugin to 2.4.8

##### ✅功能增强

- 部门结构变化带来的部门权限无法控制的解决方案
- 拓展用户部门字段解决方案 `破坏性更新`
- 优化`DataIntegrityViolationException`的异常处理，并提示相应错误

---

### 2.4.6 2022-06-14

- 🔥Upgrade cube-system-modules to 2.4.6
- 🔥Upgrade HuTool to 5.8.2
- 🔥Upgrade smart-doc-maven-plugin to 2.4.7
- 🐞Fix SysDepartTreeModel没有无参构造导致的bug
- 🔨Refactor 一些API参数为具体实体类
- 🔨Refactor 获取字典项API支持代逗号的SQL条件拼接
- 🔨Refactor SqlInjectionUtil优化写法
- 🔨Refactor 重构部分类的包结构
- 🔨Refactor 登录相关接口添加参数验证
- 🚫Remove 删除废弃的类

### 2.4.5 2022-05-31

- 🔥Upgrade CubeDB to 2.4.5
- 🔥Upgrade smart-doc-maven-plugin to 2.4.6
- 🔨Refactor 所有实体类delFlag字段的数据类型变更为Integer

### 2.4.4 2022-05-30

- 🔥Upgrade SpringBoot to 2.6.8
- 🔥Upgrade SpringCloud to 2021.0.3
- 🔥Upgrade CubeCloud to 2.4.4
- 🐞Fix 暂时不用DelFlag枚举，因为存在多处不兼容的地方
- 🔨Refactor 重命名已Jeecg开头的类，变更为Cube开头

### 2.4.3 2022-05-26

- 🌟New Feature starter-rocketmq
- 🔥Upgrade SpringBoot to 2.6.7 `SpringBoot默认禁用循环引用`
- 🔥Upgrade SpringCloud to 2021.0.2
- 🔥Upgrade SpringCloudAlibaba to 2021.0.1.0
- 🔥Upgrade SpringBootAdmin to 2.6.7
- 🐞Fix 字典翻译时需要翻译的数据是基础类型的bug
- 🐞Fix mybatis-plus UpdateWrapper 更新数据时，无法在mybatis拦截器中获得更新的实体类对象信息导致的报错
- 🐞Fix 消息已读参数接受问题
- 🐞Fix SystemAPIController mapping url 配置错误的问题

### 2.4.2 2022-05-18

- 🌟New Feature starter-seata
- 🌟New Feature starter-shardingsphere
- 🌟New Module base-model
- 🔥Upgrade SpringBoot to 2.4.13
- 🔥Upgrade SpringCloud to 2020.5
- 🔥Upgrade SpringCloudAlibaba to 2021.1
- 🔥Upgrade Sa-Token to 1.30.0
- 🔥Upgrade FileManager to 1.2.0
- 🔥Upgrade HuTool to 5.8.1
- 🔥Upgrade SpringBootAdmin to 2.4.4
- 🚫Remove Online报表相关API

### 2.4.1 2022-05-07

- 🔨Refactor 拆分Service层和Mapper层提供底层调用能力
- 🔨Refactor 所有Jeecg包名迁移到公司包名（升级需要迁移）`破坏性更新`
- 🔨Refactor 分离监控模块

### 2.4.0 2022-04-20

- 🌟New Feature 新增函数防重复提交功能
- 🌟New Feature 接入自研easy-excel工具
- 🌟New Feature 接入非侵入式文档生成工具smart-doc
- 🔥Upgrade hutool to 5.7.22
- 🔥Upgrade dynamic-datasource to 3.5.1
- 🔥Upgrade sa-token to 1.29.0
- 🔨Refactor 【URL】API URL 大面积重构不兼容老版本 `破坏性更新`
- 🔨Refactor 【字典】合并查询SQL提升效率
- 🚫Remove knife4j(swagger)
- 🚫Remove commons-io

### 2.3.6 2022-01-25

- 🌟New Feature SysBaseAPI新增批量添加用户
- 🔥Upgrade mybatis-plus to 3.5.1
- 🔥Upgrade hutool to 5.7.20
- 🔥Upgrade logback to 1.2.10
- 🔨Refactor 【字典】删除无用的函数，合并重复的SQL
- 🔨Refactor @Dict注解：code修改为value
- 🚫Remove auto-poi
- 🚫Remove commons-beanutils 版本依赖定义

### 2.3.5 2022-01-04

- 🐞Fix [B级模块60余项Bug](http://25.30.9.197:8080/browse/MFPT-113?jql=project%20%3D%20MFPT%20AND%20fixVersion%20%3D%20%22V2.3.0%22)
- 🐞Fix MyBatis拦截器未处理非Web上下文的情况
- 🐞Fix 多部门下（部门没有上下级关系），我的部门关键词搜索部门失效问
- 🐞Fix 完善枚举组件
- 🐞Fix 通讯录返回数据数量异常的问题
- 🐞Fix 数据字典查询都加入Redis缓存
- 🐞Fix 数据字典查询Key为Null时不翻译
- 🐞Fix 严重 用户再多部门的情况下选择部门会导致密码和密钥泄露（前端也有修改）
- 🐞Fix 登录接口和选择部门接口没有统一orgCode和orgCodeTxt的问题
- 🐞Fix 未登陆的情况下使用@AutoLog报错的问题
- 🔥Upgrade mybatis-plus to 3.5.0
- 🔥Upgrade hutool to 5.7.18
- 🔥Upgrade redisson to 3.16.7
- 🔥Upgrade auto-poi to 1.3.6
- ✅Enhancement 支持List字典翻译
- ✅Enhancement 验证码登录：支持4种样式验证码(动态图形干扰/线段干扰/圆圈干扰/扭曲干扰)，支持四则运算
- ✅Enhancement 新增可自定义初始化MyBatis-Plus插件
- 🚫Remove Minio上传组件（YML配置文件有变更）
- 🚫Remove freemarker
- 🚫Remove commons-beanutils

### 2.3.4 2021-12-15

- 🔥Upgrade cube-codegen to 1.2.5

### 2.3.3 2021-11-26

- 🌟New Feature Validator 全局参数异常处理
- 🌟New Feature SaToken 全局异常处理
- 🔨Refactor 此版本后所有登录凭证失效等API不再返回http status 500

### 2.3.2 2021-11-24

- 🌟New Feature 新增字典支持查询条件，加载字典值时忽略条件
- 🐞Fix 无法使用SaToken注解进行接口API鉴权
- 🐞Fix 修复条件构造器没有转义%和_关键字

### 2.3.1 2021-11-19

- 🐞Fix 修复Spring循环引用的bug
- 🔥Upgrade cube-codegen to 1.2.3
- 🔨Refactor pom
- 🔨Refactor 移除cube-starter

### 2.3.0 2021-10-27

- 🌟New Feature Shiro 迁移到 Sa-Token
- 🔥Upgrade cube-codegen to 1.2.2
- 🔨Refactor 移除消息中心
- ✅Enhancement 优化字典翻译速度
- 🚫Remove 移除FastJson，改为Jackson和HuTool-json
- 🚫Remove 删除用户代理人等无用功能
- 🚫Remove 移除CAS单点登录

### 2.2.0 2021-09-08

- 🌟New Feature 数据库字段加密组件
- 🌟New Feature 新增Tomcat Native和APR支持
- ✅Enhancement 优化跨域配置

### 2.1.0 2021-08-04

- 🌟New Feature 登录密码加密传输
- 🌟New Feature 新增事件驱动（短信发送事件）
- 🔨Refactor 分离使用较少的模块(短信/OSS/任务调度)
- 🔨Refactor 分离任务调度改为可选依赖
- ✅Enhancement 优化验证码逻辑

### 更早……

- 🌟New Feature 新版代码生成器
- 🌟New Feature 枚举组件
- 🌟New Feature 接入积木报表
- 🌟New Feature 数据源改为Hikari
- 🔥Upgrade SpringBoot to 2.3.12.RELEASE
- 🔥Upgrade SpringCloud to Hoxton.SR12
- 🔨Refactor 分离第三方登录模块
- 。。。