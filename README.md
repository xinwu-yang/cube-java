# 魔方快速开发平台

### 平台介绍

魔方低代码开发平台围绕企业/行业应用快速开发为目标，提供JavaEE企业级应用的开发框架（“脚手架”功能），并实现企业级应用的通用基础功能（机构、用户、权限等），封装了大量技术、业务和UI组件，和基于E-R(实体关系）分析的代码自动生成工具，帮助用户实现应用快速开发。

### 版本: 3.0.x

### 运行和构建环境

- 运行环境：
    - JDK 1.8+
    - Maven 3.6.3+
    - MySQL 5.7+
    - Redis 6+
    - Node.js 14+
- 基础框架：
    - SpringBoot 2.7.17
    - MyBatis-Plus 3.5.4.1
    - Sa-Token 1.37.0
    - HuTool 5.8.22
    - SpringDoc 1.7.0
	
### 开发文档

- [后端开发文档](https://github.com/xinwu-yang/cube-java/wiki)

- [前端开发文档](https://github.com/xinwu-yang/cube-vue/wiki)

### Java代码结构说明

```text
├─cube-java 魔方平台总依赖管理Bom
│  ├─cube-system
│  │  ├─cube-system-api （系统管理模块：API层）
│  │  ├─cube-system-service （系统管理模块： Service层，WebSocket处理）
│  │  ├─cube-system-mapper （系统管理模块： Mapper层）
│  ├─cube-base
│  │  ├─system-cloud-api 微服务模块API
│  │  ├─system-local-api 单体服务模块基础API
│  │  ├─base-model 系统的Entity/DTO/VO
│  │  ├─base-core 通用配置和Model
│  │  ├─base-tools 通用工具类
│  │  ├─base-annotations 通用注解类
│  ├─cube-system-modules 基础cube-system拓展功能
│  │  ├─cube-system-enhance 地区管理，行为统计等拓展功能
│  │  ├─cube-workflow 工作流插件
│  │  ├─cube-system-job quartz任务调度功能
│  │  ├─cube-system-oss 阿里云OSS接入
│  │  ├─cube-system-sms 消息中心
│  │  ├─cube-system-third 第三方登录
│  │  ├─cube-system-monitor 系统监控（springboot，redis，mysql等）
│  ├─cube-plugins-starters 魔方功能插件（基于SpringBoot）
│  │  ├─sql-export 动态SQL导出插件
│  │  ├─sensitive-starter 字符串脱敏插件
│  │  ├─easy-excel-starter Excel导入导出插件
│  │  ├─file-manager-starter FTP、S3协议的上传下载以及授权插件
│  │  ├─crypto-starter 基于MyBatis-Plus的加密插件
│  │  ├─magic-map-spring-boot-starter 基于magic-api封装的通用地图API
│  ├─cube-codegen 代码生成器
│  │  ├─cube-codegen-engine 代码生成器引擎
│  │  │  ├─cube-codegen-annotations 设计实体类时用到的注解
│  │  │  ├─cube-codegen-core 核心框架
│  │  │  ├─cube-codegen-dbSync 数据库结构同步
│  │  │  ├─cube-codegen-web 开放的Web API
```