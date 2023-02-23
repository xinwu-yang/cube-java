# 使用说明

1. 引入依赖

```xml
<dependency>
    <groupId>com.tievd.cube.starter</groupId>
    <artifactId>starter-rabbitmq</artifactId>
</dependency>
```

2. 推送消息API介绍

参数名 |       参数描述        | 参数类型
:----------- |:-----------------:| -----------:
queueName | 队列名称(队列自动创建,无需手动) | String
handlerName |        参数         | 自定义消息处理器beanName
params |        参数         | Object
expiration |       延迟时间        | int(毫秒)

```
//立即发送 void sendMessage(String queueName, Object params)
//发送延时消息 void sendMessage(String queueName, Object params, Integer expiration)
//发送远程消息 void publishEvent(String handlerName, BaseMap params)
```

3. 编写示例（简单3步完成消息的发送和接收）

```
//注入消息发送客户端
@Autowired
private RabbitMqClient  rabbitMqClient;

//发送消息示例代码
BaseMap map = new BaseMap();
map.put("orderId", "12345");
rabbitMqClient.sendMessage("test", map);
//延迟10秒发送
//rabbitMqClient.sendMessage("test", map, 10000);

//编写消息监听监听器
//定义接收者（可以定义N个接受者，消息会均匀的发送到N个接收者中）
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import BaseRabbiMqHandler;
import MqListener;
import org.jeecg.common.annotation.RabbitComponent;
import org.jeecg.common.base.BaseMap;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;

@Slf4j
@RabbitListener(queues = "test3")
@RabbitComponent(value = "testListener3")
public class DemoRabbitMqListener3 extends BaseRabbiMqHandler<BaseMap> {

    @RabbitHandler
    public void onMessage(BaseMap baseMap, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        super.onMessage(baseMap, deliveryTag, channel, new MqListener<BaseMap>() {
            @Override
            public void handler(BaseMap map, Channel channel) {
                String orderId = map.get("orderId").toString();
                log.info("业务处理3：orderId:" + orderId);
            }
        });
    }
}

或者

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import BaseRabbiMqHandler;
import MqListener;
import org.jeecg.common.annotation.RabbitComponent;
import org.jeecg.common.base.BaseMap;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;

@Slf4j
@RabbitComponent(value = "testListener2")
public class DemoRabbitMqListener2 extends BaseRabbiMqHandler<BaseMap> {

    @RabbitListener(queues = "test2")
    public void onMessage(BaseMap baseMap, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        super.onMessage(baseMap, deliveryTag, channel, new MqListener<BaseMap>() {
            @Override
            public void handler(BaseMap map, Channel channel) {
                String orderId = map.get("orderId");
                log.info("业务处理2：orderId:" + orderId);
            }
        });
    }
}
```