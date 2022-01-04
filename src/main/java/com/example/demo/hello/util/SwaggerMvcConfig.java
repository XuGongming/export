/*
 *  Copyright@2019 清云智通（北京）科技有限公司 保留所有权利
 */
package com.example.demo.hello.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置 swagger 能访问
 *
 * @author wangxt@tsingyun.net
 * @date 2020/6/2 10:55
 */
@Configuration
public class SwaggerMvcConfig implements WebMvcConfigurer {


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/META-INF/resources/templates/");
    }


}