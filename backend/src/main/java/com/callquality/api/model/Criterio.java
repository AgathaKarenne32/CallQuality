package com.callquality.api.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_criterio")
public class Criterio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descricao;

    @Column(name = "instrucao_ia", columnDefinition = "TEXT")
    private String instrucaoIa; // O prompt específico para a IA

    private Integer peso = 1;

    private Boolean ativo = true;
}
