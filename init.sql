-- 1. USUÁRIOS (Quem acessa)
CREATE TABLE tb_usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    senha_hash VARCHAR(255) NOT NULL, -- Hash BCrypt
    perfil ENUM('ADMIN', 'SUPERVISOR', 'ANALISTA') NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 2. CRITÉRIOS DE AVALIAÇÃO (Regras parametrizáveis)
-- Ex: "Fez saudação?", "Resolveu o problema?", "Foi educado?"
CREATE TABLE tb_criterio (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL,
    instrucao_ia TEXT, -- Prompt específico para a IA avaliar este item
    peso INT DEFAULT 1, -- Peso na nota final (1 a 5)
    ativo BOOLEAN DEFAULT TRUE
);

-- 3. LIGAÇÕES (O objeto central)
CREATE TABLE tb_ligacao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    analista_id BIGINT NOT NULL,
    
    -- Metadados do Arquivo
    nome_arquivo_original VARCHAR(255),
    bucket_path VARCHAR(500) NOT NULL, -- Caminho no S3 ou local
    duracao_segundos INT,
    tamanho_bytes BIGINT,
    
    -- Metadados do Atendimento
    data_atendimento DATETIME NOT NULL,
    cliente_identificador VARCHAR(100), -- Telefone ou ID do cliente (anonimizado se precisar)
    
    -- Status do Processamento Assíncrono
    status_processamento ENUM('PENDENTE', 'TRANSCRICAO_EM_ANDAMENTO', 'ANALISE_IA_EM_ANDAMENTO', 'CONCLUIDO', 'ERRO') DEFAULT 'PENDENTE',
    mensagem_erro TEXT, -- Se der erro, salva aqui o motivo
    
    -- Resultados Brutos da IA (Armazenados aqui para facilitar busca rápida)
    transcricao_completa LONGTEXT,
    sentimento_predominante ENUM('POSITIVO', 'NEUTRO', 'NEGATIVO', 'MISTO') DEFAULT 'NEUTRO',
    
    data_upload DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (analista_id) REFERENCES tb_usuario(id)
);

-- 4. AVALIAÇÕES (O Boletim)
CREATE TABLE tb_avaliacao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ligacao_id BIGINT NOT NULL,
    supervisor_id BIGINT, -- Se NULL, foi avaliação automática pura
    
    nota_final DECIMAL(5,2), -- 0.00 a 100.00
    feedback_geral TEXT,
    
    origem_avaliacao ENUM('IA', 'MANUAL_SUPERVISOR') NOT NULL,
    data_avaliacao DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (ligacao_id) REFERENCES tb_ligacao(id),
    FOREIGN KEY (supervisor_id) REFERENCES tb_usuario(id)
);

-- 5. DETALHE DA AVALIAÇÃO (Nota item a item)
CREATE TABLE tb_item_avaliacao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    avaliacao_id BIGINT NOT NULL,
    criterio_id BIGINT NOT NULL,
    
    -- Resultado
    cumpriu_requisito BOOLEAN, -- Sim/Não
    nota_atribuida DECIMAL(5,2),
    
    -- Explicação (Fundamental para a IA justificar a nota)
    justificativa_ia TEXT, 
    
    FOREIGN KEY (avaliacao_id) REFERENCES tb_avaliacao(id),
    FOREIGN KEY (criterio_id) REFERENCES tb_criterio(id)
);

-- 6. AUDITORIA E CUSTOS DE IA (Requisito Não Funcional)
CREATE TABLE tb_log_uso_ia (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ligacao_id BIGINT,
    
    servico_usado VARCHAR(50), -- 'WHISPER', 'GPT-4o', 'GPT-3.5'
    tipo_operacao ENUM('TRANSCRICAO', 'ANALISE'),
    
    tokens_entrada INT DEFAULT 0,
    tokens_saida INT DEFAULT 0,
    custo_estimado_usd DECIMAL(10,6),
    
    tempo_processamento_ms BIGINT, -- Milissegundos que demorou
    data_uso DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (ligacao_id) REFERENCES tb_ligacao(id)
);

-- DADOS SEED (Para você não começar com banco vazio)
INSERT INTO tb_usuario (nome, email, senha_hash, perfil) VALUES 
('Agatha Admin', 'agatha@callquality.com', 'HASH_DE_TESTE', 'ADMIN'),
('Supervisor Teste', 'sup@callquality.com', 'HASH_DE_TESTE', 'SUPERVISOR'),
('Analista Junior', 'ana@callquality.com', 'HASH_DE_TESTE', 'ANALISTA');

INSERT INTO tb_criterio (descricao, instrucao_ia, peso) VALUES 
('Saudação Inicial', 'Verifique se o atendente se apresentou e disse o nome da empresa no início.', 1),
('Empatia', 'Analise se o atendente usou palavras de compreensão como "sinto muito", "entendo", "vou ajudar".', 2),
('Resolução', 'O problema do cliente foi explicitamente resolvido ou encaminhado corretamente?', 3);