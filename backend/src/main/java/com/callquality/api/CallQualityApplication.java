package com.callquality.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync // <--- Habilita o processamento em segundo plano
@SpringBootApplication
public class CallQualityApplication {

	public static void main(String[] args) {
		SpringApplication.run(CallQualityApplication.class, args);
	}

}
