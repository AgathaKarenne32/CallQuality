package com.callquality.api.service;

import com.callquality.api.model.*;
import com.callquality.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Service
public class ProcessamentoIAService {

    @Autowired
    private LigacaoRepository ligacaoRepository;

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private CriterioRepository criterioRepository;

    @Autowired
    private ItemAvaliacaoRepository itemRepository;

    @Autowired
    private HuggingFaceService iaReal;

    @Autowired
    private S3Client s3Client;

    @Value("${storage.s3.bucket-name}")
    private String bucketName;

    @Value("${openai.api.key}")
    private String openAiKey;

    @Async
    public void iniciarAnalise(Long ligacaoId) {
        try {
            Ligacao ligacao = ligacaoRepository.findById(ligacaoId)
                    .orElseThrow(() -> new RuntimeException("Ligação não encontrada"));

            atualizarStatus(ligacaoId, StatusProcessamento.TRANSCRICAO_EM_ANDAMENTO);

            // 1. RECUPERAR ÁUDIO DO MINIO
            InputStream audioStream = s3Client.getObject(GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(ligacao.getBucketPath())
                    .build());

            // 2. CHAMADA AO WHISPER (Transcrição Real)
            String textoTranscrito = transcreverComWhisper(audioStream, ligacao.getNomeArquivoOriginal());

            atualizarStatus(ligacaoId, StatusProcessamento.ANALISE_IA_EM_ANDAMENTO);

            // 3. ANÁLISE DE SENTIMENTO (Via Groq/Llama)
            String sentimentoReal = iaReal.analisarSentimento(textoTranscrito);

            ligacao.setTranscricaoCompleta(textoTranscrito);
            try {
                ligacao.setSentimento(Sentimento.valueOf(sentimentoReal));
            } catch (Exception e) {
                ligacao.setSentimento(Sentimento.NEUTRO);
            }

            ligacao.setStatus(StatusProcessamento.CONCLUIDO);
            ligacaoRepository.save(ligacao);

            // 4. GERAR AVALIAÇÃO COM CÁLCULO PONDERADO
            gerarAvaliacao(ligacao, sentimentoReal);

        } catch (Exception e) {
            System.err.println("❌ Erro no processamento: " + e.getMessage());
            atualizarStatus(ligacaoId, StatusProcessamento.ERRO);
        }

        @Autowired
        private SimpMessagingTemplate messagingTemplate;

        messagingTemplate.convertAndSend("/topic/analise", "Concluído: " + ligacaoId);
    }

    private String transcreverComWhisper(InputStream audio, String fileName) {
        if (openAiKey.contains("chave-nao-configurada")) {
            return "[Mock] Transcrição simulada: O atendimento foi produtivo e o problema resolvido.";
        }

        RestClient restClient = RestClient.builder().build();
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("file", new InputStreamResource(audio)).filename(fileName);
        bodyBuilder.part("model", "whisper-1");

        Map response = restClient.post()
                .uri("https://api.openai.com/v1/audio/transcriptions")
                .header("Authorization", "Bearer " + openAiKey)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(bodyBuilder.build())
                .retrieve()
                .body(Map.class);

        return (String) response.get("text");
    }

    private void gerarAvaliacao(Ligacao ligacao, String sentimento) {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setLigacao(ligacao);
        avaliacao.setOrigemAvaliacao(OrigemAvaliacao.IA);

        List<Criterio> criterios = criterioRepository.findByAtivoTrue();
        BigDecimal somaNotasPesadas = BigDecimal.ZERO;
        int somaPesos = 0;

        avaliacao = avaliacaoRepository.save(avaliacao);

        for (Criterio crit : criterios) {
            ItemAvaliacao item = new ItemAvaliacao();
            item.setAvaliacao(avaliacao);
            item.setCriterio(crit);
            item.setNomeCriterioSnapshot(crit.getDescricao());
            item.setPesoSnapshot(crit.getPeso());

            boolean cumpriu = !sentimento.equals("NEGATIVO");
            item.setCumpriuRequisito(cumpriu);
            BigDecimal notaItem = cumpriu ? new BigDecimal("10.0") : BigDecimal.ZERO;
            item.setNotaAtribuida(notaItem);
            item.setJustificativaIa("Análise baseada no sentimento predominante.");
            
            itemRepository.save(item);

            somaNotasPesadas = somaNotasPesadas.add(notaItem.multiply(new BigDecimal(crit.getPeso())));
            somaPesos += crit.getPeso();
        }

        if (somaPesos > 0) {
            avaliacao.setNotaFinal(somaNotasPesadas.divide(new BigDecimal(somaPesos), 2, RoundingMode.HALF_UP));
            avaliacaoRepository.save(avaliacao);
        }
    }

    private void atualizarStatus(Long id, StatusProcessamento status) {
        ligacaoRepository.findById(id).ifPresent(lig -> {
            lig.setStatus(status);
            ligacaoRepository.save(lig);
        });
    }
}

LogUsoIA log = new LogUsoIA();
log.setLigacao(ligacao);
log.setServicoUsado("OpenAI-Whisper / Groq-Llama3");
log.setTokensEntrada(tokensIn); 
log.setTokensSaida(tokensOut);
log.setCustoEstimadoUsd(calculaCusto(tokensIn, tokensOut));
logIARepository.save(log);