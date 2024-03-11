package com.example.mhcidemo.Config;


import com.example.mhcidemo.Interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Component
public class MyInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> pathPatterns=new ArrayList<>();
        pathPatterns.add("/jwt/parser");
        pathPatterns.add("/order/**");
        List<String> excludePathPatterns=new ArrayList<>();
        excludePathPatterns.add("/jwt/token");
        registry.addInterceptor(jwtInterceptor) //添加拦截器
                .addPathPatterns(pathPatterns) //添加拦截url
                .excludePathPatterns(excludePathPatterns); //添加不拦截url
    }

    @Bean
    public RestTemplate getRestTemplate()  {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }
}

