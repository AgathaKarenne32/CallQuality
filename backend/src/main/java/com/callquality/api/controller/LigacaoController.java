package com.callquality.api.controller;

import com.callquality.api.model.Ligacao;
import com.callquality.api.model.Usuario;
import com.callquality.api.repository.LigacaoRepository;
import com.callquality.api.repository.UsuarioRepository;
import com.callquality.api.service.ProcessamentoIAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/ligacoes")
public class LigacaoController {

    @Autowired
    private LigacaoRepository repository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProcessamentoIAService iaService; // <--- Injetamos o serviço

    @GetMapping
    public List<Ligacao> listarTodas() {
        return repository.findAll();
    }

    @PostMapping("/upload")
    public Ligacao upload(@RequestParam("arquivo") MultipartFile arquivo, 
                          @RequestParam("idAnalista") Long idAnalista) {
        
        Usuario analista = usuarioRepository.findById(idAnalista)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Analista não encontrado"));

        Ligacao novaLigacao = new Ligacao();
        novaLigacao.setAnalista(analista);
        novaLigacao.setNomeArquivoOriginal(arquivo.getOriginalFilename());
        novaLigacao.setTamanhoBytes(arquivo.getSize());
        String caminhoFalso = "s3://callquality-bucket/" + System.currentTimeMillis() + "_" + arquivo.getOriginalFilename();
        novaLigacao.setBucketPath(caminhoFalso);
        novaLigacao.setDataAtendimento(LocalDateTime.now());
        novaLigacao.setDuracaoSegundos(180); 
        novaLigacao.setClienteIdentificador("CLIENTE-MOCK");

        Ligacao salva = repository.save(novaLigacao);

        // Dispara o processo assíncrono (Fire and Forget)
        iaService.iniciarAnalise(salva.getId());

        return salva;
    }
}
