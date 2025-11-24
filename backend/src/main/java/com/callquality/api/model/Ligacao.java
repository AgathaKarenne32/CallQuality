package com.callquality.api.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tb_ligacao")
public class Ligacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento: Uma ligação pertence a um Analista (Usuario)
    @ManyToOne
    @JoinColumn(name = "analista_id", nullable = false)
    private Usuario analista;

    @Column(name = "nome_arquivo_original")
    private String nomeArquivoOriginal;

    @Column(name = "bucket_path", nullable = false)
    private String bucketPath; // Onde o arquivo está salvo

    @Column(name = "duracao_segundos")
    private Integer duracaoSegundos;

    @Column(name = "tamanho_bytes")
    private Long tamanhoBytes;

    @Column(name = "data_atendimento", nullable = false)
    private LocalDateTime dataAtendimento;

    @Column(name = "cliente_identificador")
    private String clienteIdentificador;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_processamento")
    private StatusProcessamento status = StatusProcessamento.PENDENTE;

    @Column(name = "mensagem_erro", columnDefinition = "TEXT")
    private String mensagemErro;

    @Column(name = "transcricao_completa", columnDefinition = "LONGTEXT")
    private String transcricaoCompleta;

    @Enumerated(EnumType.STRING)
    @Column(name = "sentimento_predominante")
    private Sentimento sentimento = Sentimento.NEUTRO;

    @Column(name = "data_upload", updatable = false)
    private LocalDateTime dataUpload = LocalDateTime.now();
}
