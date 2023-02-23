# 魔方快速开发平台

### 版本: 3.0.x

### 运行和构建环境

- 运行环境：
    - JDK 1.8+
    - Maven 3.6.3+
    - MySQL 5.7+ / 达梦 8
    - Redis 6+
    - Node.js 14+
- 基础框架：
    - SpringBoot 2.6.14
    - MyBatis-Plus 3.5.3.1
    - Sa-Token 1.33.0
    - HuTool 5.8.11
    - SpringDoc 1.6.13
- 微服务框架:
    - SpringCloud 2021.0.4
        - Gateway
        - OpenFeign
        - CircuitBreaker
    - Spring Cloud Alibaba 2021.0.4.0
        - Nacos 1.4.2
        - Sentinel 1.8.3
        - Seata 1.4.2
    - Xxl-job 2.3.1
    - Redisson 3.17.7
    - ShardingSphere 5.1.2
    - SpringBootAdmin 2.6.9

### 项目结构说明

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
│  ├─cube-cloud-starters 微服务插件
│  │  ├─starter-cloud 微服务通用配置（FeignClients配置）
│  │  ├─starter-job 基于XXL-Job分布式任务调度
│  │  ├─starter-lock 分布式锁，高并发使用
│  │  ├─starter-rabbitmq 消息队列，功能解耦、流量削峰、异步处理
│  │  ├─starter-rocketmq 消息队列，功能解耦、流量削峰、异步处理
│  │  ├─starter-redis 对redis进行了封装，提供数据存储，发布订阅，过期Key监听等
│  │  ├─starter-seata 分布式事务
│  │  ├─starter-shardingsphere 分库分表
│  ├─cube-cloud-modules 微服务相关服务模块
│  │  ├─cube-cloud-gateway 微服务网关
│  │  ├─cube-cloud-monitor 微服务监控服务
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
│  │  ├─cube-codegen-ui 代码生成器模板
├─cube-web
│  ├─cube-block 魔方vue组件
│  ├─cube-skin 魔方皮肤定制方案
│  ├─cube-web-plugin（基于JavaScript）
│  │  ├─cube-chunk-uploader 支持多线程断点续传的上传组件
│  │  ├─cube-loading 页面loading过渡效果插件
```