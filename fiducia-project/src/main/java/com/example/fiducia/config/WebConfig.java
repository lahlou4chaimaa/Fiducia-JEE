package com.example.fiducia.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Cette ligne permet à Spring de servir les fichiers du dossier physique "uploads"
        // via l'URL http://localhost:8080/api/files/nom_du_fichier.pdf
        registry.addResourceHandler("/api/files/**")
                .addResourceLocations("file:uploads/");
    }
}