# 使用说明

1. 引入依赖

```xml
<dependency>
    <groupId>com.tievd.cube.starter</groupId>
    <artifactId>starter-lock</artifactId>
</dependency>
```

2. 配置（支持单机、哨兵、集群、主从4种redis部署方式）

```yaml
cube:
  #分布式锁配置-单机
  redisson:
    address: 127.0.0.1:6379
    password:
    type: STANDALONE
    enabled: true
```

```yaml
cube:
  #分布式锁配置-哨兵
  redisson:
    address: my-sentinel-name,127.0.0.1:26379,127.0.0.1:26389,127.0.0.1:26399
    password:
    type: SENTINEL
    enabled: true
```

```yaml
cube:
  #分布式锁配置-集群
  redisson:
    address: 127.0.0.1:6379,127.0.0.1:6380,127.0.0.1:6381,127.0.0.1:6382,127.0.0.1:6383,127.0.0.1:6384
    password:
    type: CLUSTER
    enabled: true
```

```yaml
cube:
  #分布式锁配置-主从
  redisson:
    address: 127.0.0.1:6379,127.0.0.1:6380,127.0.0.1:6381
    password:
    type: MASTERSLAVE
    enabled: true
```

3. 注解介绍

```
@JLock 函数加锁注解
@JRepeat(lockKey = "#name", lockTime = 5) 同一个参数，5秒内函数不能执行多次
```

4. 编码

```java
/**
 * 分布式锁测试demo
 */
@Slf4j
@Component
public class DemoLockTest {

    @Autowired
    private RedissonLockClient redissonLock;

    /**
     *注解方式测试分布式锁
     */
    @Scheduled(cron = "0/5 * * * * ?")
    @JLock(lockKey = "redis-lock")
    public void execute() throws InterruptedException {
        log.info("执行execute任务开始，休眠三秒");
        Thread.sleep(3000);
        System.out.println("=======================业务逻辑1=============================");
        log.info("execute任务结束，休眠三秒");
    }

    /**
     * 编码方式测试分布式锁
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void execute2() throws InterruptedException {
        if (redissonLock.tryLock("redisson", -1, 10000)) {
            log.info("执行任务execute2开始，休眠三秒");
            Thread.sleep(3000);
            System.out.println("=======================业务逻辑2=============================");
            log.info("定时execute2结束，休眠三秒");
            redissonLock.unlock("redisson");
        } else {
            log.info("execute2获取锁失败");
        }
    }
}
```