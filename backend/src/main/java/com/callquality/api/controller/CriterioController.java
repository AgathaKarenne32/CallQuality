package com.callquality.api.controller;

import com.callquality.api.model.Criterio;
import com.callquality.api.repository.CriterioRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    // Vamos deixar preparado um método para criar critérios novos também!
    @PostMapping
    public Criterio criar(@RequestBody Criterio criterio) {
        return repository.save(criterio);
    }
}
