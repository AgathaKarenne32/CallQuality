package com.callquality.api.config;

import com.callquality.api.repository.UsuarioRepository;
import com.callquality.api.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var token = recuperarToken(request);

        if (token != null) {
            var emailSubject = tokenService.validarToken(token);

            if (!emailSubject.isEmpty()) {
                var usuario = usuarioRepository.findByEmail(emailSubject).orElseThrow();

                // Cria a autenticação do Spring (considerando todos como user comum por
                // enquanto)
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, Collections.emptyList());

                // Salva no contexto "O usuário está logado!"
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Segue o fluxo (vai para o Controller ou é barrado se não tiver autenticação)
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null)
            return null;
        return authHeader.replace("Bearer ", "");
    }
}
