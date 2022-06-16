package com.pie.dpc.server.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * ClassName CorsConfig
 * Description
 * Date 2022/5/24
 * Author wangxiyue.xy@163.com
 * 增加跨域支持
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET","POST")
                .allowedHeaders("*")
                .maxAge(3600);// 3600 =  1 小时
    }


}
