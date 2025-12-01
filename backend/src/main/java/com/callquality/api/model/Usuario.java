package com.callquality.api.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore; // Importante
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "tb_usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @JsonIgnore // Esconde a senha
    @Column(name = "senha_hash", nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Perfil perfil;

    private Boolean ativo = true;

    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    // --- HIERARQUIA ---
    
    @ManyToOne
    @JoinColumn(name = "supervisor_id")
    @JsonIgnore // Ignora o chefe na visualização
    private Usuario supervisor;

    @OneToMany(mappedBy = "supervisor", fetch = FetchType.LAZY)
    @JsonIgnore // <--- A CORREÇÃO: O Java NÃO vai tentar buscar a lista sozinho
    private List<Usuario> liderados;
}
