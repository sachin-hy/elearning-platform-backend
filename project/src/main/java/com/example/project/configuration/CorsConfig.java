package com.example.project.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

//	@Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**") 
//                        .allowedOrigins("https://elearning-platform-frontend-rho.vercel.app") 
//                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS" ,"PATCH") 
//                        .allowedHeaders("*") 
//                        .allowCredentials(true); 
//            }
//        };
//    }
}
