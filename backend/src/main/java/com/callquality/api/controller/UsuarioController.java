package com.callquality.api.controller;

import com.callquality.api.model.Usuario;
import com.callquality.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @GetMapping
    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    // --- NOVO ENDPOINT: MINHA EQUIPE ---
    @GetMapping("/minha-equipe")
    public List<Usuario> listarMinhaEquipe() {
        // Pega o usuário logado do Token
        Usuario logado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Busca quem tem o ID dele como supervisor_id
        return repository.findBySupervisorId(logado.getId());
    }
}
