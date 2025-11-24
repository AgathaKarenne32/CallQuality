package com.callquality.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teste")
public class TesteController {

    @GetMapping
    public String hello() {
        return "🚀 Backend CallQuality está RODANDO! O banco de dados conectou com sucesso.";
    }
}