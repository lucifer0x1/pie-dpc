package com.pie.dpc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/8 20:07
 * @Description TODO : swagger 配置类
 **/

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createApi(){
        return  new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(buildApiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.pie.dpc.controller"))
                .paths(PathSelectors.any()).build();
    }

    private ApiInfo buildApiInfo(){
        return new ApiInfoBuilder()
                .title("数据采集客户端API接口文档")
                .contact(new Contact("lucifer","www.xybug.com","wangxiyue.xy@163.com"))
                .description("客户端接口API描述")
                .version("1.0-dev").build();
    }
}
