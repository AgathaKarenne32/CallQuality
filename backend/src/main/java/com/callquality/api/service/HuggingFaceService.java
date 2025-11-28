package com.callquality.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

@Service
public class HuggingFaceService {

    @Value("${hf.api.key}")
    private String token;

    @Value("${hf.api.url}")
    private String url;

    private final RestClient restClient;

    public HuggingFaceService(RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    public String analisarSentimento(String texto) {
        try {
            if (token.contains("chave-nao-configurada")) {
                throw new RuntimeException("Token da Groq não configurado");
            }

            // Prompt Otimizado para Llama 3
            String prompt = "Analise o sentimento deste texto de suporte. Responda APENAS com uma das palavras: POSITIVO, NEUTRO ou NEGATIVO. Não explique nada.\n\nTexto: \"" + texto + "\"";

            // Corpo da Requisição (Formato Chat Completion)
            Map<String, Object> body = Map.of(
                "model", "llama3-8b-8192", // Modelo rápido e inteligente
                "messages", List.of(
                    Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.3
            );

            // Chamada HTTP
            Map resposta = restClient.post()
                    .uri(url)
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(Map.class);

            // Navega no JSON de resposta da Groq
            // { "choices": [ { "message": { "content": "POSITIVO" } } ] }
            if (resposta != null && resposta.containsKey("choices")) {
                List choices = (List) resposta.get("choices");
                if (!choices.isEmpty()) {
                    Map choice = (Map) choices.get(0);
                    Map message = (Map) choice.get("message");
                    String conteudo = (String) message.get("content");
                    return limparResposta(conteudo);
                }
            }
        } catch (Exception e) {
            System.out.println("⚠️ Erro na IA (Groq): " + e.getMessage());
            // Mantém o Fallback se der erro
            return fallbackLocal(texto);
        }
        return "NEUTRO";
    }

    private String fallbackLocal(String texto) {
        System.out.println("🔄 Usando Fallback Local...");
        String t = texto.toUpperCase();
        if (t.contains("RUIM") || t.contains("PROBLEMA")) return "NEGATIVO";
        if (t.contains("EXCELENTE") || t.contains("OBRIGADO")) return "POSITIVO";
        return "NEUTRO";
    }

    private String limparResposta(String text) {
        String t = text.toUpperCase();
        if (t.contains("POSITIVO")) return "POSITIVO";
        if (t.contains("NEGATIVO")) return "NEGATIVO";
        return "NEUTRO";
    }
}
