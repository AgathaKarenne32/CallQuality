# 📞 CallQuality AI - Documentação do Projeto

## 1. Visão Geral do Projeto

O **CallQuality AI** é uma solução de software *Full Stack* desenvolvida para revolucionar o departamento de Garantia de Qualidade (QA) em centros de atendimento (*Call Centers*). O objetivo principal da plataforma é substituir processos manuais, lentos e baseados em folhas de cálculo, por um sistema centralizado e inteligente.

A inovação central do projeto reside na sua arquitetura híbrida de avaliação: o sistema utiliza **Inteligência Artificial Generativa** para realizar a triagem massiva, transcrição e pré-avaliação de 100% das chamadas, enquanto os supervisores humanos atuam de forma estratégica, validando as notas e focando no *coaching* (treino) dos analistas. Isso garante escala na análise sem perder a nuance da supervisão humana.

---

## 2. Requisitos Funcionais (O que o sistema faz)

Esta secção detalha as funcionalidades que estarão disponíveis para os utilizadores finais (Administradores, Supervisores e Analistas).

### 🔐 Módulo de Acesso e Gestão

* **Autenticação e Controle de Acesso (RBAC):** O sistema deve possuir um login seguro utilizando o padrão JWT (*JSON Web Token*). O acesso às funcionalidades será restrito com base no perfil do utilizador:
    * **Administrador:** Acesso total.
    * **Supervisor:** Gestão de equipa e avaliações.
    * **Analista:** Visualização pessoal.
* **Gestão de Usuários:** O perfil Administrador deve ter permissão para registar novos utilizadores, editar informações, inativar contas e redefinir senhas, garantindo a manutenção da base de colaboradores.
* **Parametrização de Critérios:** O sistema deve permitir que Supervisores criem e editem critérios de qualidade dinâmicos (exemplo: "Empatia", "Clareza", "Resolução"). Cada critério deve possuir um peso específico, permitindo que a empresa ajuste o rigor da avaliação conforme a necessidade do negócio.

### 🧠 Módulo Core (Processamento e IA)

