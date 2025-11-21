1. Requisitos Funcionais (RF)
O que o sistema deve fazer.

[RF001] Gestão de Usuários: O sistema deve permitir o cadastro de analistas e supervisores.

[RF002] Upload de Áudio: O sistema deve permitir o upload de arquivos de áudio (.mp3, .wav) associados a um analista.

[RF003] Transcrição Automática (IA): O sistema deve converter o áudio em texto automaticamente utilizando API de Speech-to-Text.

[RF004] Análise de Sentimento: O sistema deve identificar se o sentimento do cliente foi Positivo, Neutro ou Negativo.

[RF005] Avaliação Automática (IA): O sistema deve avaliar a transcrição com base nos critérios ativos e gerar uma nota preliminar.

[RF006] Revisão Manual: O supervisor deve poder alterar a nota dada pela IA e adicionar observações manuais.

[RF007] Dashboard de Desempenho: O sistema deve exibir gráficos com a evolução das notas dos analistas e os critérios com maior índice de erro.

[RF008] Cadastro de Critérios: O supervisor deve poder criar/editar critérios de avaliação (ex: "Saudação", "Empatia") e seus pesos.

2. Requisitos Não Funcionais (RNF)
Como o sistema deve ser.

[RNF001] Processamento Assíncrono: A transcrição e análise de IA devem ocorrer em segundo plano para não travar a interface do usuário.

[RNF002] Armazenamento: Os arquivos de áudio não devem ser salvos no banco de dados, mas sim em um File System ou Object Storage (S3/MinIO), salvando apenas o link no banco.

[RNF003] Segurança: As senhas devem ser armazenadas com hash (BCrypt).

[RNF004] Stack: Backend em Java (Spring Boot), Banco MySQL 8.0 (Docker).

3. Regras de Negócio (RN)
As restrições do negócio.

[RN001] Visibilidade: Analistas só podem ver suas próprias avaliações. Supervisores veem de todos.

[RN002] Imutabilidade da IA: O texto transcrito pela IA não pode ser alterado, mas a avaliação (notas) pode ser corrigida pelo supervisor.

[RN003] Critérios Dinâmicos: Uma alteração em um critério de avaliação só afeta avaliações futuras, não altera o histórico passado.

[RN004] Pontuação: A nota final é uma média ponderada: (Soma das notas dos itens * peso) / Soma dos pesos.


📋 Requisitos Funcionais (O que o sistema faz)
Autenticação e RBAC (Role-Based Access Control):

O sistema deve ter login seguro (JWT).

Perfil Admin: Cria usuários e configura critérios.

Perfil Supervisor: Sobe áudios, vê dashboards, corrige notas da IA.

Perfil Analista: Vê apenas suas próprias avaliações.

Pipeline de Processamento de Áudio:

Upload de arquivos .mp3 e .wav.

Validação de tamanho e formato.

Envio para fila de processamento (para não travar o sistema).

Integração com IA (O Core):

Etapa 1: Transcrição (Speech-to-Text) - Converter áudio em texto.

Etapa 2: Análise de Sentimento - Detectar se o cliente estava irritado.

Etapa 3: Scoring Automático - Aplicar as regras de negócio sobre o texto.

Gestão da Qualidade:

Interface para o Supervisor ler a transcrição e ouvir o áudio simultaneamente.

Botão para "Recalcular" ou "Editar Nota" caso a IA tenha alucinado.

⚙️ Requisitos Não Funcionais (Qualidade do sistema)
Auditoria de Custos: Registrar quantos tokens (dinheiro) foram gastos em cada análise para evitar surpresas na fatura da OpenAI.

Isolamento de Dados: O banco deve suportar Soft Delete (não apagar registros de verdade, apenas marcar como inativos) para histórico.

Performance: O banco deve ter índices nas colunas de busca frequente (analista_id, data_criacao, sentimento).

📏 Regras de Negócio (A Lógica)
Cálculo da Nota: A nota vai de 0 a 100.

Fórmula: (Soma dos Pontos Obtidos / Soma dos Pesos Possíveis) * 100.

Imutabilidade do Áudio: Uma vez avaliado, o arquivo de áudio não pode ser substituído, apenas arquivado.

Hierarquia de Avaliação: Uma nota dada manualmente por um Supervisor sempre sobrescreve a nota da IA.