package org.cube.application.config;

import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.captcha.generator.MathGenerator;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.util.StrUtil;
import org.cube.plugin.easyexcel.dict.IDictTranslator;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.cube.application.config.properties.*;
import org.cube.commons.dict.DictTranslator;
import org.cube.commons.utils.captcha.CaptchaGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;

import java.awt.*;
import java.util.Map;

@Configuration
@Import(cn.hutool.extra.spring.SpringUtil.class)
@ComponentScan(basePackages = "cn.hutool.extra.spring")
@EnableConfigurationProperties({MyBatisPlusPluginProperties.class, CaptchaConfigProperties.class, ResourcesProperties.class, OrgCodeViewProperties.class, InterceptorProperties.class, SwaggerProperties.class})
public class CubeSystemConfig {
    // 默认
    private static final String DEFAULT_CODE = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ23456789";

    @Autowired
    private CaptchaConfigProperties captchaConfigProperties;
    @Autowired
    private SwaggerProperties swaggerProperties;

    @Bean
    @ConditionalOnProperty(name = "cube.login.captcha.math", havingValue = "false", matchIfMissing = true)
    public CodeGenerator randomGenerator() {
        String code = DEFAULT_CODE;
        if (StrUtil.isNotBlank(captchaConfigProperties.getCode())) {
            code = captchaConfigProperties.getCode();
        }
        return new RandomGenerator(code, 4);
    }

    @Bean
    @ConditionalOnProperty(name = "cube.login.captcha.math", havingValue = "true")
    public CodeGenerator mathGenerator() {
        return new MathGenerator();
    }

    @Bean
    @Scope("prototype")
    public CaptchaGenerator captchaGenerator(CodeGenerator codeGenerator) {
        CaptchaGenerator captchaGenerator = new CaptchaGenerator(captchaConfigProperties.getType(), codeGenerator);
        if (captchaConfigProperties.getRed() != null && captchaConfigProperties.getGreen() != null && captchaConfigProperties.getBlue() != null) {
            Color color = new Color(captchaConfigProperties.getRed(), captchaConfigProperties.getGreen(), captchaConfigProperties.getBlue());
            captchaGenerator.setBackground(color);
        }
        return captchaGenerator;
    }

    @Bean
    @ConditionalOnMissingBean
    public IDictTranslator dictTranslator() {
        return new DictTranslator();
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenAPI openAPI() {
        OpenAPI openAPI = new OpenAPI();
        openAPI.info(new Info().title("Cube API")
                .description("魔方快速开发平台API")
                .version("v2.6.x")
                .license(new License()
                        .name("Apache 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0")));
        openAPI.externalDocs(new ExternalDocumentation().description("魔方开发文档").url("http://125.71.201.11:10086/"));
        openAPI.components(new Components().securitySchemes(swaggerProperties.getSecuritySchemes()));
        Map<String, SecurityScheme> securitySchemeMap = swaggerProperties.getSecuritySchemes();
        if (securitySchemeMap != null && securitySchemeMap.size() > 0) {
            securitySchemeMap.keySet().forEach(key -> openAPI.addSecurityItem(new SecurityRequirement().addList(key)));
        }
        return openAPI;
    }
}