* **Upload e Gestão de Áudio:** O sistema deve permitir o envio de ficheiros de áudio (formatos \`.mp3\` e \`.wav\`) associados a um analista específico. O sistema deve validar automaticamente o formato e o tamanho do ficheiro antes de iniciar o processamento.
* **Transcrição Automática (Speech-to-Text):** Após o upload, o sistema deve converter automaticamente todo o conteúdo falado no áudio para texto estruturado, utilizando uma API de IA de alta precisão.
* **Análise de Sentimento:** O sistema deve ser capaz de analisar o texto transcrito e classificar o sentimento predominante do cliente durante a interação (**Positivo**, **Neutro** ou **Negativo**), servindo como um indicador de satisfação.
* **Avaliação Automatizada:** Com base na transcrição e nos critérios configurados, a IA deve realizar uma avaliação preliminar, atribuindo uma nota para cada item e gerando uma justificativa em texto para a pontuação atribuída.

### 📊 Módulo de Auditoria e Visualização

* **Interface de Revisão:** O Supervisor deve ter acesso a uma interface que combine o *player* de áudio e o texto transcrito. Nesta tela, ele deve poder validar ou alterar as notas sugeridas pela IA e adicionar *feedbacks* manuais textuais para o analista.
* **Dashboards de Desempenho:** O sistema deve gerar gráficos visuais que mostrem a evolução das notas ao longo do tempo, o ranking dos melhores analistas e, crucialmente, quais critérios de qualidade estão a ser mais infringidos pela equipa.
* **Portal do Analista:** O analista deve ter uma visão restrita onde pode consultar o seu próprio histórico de avaliações, ler os *feedbacks* recebidos e ouvir as suas próprias chamadas avaliadas para fins de auto-aprendizagem.

---

## 3. Requisitos Não Funcionais (Como o sistema opera)

Esta secção define as restrições técnicas, padrões de qualidade e arquitetura que suportam o sistema.

* **Processamento Assíncrono e Filas:** Dado que a transcrição de áudio e a análise de IA são processos demorados, estas tarefas não devem bloquear a navegação do utilizador. O sistema deve utilizar filas de processamento para que o utilizador possa continuar a navegar enquanto o *backend* processa os arquivos em segundo plano.
* **Estratégia de Armazenamento (Storage):** Para garantir a performance da base de dados, os arquivos de áudio binários não devem ser armazenados diretamente nas tabelas. Devem ser guardados num sistema de ficheiros ou serviço de armazenamento em nuvem (*Object Storage*), guardando na base de dados apenas o caminho (*link*) para o ficheiro.
* **Segurança da Informação:** Todas as senhas dos utilizadores devem ser armazenadas na base de dados utilizando algoritmos de *hash* fortes (como BCrypt), garantindo que nem mesmo os administradores tenham acesso às senhas originais.
* **Auditoria de Custos de API:** O sistema deve registar o consumo de *tokens* (unidade de custo das IAs Generativas) de cada operação realizada. Isso é essencial para monitorizar os custos operacionais da ferramenta e evitar gastos excessivos com as APIs de terceiros.
* **Stack Tecnológica Definida:**
    * **Backend:** Java 21 com Spring Boot 3.2.
    * **Database:** MySQL 8.0 (via Docker).
    * **IA Integration:** OpenAI APIs (Modelos Whisper e GPT).

---

## 4. Regras de Negócio (Lógica e Restrições)

Estas são as leis que regem o comportamento do sistema e a tomada de decisão.

1.  **Soberania da Avaliação Humana:** Embora a IA realize a avaliação inicial, a nota atribuída por um Supervisor humano é sempre a final. Se um Supervisor alterar uma nota dada pela IA, o sistema deve considerar a nota humana como a verdade absoluta e alterar o estado da avaliação para "Revisado".
2.  **Cálculo de Nota Ponderada:** A nota final de um atendimento não é uma média aritmética simples. Ela deve ser calculada através de uma média ponderada, onde critérios mais importantes (com peso maior) influenciam mais o resultado final.
    * *Fórmula:* \`(Soma das Notas dos Itens × Peso do Critério) / Soma Total dos Pesos\`.
3.  **Imutabilidade da Evidência:** O texto transcrito pela IA é considerado uma evidência do atendimento e não pode ser editado manualmente. Apenas as notas e os comentários de avaliação podem ser alterados.
4.  **Versionamento de Critérios:** Se um critério de avaliação for alterado ou removido pelo administrador, essa mudança só deve afetar avaliações futuras. O histórico de avaliações passadas deve permanecer intacto, preservando as regras que estavam vigentes na data daquela avaliação (Padrão Snapshot).
5.  **Privacidade e Visibilidade:** Um Analista nunca pode visualizar as avaliações, notas ou áudios de outros colegas. A sua visão é estritamente limitada aos seus próprios dados. Supervisores e Administradores têm visão global.

---

## 5. Arquitetura do Backend (Implementação Técnica)

O Backend foi construído utilizando **Java** com **Spring Boot**, seguindo uma arquitetura em camadas (Layered Architecture) para garantir a separação de responsabilidades e facilitar a manutenção.

### 🏗️ Estrutura e Decisões Técnicas

#### 1. API RESTful com Spring Web
* **O que é:** O ponto de entrada da aplicação.
* **Por que usamos:** Para expor os dados (Usuários, Ligações, Avaliações) de forma padronizada (JSON) para que qualquer Frontend (React, Mobile, etc.) possa consumir.
* **Componentes:** \`Controllers\` que recebem as requisições HTTP e devolvem as respostas.

#### 2. Persistência de Dados (Spring Data JPA + MySQL)
* **O que é:** A camada que conversa com o Banco de Dados.
* **Por que usamos:** O JPA abstrai a complexidade do SQL. Criamos "Entidades" (Classes Java) que espelham as tabelas. Isso nos permite trocar de banco no futuro se necessário e evita erros manuais de SQL.
* **Destaque:** Implementação do padrão *Snapshot* na tabela \`tb_item_avaliacao\` para garantir a regra de versionamento de critérios.

#### 3. Motor de Processamento Assíncrono (@Async)
* **O que é:** A capacidade do sistema realizar tarefas em "segundo plano".
* **Por que usamos:** A transcrição de áudio e a análise de IA são processos lentos (podem levar minutos). Se fizéssemos isso de forma síncrona, o navegador do usuário ficaria "congelado" esperando.
* **Como funciona:** Quando o usuário faz Upload, o servidor responde imediatamente "Recebido" (Status: PENDENTE) e libera o usuário. Uma *thread* separada assume o processamento pesado, atualizando o status para CONCLUIDO quando terminar.

#### 4. Camada de Serviço (Service Layer)
* **O que é:** O cérebro da aplicação.
* **Por que usamos:** Para isolar a Regra de Negócio. O Controller apenas recebe dados, o Repository apenas salva dados. Quem calcula a média ponderada, chama a IA e define se a ligação foi boa ou ruim é o Service. Isso facilita os testes unitários.

#### 5. Documentação Viva (Swagger/OpenAPI)
* **O que é:** Uma interface visual gerada automaticamente.
* **Por que usamos:** Permite testar a API sem precisar escrever código de Frontend. A documentação se atualiza sozinha sempre que alteramos o código Java, garantindo que nunca fique obsoleta.

#### 6. Robustez com Testes (JUnit + MockMvc)
* **O que é:** Robôs que testam o código.
* **Por que usamos:** Criamos testes de integração que simulam o envio de um arquivo real para garantir que o fluxo de Upload -> Banco -> Resposta nunca pare de funcionar, mesmo após alterações futuras.

---

## 6. Como Executar o Backend

### Pré-requisitos
* Docker e Docker Compose
* Java 17 ou superior (ou use o Codespaces)

### Passos
1. Suba o banco de dados:
   \`\`\`bash
   docker-compose up -d
   \`\`\`
2. Inicie a aplicação:
   \`\`\`bash
   cd backend
   mvn spring-boot:run
   \`\`\`
3. Acesse a Documentação (Swagger):
   \`http://localhost:8081/swagger-ui/index.html\`


Novas implementações, que pretendo seguir agora, após concluir o mvp.

**Roadmap de Evolução** serveria para desenhar e para transformar o **CallQuality AI** em um produto de mercado (SaaS).

Divido em 3 pilares: **Técnico (Backend)**, **Funcional (Produto)** e **Visual (UX/UI)**.


### 1. 🧠 Evolução Técnica (Backend & Infra)

O seu backend está sólido, mas ele roda em modo "Simulação". O próximo passo é torná-lo real.

* **A. Integração Real com OpenAI (Fim do Mock):**
    * **O que fazer:** Substituir os `Thread.sleep` do `ProcessamentoIAService`.
    * **Como:**
        1.  Usar a API **Whisper** (da OpenAI) para enviar o arquivo de áudio e receber o texto real.
        2.  Enviar o texto para o **GPT-4o** com um *System Prompt* contendo os critérios que cadastramos no banco.
        3.  Fazer o Java ler o JSON de resposta da OpenAI e salvar as notas reais.
    * *Impacto:* O sistema deixa de ser um brinquedo e passa a analisar chamadas de verdade.

* **B. Armazenamento de Arquivos (Object Storage):**
    * **O que fazer:** Hoje salvamos um caminho falso (`s3://fake...`). Precisamos salvar o arquivo físico.
    * **Como:** Integrar com **MinIO** (que é um clone do S3 que roda no Docker de graça) ou **AWS S3** (se tiver conta na AWS).
    * *Impacto:* O usuário poderá dar "Play" no áudio e ouvir a gravação de verdade.

* **C. Notificações em Tempo Real (Websockets):**
    * **O que fazer:** Substituir o `setInterval` de 5 segundos no Frontend (que fica "perguntando" pro servidor se acabou).
    * **Como:** Usar **Spring Websocket**. O servidor avisa o navegador: *"Ei, acabei a análise #15!"* e o Frontend atualiza na hora.
    * *Impacto:* Performance e experiência de uso instantânea.

---

### 2. 🛡️ Evolução de Segurança (O "Cadeado")

O README menciona JWT e RBAC, mas ainda não implementamos o bloqueio de rotas.

* **A. Spring Security + JWT:**
    * **O que fazer:** Criar o endpoint `/auth/login`. Quando o usuário logar, recebe um Token.
    * **Proteção:** Ninguém pode chamar `/ligacoes/upload` sem enviar esse token no cabeçalho.
    * *Impacto:* Segurança obrigatória para qualquer software comercial.

* **B. Diferenciação de Perfis (RBAC):**
    * **O que fazer:**
        * **Analista:** Só vê as *suas* ligações.
        * **Supervisor:** Vê tudo e pode criar critérios.
    * *Impacto:* Permite vender o software para empresas grandes com hierarquia.

---

### 3. ✨ Evolução de Produto & UX (Frontend)

Aqui é onde o usuário "brilha os olhos".

* **A. Player de Áudio com Onda (Waveform):**
    * **Ideia:** Em vez de um player padrão do navegador, usar uma biblioteca como `wavesurfer.js`.
    * **Visual:** Mostrar o desenho da onda sonora.
    * **Funcionalidade:** Quando clicar em uma frase na transcrição, o áudio pula para aquele segundo exato. (Isso é o "estado da arte" em ferramentas de transcrição).

* **B. Módulo de Contestação:**
    * **Cenário:** A IA deu nota baixa injustamente.
    * **Funcionalidade:** O Analista clica num botão "Contestar Avaliação", escreve o motivo, e o Supervisor recebe um alerta para reavaliar manualmente.

* **C. Comparativo de Períodos:**
    * **No Dashboard:** Um gráfico mostrando "Esta semana" vs "Semana passada".
    * *Impacto:* Gestão de performance.

---
