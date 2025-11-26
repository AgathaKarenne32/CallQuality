package com.callquality.api.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "tb_avaliacao")
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "ligacao_id", nullable = false)
    private Ligacao ligacao;

    @ManyToOne
    @JoinColumn(name = "supervisor_id")
    private Usuario supervisor;

    @Column(name = "nota_final")
    private BigDecimal notaFinal;

    @Column(name = "feedback_geral", columnDefinition = "TEXT")
    private String feedbackGeral;

    @Enumerated(EnumType.STRING)
    @Column(name = "origem_avaliacao")
    private OrigemAvaliacao origemAvaliacao;

    @Column(name = "data_avaliacao")
    private LocalDateTime dataAvaliacao = LocalDateTime.now();

    @OneToMany(mappedBy = "avaliacao", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ItemAvaliacao> itens;
}
