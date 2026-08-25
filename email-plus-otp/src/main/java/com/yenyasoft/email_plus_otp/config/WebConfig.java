package com.yenyasoft.email_plus_otp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Author: Subash
 * Created: 8/25/2026
 * File: WebConfig.java
 */

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                       "http://localhost:5173"
                )
                .allowedMethods(
                       "GET", "POST", "PUT", "DELETE", "PATCH"
                )
                .allowedHeaders("*");
    }
}

