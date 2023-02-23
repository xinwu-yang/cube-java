package org.cube.application.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.cube.application.config.condition.CrossFilterCondition;
import org.cube.application.config.properties.InterceptorProperties;
import org.cube.commons.base.BaseEnum;
import org.cube.commons.json.EnumSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.List;
import java.util.Map;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final String DEFAULT_INTERCEPTOR_NAME = "SaToken";

    @Autowired
    private InterceptorProperties interceptorProperties;

    @Autowired(required = false)
    private List<HandlerInterceptor> interceptors;

    @Autowired(required = false)
    private List<Formatter<?>> formatters;

    @Autowired(required = false)
    private List<Converter<?, ?>> converters;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        Map<String, InterceptorProperties.InterceptorConfig> configs = interceptorProperties.getConfigs();
        InterceptorProperties.InterceptorConfig defaultConfig;
        if (configs.containsKey(DEFAULT_INTERCEPTOR_NAME)) {
            defaultConfig = configs.get(DEFAULT_INTERCEPTOR_NAME);
        } else {
            defaultConfig = new InterceptorProperties.InterceptorConfig();
        }
        List<String> excludeUrls = defaultConfig.getExcludes();
        // 注册路由拦截器，自定义验证规则
        // 登录相关
        excludeUrls.add("/sys/randomImage/**");//登录验证码
        excludeUrls.add("/sys/checkCaptcha");//校验验证码
        excludeUrls.add("/sys/login");//登录
        excludeUrls.add("/sys/phoneLogin");//验证码登录
        excludeUrls.add("/sys/sms");//发送短信验证码
        excludeUrls.add("/sys/ram/sign");//签发登录凭证
        // 用户相关
        excludeUrls.add("/sys/user/checkOnlyUser");//校验用户是否存在
        excludeUrls.add("/sys/user/register");//用户注册
        excludeUrls.add("/sys/user/querySysUser");//根据手机号获取用户信息
        excludeUrls.add("/sys/user/phoneVerification");//用户忘记密码验证手机号
        excludeUrls.add("/sys/user/passwordChange");//用户重置密码
        excludeUrls.add("/sys/user/online/**");//用户心跳
        excludeUrls.add("/sys/thirdLogin/**");//第三方登录
        // 通用API
        excludeUrls.add("/sys/common/enumValueList");//枚举组件
        excludeUrls.add("/sys/common/static/**");//图片预览 &下载文件不限制token
        excludeUrls.add("/sys/common/error");//没有权限API
        excludeUrls.add("/sys/common/403");//没有权限API
        excludeUrls.add("/error/**");//让程序能够正常提示一些错误
        // 代码生成器
        excludeUrls.add("/codegen/**");
        // WebSocket排除
        excludeUrls.add("/websocket/**");//系统通知和公告
        // 性能监控
        excludeUrls.add("/actuator/**");//SpringBoot监控
        excludeUrls.add("/monitoring/**");//JavaMelody监控

        SaInterceptor saInterceptor = new SaInterceptor(handle -> StpUtil.checkLogin());
        registry.addInterceptor(saInterceptor).addPathPatterns(defaultConfig.getIncludes()).excludePathPatterns(excludeUrls);

        // 自定义拦截器注入
        if (interceptors != null && interceptors.size() > 0) {
            for (HandlerInterceptor interceptor : interceptors) {
                String name = interceptor.getClass().getSimpleName();
                if (configs.containsKey(name)) {
                    InterceptorProperties.InterceptorConfig config = configs.get(name);
                    registry.addInterceptor(interceptor).addPathPatterns(config.getIncludes()).excludePathPatterns(config.getExcludes());
                }
            }
        }
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        if (formatters != null && formatters.size() > 0) {
            for (Formatter<?> formatter : formatters) {
                registry.addFormatter(formatter);
            }
        }
        if (converters != null && converters.size() > 0) {
            for (Converter<?, ?> converter : converters) {
                registry.addConverter(converter);
            }
        }
    }

    /**
     * 跨域配置
     */
    @Bean
    @Conditional(CrossFilterCondition.class)
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        CorsFilter corsFilter = new CorsFilter(urlBasedCorsConfigurationSource);
        FilterRegistrationBean<CorsFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>(corsFilter);
        // 解决corsFilter被其他Filter提前拦截导致无法跨域的bug
        filterFilterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filterFilterRegistrationBean;
    }

    /**
     * Jackson序列化配置
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return jacksonObjectMapperBuilder -> {
            jacksonObjectMapperBuilder.serializerByType(Long.class, ToStringSerializer.instance);
            jacksonObjectMapperBuilder.serializerByType(Long.TYPE, ToStringSerializer.instance);
            jacksonObjectMapperBuilder.serializerByType(BaseEnum.class, EnumSerializer.instance);
            jacksonObjectMapperBuilder.failOnUnknownProperties(false);
            jacksonObjectMapperBuilder.simpleDateFormat("yyyy-MM-dd HH:mm:ss");
            jacksonObjectMapperBuilder.timeZone("GMT+8");
        };
    }

    /**
     * 注册HttpTrace
     */
    @Bean
    public InMemoryHttpTraceRepository getInMemoryHttpTrace() {
        return new InMemoryHttpTraceRepository();
    }

    /**
     * 自动注册使用了@ServerEndpoint注解声明的WebsocketEndpoint
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
