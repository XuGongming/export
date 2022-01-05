/*
 *  Copyright@2019 清云智通（北京）科技有限公司 保留所有权利
 */
package com.example.demo.demo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 测试 扩展 common 中的 swagger 配置
 *
 * @author wangxt@tsingyun.net
 * @date 2019/12/18 10:53
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Value("${commons.swagger2.scanPackage:com.example.demo}")
    private String scanPackage;

    @Value("${commons.swagger2.title:swagger}")
    private String title;
    @Value("${commons.swagger2.description:swagger}")
    private String description;

    @Value("${commons.swagger2.version:1.0.0}")
    private String version;

    @Value("${commons.swagger2.enable:true}")
    private boolean enable;


    @Bean
    public Docket createRestApi() {

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(enable)
                .select()
                .apis(RequestHandlerSelectors.basePackage(scanPackage))
                .paths(PathSelectors.any())
                .build();
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .version(version)
                .build();
    }
}
