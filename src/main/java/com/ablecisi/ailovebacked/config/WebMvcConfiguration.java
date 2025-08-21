package com.ablecisi.ailovebacked.config;

import com.ablecisi.ailovebacked.interceptor.JwtTokenUserInterceptor;
import com.ablecisi.ailovebacked.json.JacksonObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * 配置类，注册web层相关组件
 */
@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {
    private final JwtTokenUserInterceptor jwtTokenUserInterceptor;

    @Autowired
    public WebMvcConfiguration(JwtTokenUserInterceptor jwtTokenUserInterceptor) {
        this.jwtTokenUserInterceptor = jwtTokenUserInterceptor;
    }

    /**
     * 注册自定义拦截器
     *
     * @param registry 拦截器注册对象
     */
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册用户端拦截器...");
        registry.addInterceptor(jwtTokenUserInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login");
    }

    /**
     * 扩展消息转换器
     *
     * @param converters 消息转换器列表
     */
    @Override
    protected void extendMessageConverters(@NonNull List<HttpMessageConverter<?>> converters) {
        log.info("开始扩展消息转换器...");
        // 创建一个消息转换器
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        // 需要为转换器设置一个对象映射器
        JacksonObjectMapper jacksonObjectMapper = new JacksonObjectMapper();
        // 设置对象映射器
        converter.setObjectMapper(jacksonObjectMapper);
        // 将转换器添加到转换器列表的第一个位置
        converters.addFirst(converter);
        log.info("扩展消息转换器完成");
    }
}
