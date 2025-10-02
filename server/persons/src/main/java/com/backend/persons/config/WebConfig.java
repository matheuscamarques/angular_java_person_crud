package com.backend.persons.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Aplica a configuração a todos os endpoints da API
                        .allowedOrigins("http://localhost:4200") // Permite requisições desta origem (seu frontend Angular)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos HTTP permitidos
                        .allowedHeaders("*") // Permite todos os cabeçalhos na requisição
                        .allowCredentials(true); // Permite o envio de cookies e credenciais de autenticação
            }
        };
    }
} 