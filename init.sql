CREATE TABLE tb_usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    perfil ENUM('ADMIN', 'SUPERVISOR', 'ANALISTA') NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tb_ligacao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    analista_id BIGINT NOT NULL,
    data_hora DATETIME NOT NULL,
    arquivo_url VARCHAR(255) NOT NULL,
    transcricao TEXT,
    status_analise ENUM('PENDENTE', 'EM_PROCESSAMENTO', 'CONCLUIDO') DEFAULT 'PENDENTE',
    FOREIGN KEY (analista_id) REFERENCES tb_usuario(id)
);

CREATE TABLE tb_criterio (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL,
    peso INT NOT NULL,
    ativo BOOLEAN DEFAULT TRUE
);

-- 3. TABELA DE LIGAÇÕES (Dados do Áudio + Transcrição)
CREATE TABLE tb_ligacao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    analista_id BIGINT NOT NULL,
    data_ligacao DATETIME NOT NULL,
    duracao_segundos INT,
    
    -- Dados do Arquivo
    nome_arquivo VARCHAR(255) NOT NULL,
    caminho_storage VARCHAR(500) NOT NULL, -- URL ou Path local
    
    -- Dados da IA (Transcrição)
    transcricao_texto LONGTEXT, -- Texto completo da conversa
    sentimento_geral ENUM('POSITIVO', 'NEUTRO', 'NEGATIVO') DEFAULT 'NEUTRO',
    
    -- Status do Processamento (Fila)
    status_processamento ENUM('PENDENTE', 'PROCESSANDO', 'CONCLUIDO', 'ERRO') DEFAULT 'PENDENTE',
    
    FOREIGN KEY (analista_id) REFERENCES tb_usuario(id)
);

CREATE TABLE tb_avaliacao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ligacao_id BIGINT NOT NULL,
    supervisor_id BIGINT, -- NULL se foi 100% automático e ninguém revisou ainda
    nota_final DECIMAL(5,2),
    comentario_geral TEXT,
    tipo_avaliador ENUM('IA', 'SUPERVISOR') NOT NULL,
    data_avaliacao DATETIME DEFAULT CURRENT_TIMESTAMP,
    -- Guardar o prompt usado e a resposta crua ajuda a "debugar" a IA depois
    metadata_ia JSON,
    FOREIGN KEY (ligacao_id) REFERENCES tb_ligacao(id),
    FOREIGN KEY (supervisor_id) REFERENCES tb_usuario(id)
);

CREATE TABLE tb_item_avaliacao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    avaliacao_id BIGINT NOT NULL,
    criterio_id BIGINT NOT NULL,
    nota_atribuida DECIMAL(5,2), -- Ex: 0 ou 10, ou 0 a 5
    observacao_ia TEXT, -- A IA explica por que tirou ponto
    FOREIGN KEY (avaliacao_id) REFERENCES tb_avaliacao(id),
    FOREIGN KEY (criterio_id) REFERENCES tb_criterio(id)
);

-- 6. LOG DE CUSTOS E USO DE IA (Diferencial para Portfólio)
CREATE TABLE tb_log_ia (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ligacao_id BIGINT,
    modelo_usado VARCHAR(50), -- Ex: 'gpt-4o-mini', 'whisper-1'
    tokens_entrada INT,
    tokens_saida INT,
    custo_estimado DECIMAL(10,6), -- Custo em Dólar
    data_uso DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ligacao_id) REFERENCES tb_ligacao(id)
);

-- Dados iniciais para teste
INSERT INTO tb_usuario (nome, email, senha, perfil) VALUES 
('Agatha Admin', 'agatha@empresa.com', '$2a$10$ExemploHashSenha', 'ADMIN'),
('Roberto Supervisor', 'roberto@empresa.com', '$2a$10$ExemploHashSenha', 'SUPERVISOR'),
('João Analista', 'joao@empresa.com', '$2a$10$ExemploHashSenha', 'ANALISTA');

INSERT INTO tb_criterio (descricao, peso) VALUES 
('Realizou a saudação padrão?', 1),
('Demonstrou empatia durante o atendimento?', 2),
('Confirmou se a solicitação foi resolvida?', 3),
('Não utilizou gírias ou linguagem informal?', 1);