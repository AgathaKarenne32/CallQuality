package com.callquality.api.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore; // <--- Importante!
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "tb_item_avaliacao")
public class ItemAvaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore // <--- A CORREÇÃO: Quebra o loop infinito
    @ManyToOne
    @JoinColumn(name = "avaliacao_id", nullable = false)
    private Avaliacao avaliacao;

    @ManyToOne
    @JoinColumn(name = "criterio_id")
    private Criterio criterio;

    @Column(name = "nome_criterio_snapshot")
    private String nomeCriterioSnapshot;

    @Column(name = "peso_snapshot")
    private Integer pesoSnapshot;

    @Column(name = "cumpriu_requisito")
    private Boolean cumpriuRequisito;

    @Column(name = "nota_atribuida")
    private BigDecimal notaAtribuida;

    @Column(name = "justificativa_ia", columnDefinition = "TEXT")
    private String justificativaIa;
}
