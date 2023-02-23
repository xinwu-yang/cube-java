package org.cube.application.config;

import org.cube.application.config.properties.TomcatNativeAprProperties;
import org.apache.catalina.Context;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TomcatNativeAprProperties.class)
public class TomcatConfig {

    private final static String APR_PROTOCOL = "org.apache.coyote.http11.Http11AprProtocol";

    @Autowired
    private TomcatNativeAprProperties tomcatNativeAprProperties;

    /**
     * tomcat-embed-jasper引用后提示jar找不到的问题
     */
    @Bean
    public TomcatServletWebServerFactory tomcatFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                ((StandardJarScanner) context.getJarScanner()).setScanManifest(false);
            }
        };
        if (tomcatNativeAprProperties.isEnableTomcatNative()) {
            factory.setProtocol(APR_PROTOCOL);
        }
        factory.addConnectorCustomizers(connector -> {
            connector.setProperty("relaxedPathChars", "[]{}");
            connector.setProperty("relaxedQueryChars", "[]{}");
        });
        return factory;
    }
}
