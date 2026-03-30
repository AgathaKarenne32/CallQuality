package com.callquality.api.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tb_log_uso_ia")
public class LogUsoIA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ligacao_id")
    private Ligacao ligacao;

    @Column(name = "servico_usado", length = 50)
    private String servicoUsado; 

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_operacao")
    private TipoOperacaoIA tipoOperacao;

    @Column(name = "tokens_entrada")
    private Integer tokensEntrada = 0;

    @Column(name = "tokens_saida")
    private Integer tokensSaida = 0;

    @Column(name = "custo_estimado_usd", precision = 10, scale = 6)
    private BigDecimal custoEstimadoUsd;

    @Column(name = "tempo_processamento_ms")
    private Long tempoProcessamentoMs;

    @Column(name = "data_uso")
    private LocalDateTime dataUso = LocalDateTime.now();
}