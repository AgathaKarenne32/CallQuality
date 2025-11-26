package com.callquality.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableAsync
@SpringBootApplication
public class CallQualityApplication {

	public static void main(String[] args) {
		SpringApplication.run(CallQualityApplication.class, args);
	}

    // --- CONFIGURAÇÃO DE SEGURANÇA (CORS) EMBUTIDA ---
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**") // Libera todas as rotas
						.allowedOrigins("*") // Libera para qualquer site (Frontend)
						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
			}
		};
	}
}
