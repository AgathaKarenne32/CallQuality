package com.callquality.api.controller;

import com.callquality.api.model.Usuario;
import com.callquality.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        // Isso vai fazer um "SELECT * FROM tb_usuario" automaticamente!
        return repository.findAll();
    }
}
