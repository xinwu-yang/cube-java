package org.cube.modules.system;

import cn.hutool.core.net.NetUtil;
import lombok.extern.slf4j.Slf4j;
import org.cube.application.CubeApp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@Slf4j
@CubeApp
public class CubeSystemApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        ConfigurableApplicationContext application = SpringApplication.run(CubeSystemApplication.class, args);
        String ip = NetUtil.getLocalhostStr();
        Environment env = application.getEnvironment();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        log.info("----------------------------------------------------------");
        log.info("Application cube-system is running! Access URLs:");
        log.info("Local: http://localhost:{}{}/", port, path);
        log.info("External: http://{}:{}{}/", ip, port, path);
        log.info("魔方开发文档: https://github.com/xinwu-yang/cube-java/wiki");
        log.info("API文档: http://{}:{}{}/api-docs", ip, port, path);
        log.info("----------------------------------------------------------");
    }
}
