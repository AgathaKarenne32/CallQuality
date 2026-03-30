package com.callquality.api.controller;

import com.callquality.api.model.Ligacao;
import com.callquality.api.model.Usuario;
import com.callquality.api.model.Perfil;
import com.callquality.api.repository.LigacaoRepository;
import com.callquality.api.repository.UsuarioRepository;
import com.callquality.api.service.ProcessamentoIAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private ProcessamentoIAService iaService;

    @GetMapping
    public List<Ligacao> listarTodas() {
        // 1. Descobre quem está logado
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 2. Aplica a Regra de Negócio #5
        if (usuarioLogado.getPerfil() == Perfil.ANALISTA) {
            // Se for analista, só vê as suas
            return repository.findByAnalistaId(usuarioLogado.getId());
        }

        if (usuarioLogado.getPerfil() == Perfil.SUPERVISOR) {
            return repository.findBySupervisorId(usuarioLogado.getId());
        }

        return repository.findAll(); // ADMIN vê tudo
    }

    @Autowired
private StorageService storageService; // Adicione esta dependência

@PostMapping("/upload")
public Ligacao upload(@RequestParam("arquivo") MultipartFile arquivo,
        @RequestParam("idAnalista") Long idAnalista) {

    Usuario analista = usuarioRepository.findById(idAnalista)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Analista não encontrado"));

    // UPLOAD REAL
    String nomeNoStorage = storageService.uploadFile(arquivo);

    Ligacao novaLigacao = new Ligacao();
    novaLigacao.setAnalista(analista);
    novaLigacao.setNomeArquivoOriginal(arquivo.getOriginalFilename());
    novaLigacao.setTamanhoBytes(arquivo.getSize());
    novaLigacao.setBucketPath(nomeNoStorage); // Guarda o nome real do ficheiro no MinIO
    novaLigacao.setDataAtendimento(LocalDateTime.now());
    novaLigacao.setDuracaoSegundos(180);
    novaLigacao.setClienteIdentificador("CLIENTE-IDENTIFICADO");

    Ligacao salva = repository.save(novaLigacao);
    iaService.iniciarAnalise(salva.getId());

    return salva;
}

@GetMapping("/{id}/audio")
public ResponseEntity<Resource> extrairAudio(@PathVariable Long id) {
    // 1. Busca a ligação no banco
    Ligacao ligacao = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ligação não encontrada"));

    // 2. Recupera o InputStream do MinIO via S3Client
    try {
        software.amazon.awssdk.core.ResponseInputStream<software.amazon.awssdk.services.s3.model.GetObjectResponse> s3Object = 
            s3Client.getObject(software.amazon.awssdk.services.s3.model.GetObjectRequest.builder()
                .bucket("callquality-audios")
                .key(ligacao.getBucketPath())
                .build());

        InputStreamResource resource = new InputStreamResource(s3Object);

        // 3. Retorna o áudio com o cabeçalho correto para o player do navegador
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .body(resource);

    } catch (Exception e) {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao recuperar áudio do storage");
    }
}