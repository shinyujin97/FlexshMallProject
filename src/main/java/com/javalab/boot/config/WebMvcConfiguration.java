package com.javalab.boot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

    @Configuration
    public class WebMvcConfiguration implements WebMvcConfigurer {

   @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 노트북
        // 로컬 버전
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:///C:/JAVA/javaworks/spring-project/FlexshMallProject/files/");
    }

/*    // 클라우드 타입 버전
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:///app/files/");
    }*/
}

