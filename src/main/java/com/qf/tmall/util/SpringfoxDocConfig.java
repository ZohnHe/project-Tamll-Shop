package com.qf.tmall.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger -- API 文档工具
 * @author ACI
 * @Title: SpringfoxDocConfig
 * @ProjectName TestShop
 * @Description: TODO
 * @date 2019/1/16 20:12
 */
@Configuration
@EnableWebMvc
@EnableSwagger2
@ComponentScan(basePackages = { "com.qf.tmall.controller" })
public class SpringfoxDocConfig {

    boolean test = true;

    @Bean
    public Docket customDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.qf.tmall.controller"))
                .paths(PathSelectors.any()).build()
                .useDefaultResponseMessages(false).enable(test);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("后台系统接口文档").build();
    }
}

