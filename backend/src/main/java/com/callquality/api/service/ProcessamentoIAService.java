package com.callquality.api.service;

import com.callquality.api.model.*;
import com.callquality.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

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
    private HuggingFaceService iaReal; // <--- Nossa nova integração

    @Async
    public void iniciarAnalise(Long ligacaoId) {
        try {
            atualizarStatus(ligacaoId, StatusProcessamento.TRANSCRICAO_EM_ANDAMENTO);
            System.out.println("🎙️ [Mock] Transcrevendo ligação " + ligacaoId + "...");
            Thread.sleep(2000);

            // Vamos usar um texto fixo que varia um pouco para testar a IA
            // Num futuro passo, isso viria do Whisper
            String textoTranscrito = "O cliente estava muito irritado e reclamou do serviço. O atendente tentou acalmar mas não conseguiu.";
            if (ligacaoId % 2 == 0) {
                textoTranscrito = "O atendimento foi excelente! O problema foi resolvido rapidamente e o cliente agradeceu muito.";
            }

            atualizarStatus(ligacaoId, StatusProcessamento.ANALISE_IA_EM_ANDAMENTO);
            System.out.println("🧠 [HuggingFace] Enviando texto para análise real...");

            // --- CHAMADA REAL PARA A API ---
            String sentimentoReal = iaReal.analisarSentimento(textoTranscrito);
            System.out.println("🤖 IA Decidiu: " + sentimentoReal);

            // Salva no banco
            Ligacao ligacao = ligacaoRepository.findById(ligacaoId).get();
            ligacao.setTranscricaoCompleta(textoTranscrito);

            // Converte String para Enum (com segurança)
            try {
                ligacao.setSentimento(Sentimento.valueOf(sentimentoReal));
            } catch (Exception e) {
                ligacao.setSentimento(Sentimento.NEUTRO);
            }

            ligacao.setStatus(StatusProcessamento.CONCLUIDO);
            ligacaoRepository.save(ligacao);

            // Gera avaliação baseada no sentimento real
            gerarAvaliacao(ligacao, sentimentoReal);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void gerarAvaliacao(Ligacao ligacao, String sentimento) {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setLigacao(ligacao);
        avaliacao.setOrigemAvaliacao(OrigemAvaliacao.IA);

        // Nota baseada no sentimento real da IA
        if ("POSITIVO".equals(sentimento)) {
            avaliacao.setNotaFinal(new BigDecimal("95.0"));
            avaliacao.setFeedbackGeral("IA detectou alta satisfação no texto.");
        } else if ("NEGATIVO".equals(sentimento)) {
            avaliacao.setNotaFinal(new BigDecimal("40.0"));
            avaliacao.setFeedbackGeral("IA detectou insatisfação ou conflito.");
        } else {
            avaliacao.setNotaFinal(new BigDecimal("75.0"));
            avaliacao.setFeedbackGeral("Atendimento padrão, sem destaques.");
        }

        avaliacao = avaliacaoRepository.save(avaliacao);

        // Itens (Simplificado para o exemplo)
        List<Criterio> criterios = criterioRepository.findAll();
        for (Criterio crit : criterios) {
            ItemAvaliacao item = new ItemAvaliacao();
            item.setAvaliacao(avaliacao);
            item.setCriterio(crit);
            item.setNomeCriterioSnapshot(crit.getDescricao());
            item.setPesoSnapshot(crit.getPeso());
            item.setCumpriuRequisito(!sentimento.equals("NEGATIVO"));
            item.setNotaAtribuida(new BigDecimal("10.0"));
            item.setJustificativaIa("Análise automática via LLM.");
            itemRepository.save(item);
        }
    }

    private void atualizarStatus(Long id, StatusProcessamento status) {
        Ligacao lig = ligacaoRepository.findById(id).orElse(null);
        if (lig != null) {
            lig.setStatus(status);
            ligacaoRepository.save(lig);
        }
    }
}
