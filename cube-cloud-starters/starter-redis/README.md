# 使用说明

1. 引入依赖

```xml
<dependency>
    <groupId>com.tievd.cube.starter</groupId>
    <artifactId>starter-redis</artifactId>
</dependency>
```

2. 推送消息API介绍

参数名 |            参数描述             | 参数类型
:----------- |:---------------------------:| -----------:
handlerName |        消息监听beanName         | String
params | 业务参数，注意：参数名不要设置为handlerName | BaseMap

```
@Autowired 
CubeRedisClient redisClient;

//发布消息
redisClient.sendMessage(String handlerName, BaseMap params)
```

3. 编写示例（简单3步完成消息的发送和接收）

```
//注入消息发送客户端
@Autowired
private CubeRedisClient redisClient;

//发送消息示例代码
BaseMap params = new BaseMap ();
params.put("orderId", "12345");
redisClient.sendMessage("demoHandler", params);

//编写消息监听监听器
//定义接收者实现 CubeRedisListerer 接口
@Slf4j
@Component
public class DemoHandler implements CubeRedisListerer {

    @Override
    public void onMessage(BaseMap message) {
        String orderId = message.get("orderId").toString();
        log.info("执行业务逻辑..............");
    }
}
```