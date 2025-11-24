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

    @Async
    public void iniciarAnalise(Long ligacaoId) {
        try {
            atualizarStatus(ligacaoId, StatusProcessamento.TRANSCRICAO_EM_ANDAMENTO);
            System.out.println("🎙️ [IA] Transcrevendo ligação " + ligacaoId + "...");
            Thread.sleep(2000); 

            atualizarStatus(ligacaoId, StatusProcessamento.ANALISE_IA_EM_ANDAMENTO);
            System.out.println("🧠 [IA] Calculando notas e regras...");
            Thread.sleep(2000); 

            // Finaliza a Ligação
            Ligacao ligacao = ligacaoRepository.findById(ligacaoId).get();
            ligacao.setTranscricaoCompleta("Atendente: Olá. Cliente: Cancelar. Atendente: Ok.");
            ligacao.setSentimento(Sentimento.NEUTRO);
            ligacao.setStatus(StatusProcessamento.CONCLUIDO);
            ligacaoRepository.save(ligacao);
            
            // --- GERA A AVALIAÇÃO AUTOMÁTICA ---
            gerarAvaliacaoFake(ligacao);

            System.out.println("✅ [IA] Processamento concluído com SUCESSO!");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void gerarAvaliacaoFake(Ligacao ligacao) {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setLigacao(ligacao);
        
        // CORREÇÃO AQUI: Usando o Enum
        avaliacao.setOrigemAvaliacao(OrigemAvaliacao.IA);
        
        avaliacao.setFeedbackGeral("O atendente seguiu o script.");
        avaliacao.setNotaFinal(new BigDecimal("85.0")); 
        
        avaliacao = avaliacaoRepository.save(avaliacao);
        
        List<Criterio> criterios = criterioRepository.findAll();
        
        for (Criterio crit : criterios) {
            ItemAvaliacao item = new ItemAvaliacao();
            item.setAvaliacao(avaliacao);
            item.setCriterio(crit);
            item.setNomeCriterioSnapshot(crit.getDescricao());
            item.setPesoSnapshot(crit.getPeso());
            item.setCumpriuRequisito(true);
            item.setNotaAtribuida(new BigDecimal("10.0"));
            item.setJustificativaIa("Conforme regra.");
            
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
