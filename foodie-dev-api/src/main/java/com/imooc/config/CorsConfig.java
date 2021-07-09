package com.imooc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@Configuration
public class CorsConfig {
    public CorsConfig(){}

    @Bean
    public CorsFilter corsFilter(){
        // 1. 添加 cors 配置信息
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:8080");
        config.addAllowedOrigin("http://82.157.175.25:8080");
//        config.addAllowedOrigin("http://49.233.188.8:8080");
//        config.addAllowedOrigin("http://shop.z.mukewang.com:8080");
//        config.addAllowedOrigin("http://center.z.mukewang.com:8080");
//        config.addAllowedOrigin("http://shop.z.mukewang.com");
//        config.addAllowedOrigin("http://center.z.mukewang.com");
//        config.addAllowedOrigin("*");

        // 设置是否发送 cookie 信息
        config.setAllowCredentials(true);

        // 设置允许请求的方式
        config.addAllowedMethod("*");

        // 设置允许的 header
        config.addAllowedHeader("*");

        // 2. 为 url 添加映射路径
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**", config);

        // 3. 返回定义好的 corsSource
        return new CorsFilter(corsSource);
    }

}
