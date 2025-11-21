📞 CallQuality AI - Especificação Técnica do Projeto

Status: 🏗️ Em Arquitetura / Modelagem de Dados

Versão: 1.0.0

1. Visão Geral

O CallQuality AI é uma plataforma Full Stack projetada para modernizar o setor de Quality Assurance (QA) em Call Centers. O sistema automatiza a avaliação de atendimentos telefônicos utilizando Inteligência Artificial Generativa, reduzindo o trabalho manual de supervisores e fornecendo feedbacks instantâneos e imparciais.

O diferencial técnico reside na arquitetura híbrida: a IA realiza a triagem massiva e a transcrição (Speech-to-Text), enquanto o supervisor humano atua na validação e no coaching da equipe.

2. Requisitos Funcionais (RF)

Funcionalidades que o sistema disponibiliza aos usuários.

🔐 Módulo 1: Gestão e Acesso

ID

Requisito

Descrição

Ator

RF001

Autenticação e RBAC

Login seguro via JWT com controle de acesso baseado em cargos (Admin, Supervisor, Analista).

Todos

RF002

Gestão de Usuários

Cadastro, edição e inativação de analistas e supervisores.

Admin

RF003

Gestão de Critérios

Interface para criar regras de avaliação dinâmicas (ex: "Empatia", "Script de Vendas") e definir seus pesos.

Supervisor

🎧 Módulo 2: Core & Processamento (Pipeline)

ID

Requisito

Descrição

Ator

RF004

Upload de Áudio

Envio de arquivos .mp3 ou .wav associados a um analista específico. O sistema deve validar tamanho e formato.

Supervisor

RF005

Transcrição (STT)

Conversão automática do áudio em texto (Speech-to-Text) utilizando API de IA.

Sistema (Auto)

RF006

Análise de Sentimento

Classificação automática do humor do cliente durante a chamada (Positivo, Neutro, Negativo).

Sistema (Auto)

RF007

Avaliação Automática

A IA deve analisar a transcrição baseada nos critérios ativos e atribuir uma nota preliminar com justificativa.

Sistema (Auto)

📊 Módulo 3: Auditoria e Dashboards

ID

Requisito

Descrição

Ator

RF008

Interface de Revisão

Player de áudio sincronizado com o texto transcrito, permitindo ao supervisor alterar a nota da IA e adicionar observações.

Supervisor

RF009

Dashboard de Performance

Gráficos exibindo evolução das notas, ranking de analistas e critérios com maior índice de falha.

Supervisor

RF010

Histórico Pessoal

O analista deve visualizar apenas as suas próprias avaliações e feedbacks.

Analista

3. Requisitos Não Funcionais (RNF)

Restrições técnicas e atributos de qualidade.

[RNF001] Processamento Assíncrono: O upload do áudio deve ser desacoplado do processamento. O usuário não deve esperar a IA terminar para continuar navegando (uso de Filas/Jobs).

[RNF002] Armazenamento de Mídia: Arquivos de áudio não devem ser salvos no banco de dados (BLOB), mas sim em um Object Storage (S3, MinIO ou File System local), salvando apenas a referência (URL) no banco.

[RNF003] Segurança de Dados: Senhas devem ser armazenadas com hash forte (BCrypt).

[RNF004] Auditoria de Custos: O sistema deve registrar o consumo de tokens (entrada/saída) de cada chamada de API para controle financeiro.

[RNF005] Stack Tecnológica:

Backend: Java 17+ com Spring Boot 3.

Database: MySQL 8.0 (via Docker).

IA Integration: OpenAI API (Whisper + GPT-4o-mini/GPT-3.5).

4. Regras de Negócio (RN)

Lógica e restrições do domínio.

[RN001] Hierarquia de Avaliação: A avaliação humana (Supervisor) é soberana. Se um supervisor editar uma nota dada pela IA, a nota do supervisor prevalece e o status muda para "Revisado".

[RN002] Cálculo da Nota Final: A nota é uma média ponderada calculada pela fórmula:


$$Nota = \frac{\sum (\text{Nota do Item} \times \text{Peso do Critério})}{\sum \text{Pesos dos Critérios}}$$

[RN003] Imutabilidade da Transcrição: O texto gerado pela IA (transcrição) não pode ser alterado manualmente para garantir a integridade da evidência, apenas a nota pode ser revista.

[RN004] Versionamento de Critérios: Alterações em critérios de avaliação (ex: mudar peso da "Saudação") aplicam-se apenas a novas avaliações. Avaliações passadas não são recalculadas.

[RN005] Visibilidade de Dados:

Analista: Vê apenas dados próprios.

Supervisor: Vê dados de todos os analistas.

Admin: Acesso total ao sistema e configurações.

5. Arquitetura de Dados (Resumo)

O banco de dados foi modelado para suportar a persistência dos resultados da IA e o controle de custos.

tb_usuario: Controle de acesso.

tb_ligacao: Metadados do áudio e transcrição longa (LONGTEXT).

tb_avaliacao: Cabeçalho da nota e feedback geral.

tb_item_avaliacao: Detalhe da nota por critério (com justificativa da IA).

tb_criterio: Regras parametrizáveis.

tb_log_uso_ia: Tabela de auditoria financeira (Tokens/Custo).

Documentação gerada para o projeto CallQuality AI.