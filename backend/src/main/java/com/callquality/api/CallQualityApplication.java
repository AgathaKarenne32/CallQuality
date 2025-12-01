package com.callquality.api;

import com.callquality.api.model.*;
import com.callquality.api.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;
import java.util.Optional;

@EnableAsync
@SpringBootApplication
public class CallQualityApplication {

	public static void main(String[] args) {
		SpringApplication.run(CallQualityApplication.class, args);
	}

    @Bean
    public CommandLineRunner init(
            UsuarioRepository usuarioRepo, 
            PasswordEncoder passwordEncoder) {
        return args -> {
            // 1. CRIA ADMIN (Agatha)
            if (usuarioRepo.findByEmail("agatha@callquality.com").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNome("Agatha Admin");
                admin.setEmail("agatha@callquality.com");
                admin.setSenha(passwordEncoder.encode("123456"));
                admin.setPerfil(Perfil.ADMIN);
                usuarioRepo.save(admin);
                System.out.println("✅ SEED: Admin criado.");
            }

            // 2. CRIA SUPERVISOR (Carlos)
            Usuario supervisor;
            Optional<Usuario> supOpt = usuarioRepo.findByEmail("carlos@cq.com");
            if (supOpt.isEmpty()) {
                supervisor = new Usuario();
                supervisor.setNome("Chefe Carlos");
                supervisor.setEmail("carlos@cq.com");
                supervisor.setSenha(passwordEncoder.encode("123456"));
                supervisor.setPerfil(Perfil.SUPERVISOR);
                supervisor = usuarioRepo.save(supervisor);
                System.out.println("✅ SEED: Supervisor criado.");
            } else {
                supervisor = supOpt.get();
                // Garante a senha certa caso esteja quebrada
                supervisor.setSenha(passwordEncoder.encode("123456")); 
                usuarioRepo.save(supervisor);
            }

            // 3. CRIA ANALISTA (João) VINCULADO AO CARLOS
            Usuario analista;
            Optional<Usuario> anaOpt = usuarioRepo.findByEmail("joao@cq.com");
            if (anaOpt.isEmpty()) {
                analista = new Usuario();
                analista.setNome("João Analista");
                analista.setEmail("joao@cq.com");
                analista.setSenha(passwordEncoder.encode("123456"));
                analista.setPerfil(Perfil.ANALISTA);
                analista.setSupervisor(supervisor); // <--- VINCULO AQUI
                usuarioRepo.save(analista);
                System.out.println("✅ SEED: Analista criado e vinculado.");
            } else {
                analista = anaOpt.get();
                analista.setSupervisor(supervisor); // Garante o vínculo
                analista.setSenha(passwordEncoder.encode("123456")); // Garante a senha
                usuarioRepo.save(analista);
            }
        };
    }

    // Configuração de CORS
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("*")
						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
			}
		};
	}
}
