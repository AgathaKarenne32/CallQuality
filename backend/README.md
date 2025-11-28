# 📞 CallQuality AI - Backend API

O **CallQuality AI** é uma solução de software *Full Stack* desenvolvida para revolucionar o departamento de Garantia de Qualidade (QA) em Call Centers.

Este repositório contém o **Backend (API RESTful)** responsável por orquestrar o processamento de áudio, integração com Inteligência Artificial e gestão de dados.

🔗 **Frontend Repository:** [CallQuality-Web](https://github.com/AgathaKarenne32/CallQuality---frontend)

---

## 🚀 Status do Desenvolvimento (MVP Entregue)

O projeto atingiu o estágio de **MVP Funcional e Seguro**. As seguintes funcionalidades já estão implementadas e operacionais:

### ✅ Segurança & Autenticação
* **Login JWT:** Sistema completo de emissão e validação de tokens.
* **Criptografia:** Senhas salvas com Hash BCrypt (padrão de mercado).
* **RBAC (Controle de Acesso):**
    * Rotas públicas: Login e Documentação.
    * Rotas protegidas: Upload, Listagem e Configurações.
    * Isolamento de Dados: Analistas veem apenas suas próprias ligações.

### 🧠 Inteligência Artificial (Real)
* **Integração Groq (Llama 3):** Substituímos o mock inicial por chamadas reais a uma LLM de ponta via API.
* **Resiliência (Fallback):** Se a API da IA falhar ou a chave for inválida, o sistema ativa automaticamente um modo de análise local baseada em palavras-chave, garantindo que o processo nunca trave.

### ⚙️ Arquitetura Técnica
* **Processamento Assíncrono:** Uploads não bloqueiam o servidor. A análise roda em threads separadas (`@Async`).
* **Banco de Dados:** MySQL 8.0 rodando em Docker com migrations automáticas pelo Hibernate.
* **API Documentation:** Swagger UI configurado e seguro (suporta Bearer Token).

---

## 🛠️ Como Executar Localmente

### Pré-requisitos
* Docker & Docker Compose
* Java 17+
* Maven

### 1. Subir o Banco de Dados
```bash
docker-compose up -d
```

### 2. Configurar a Chave da IA (Opcional)
Para usar a IA real (Groq), exporte sua chave. Se não fizer isso, o sistema usará o Fallback Local.
```bash
export GROQ_API_KEY=gsk_sua_chave_aqui
```

### 3. Iniciar o Backend
```bash
mvn spring-boot:run
```
A API estará disponível em: `http://localhost:8081`

### 4. Acessar Documentação (Swagger)
Abra no navegador: [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)

---

## 🔮 Roadmap de Evolução (Próximos Passos)

Para transformar este MVP em um produto SaaS Enterprise, os próximos passos planejados são:

### 1. 🧠 Evolução Técnica (Infra)
* [ ] **Armazenamento S3/MinIO:** Salvar os arquivos de áudio fisicamente em Object Storage para permitir o "Play" real no frontend.
* [ ] **Websockets:** Substituir o polling do frontend por notificações em tempo real quando a análise concluir.

### 2. ✨ Evolução de Produto (UX)
* [ ] **Player Waveform:** Visualização da onda sonora sincronizada com a transcrição.
* [ ] **Módulo de Contestação:** Fluxo para analistas discordarem da nota da IA e solicitarem revisão humana.
* [ ] **Dashboard Gerencial:** Gráficos comparativos de performance entre equipes e períodos (Semanal/Mensal).

---

## 📐 Regras de Negócio Implementadas

1.  **Imutabilidade Histórica:** Implementamos o padrão *Snapshot* na tabela de itens de avaliação. Se um critério mudar de peso hoje, as avaliações passadas não são alteradas.
2.  **Isolamento de Tenant:** O endpoint `/ligacoes` detecta o perfil do usuário logado e filtra os dados automaticamente (Analista vs Admin).

---

Desenvolvido por **Agatha Karenne** como portfólio de Engenharia de Software Full Stack.
