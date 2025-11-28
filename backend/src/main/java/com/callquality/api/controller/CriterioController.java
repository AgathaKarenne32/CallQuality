package com.callquality.api.controller;

import com.callquality.api.model.Criterio;
import com.callquality.api.repository.CriterioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/criterios")
public class CriterioController {

    @Autowired
    private CriterioRepository repository;

    @GetMapping
    public List<Criterio> listarTodos() {
        return repository.findAll();
    }

    @PostMapping
    // AQUI ESTÁ A TRANCA 🔒
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERVISOR')")
    public Criterio criar(@RequestBody Criterio criterio) {
        return repository.save(criterio);
    }
}
