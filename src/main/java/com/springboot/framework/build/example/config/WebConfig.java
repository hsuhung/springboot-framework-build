package com.springboot.framework.build.example.config;

import com.springboot.framework.build.example.handler.InterceptorHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.spring.web.SpringfoxWebMvcConfiguration;

/**************************************************************
 * 创建日期：2020/1/19 10:05
 * 作    者：lixuhong
 * 功能描述：Web Mvc 配置
 **************************************************************/
@Configuration
@ConditionalOnClass(SpringfoxWebMvcConfiguration.class) // 当 classpath中存在某类时满足条件
public class WebConfig implements WebMvcConfigurer {

    // 拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptorHandler())
        .addPathPatterns("/**");
    }

    @Bean
    InterceptorHandler interceptorHandler(){
        return new InterceptorHandler();
    }
}
