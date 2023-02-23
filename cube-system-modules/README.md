# 魔方系统组件

### 项目结构说明

```
├─cube-system-modules cube-system拓展功能
│  ├─cube-system-enhance 地区管理，行为统计等拓展功能 默认集成到cube-system中了
│  ├─cube-system-monitor Redis监控等功能 默认集成到cube-system中了
│  ├─cube-system-jimu 接入积木报表
│  ├─cube-system-job quartz任务调度功能
│  ├─cube-system-oss 阿里云OSS接入
│  ├─cube-system-sms 消息中心
│  ├─cube-system-third 接入第三方登录
```

### 使用说明

1. cube-system-job quartz任务调度功能

```xml
<!-- 包含定时任务相关API（基于Quartz） -->
<dependency>
    <groupId>com.tievd.cube</groupId>
    <artifactId>cube-system-job</artifactId>
</dependency>
```

```yaml
#quartz定时任务，采用数据库方式
spring:
  quartz:
    job-store-type: jdbc
    initialize-schema: embedded
    #设置自动启动，默认为 true
    auto-startup: true
    #启动时更新己存在的Job
    overwrite-existing-jobs: true
    properties:
      org:
        quartz:
          scheduler:
            instanceName: MyScheduler
            instanceId: AUTO
          jobStore:
            class: org.quartz.impl.jdbcjobstore.JobStoreTX
            driverDelegateClass: org.quartz.impl.jdbcjobstore.StdJDBCDelegate
            tablePrefix: QRTZ_
            isClustered: true
            misfireThreshold: 60000
            clusterCheckinInterval: 10000
          threadPool:
            class: org.quartz.simpl.SimpleThreadPool
            threadCount: 10
            threadPriority: 5
            threadsInheritContextClassLoaderOfInitializingThread: true
```

2. cube-system-oss 阿里云OSS模块

```xml

<dependency>
    <groupId>com.tievd.cube</groupId>
    <artifactId>cube-system-oss</artifactId>
</dependency>
```

```yaml
#阿里云oss存储配置
cube:
  oss:
    endpoint: oss-cn-beijing.aliyuncs.com
    accessKey: ??
    secretKey: ??
    bucketName: jeecgos
    staticDomain: ??
  # minio文件上传
  minio:
    url: http://25.30.9.158:9011
    name: minioadmin
    password: minioadmin
    bucket: test
```

3. cube-system-third 第三方登录

```xml

<dependency>
    <groupId>com.tievd.cube</groupId>
    <artifactId>cube-system-third</artifactId>
</dependency>
```

```yaml
#第三方登录
justauth:
  enabled: true
  type:
    GITHUB:
      client-id: ??
      client-secret: ??
      redirect-uri: http://sso.test.com:8080/cube/thirdLogin/github/callback
    WECHAT_ENTERPRISE:
      client-id: ??
      client-secret: ??
      redirect-uri: http://sso.test.com:8080/cube/thirdLogin/wechat_enterprise/callback
      agent-id: 1000002
    DINGTALK:
      client-id: ??
      client-secret: ??
      redirect-uri: http://sso.test.com:8080/cube/thirdLogin/dingtalk/callback
  cache:
    type: default
    prefix: 'demo::'
    timeout: 1h
```

4. cube-system-sms 消息中心（依赖cube-system-job）

- 消息模板功能
- 支持短信、邮件、自定义拓展的方式