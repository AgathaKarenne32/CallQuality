package com.callquality.api.controller;

import com.callquality.api.dto.LoginDTO;
import com.callquality.api.repository.UsuarioRepository;
import com.callquality.api.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints para login e gestão de tokens")
public class AuthController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @Operation(summary = "Realizar Login", description = "Retorna um Token JWT se as credenciais estiverem corretas.")
    public ResponseEntity login(@RequestBody LoginDTO dados) {
        var usuario = repository.findByEmail(dados.email())
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));

        if (!passwordEncoder.matches(dados.senha(), usuario.getSenha())) {
            return ResponseEntity.status(403).body("Credenciais inválidas");
        }

        var tokenJwt = tokenService.gerarToken(usuario);

        return ResponseEntity.ok(Map.of(
            "token", tokenJwt,
            "nome", usuario.getNome(),
            "perfil", usuario.getPerfil()
        ));
    }
}
