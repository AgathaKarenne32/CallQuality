CREATE TABLE tb_usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    perfil ENUM('ADMIN', 'SUPERVISOR', 'ANALISTA') NOT NULL
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
    peso INT NOT NULL
);

CREATE TABLE tb_avaliacao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ligacao_id BIGINT NOT NULL,
    supervisor_id BIGINT,
    nota_final DECIMAL(5,2),
    comentario_geral TEXT,
    tipo_avaliacao ENUM('MANUAL', 'IA') NOT NULL,
    data_avaliacao DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ligacao_id) REFERENCES tb_ligacao(id),
    FOREIGN KEY (supervisor_id) REFERENCES tb_usuario(id)
);

CREATE TABLE tb_item_avaliacao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    avaliacao_id BIGINT NOT NULL,
    criterio_id BIGINT NOT NULL,
    nota_obtida DECIMAL(5,2),
    observacao TEXT,
    FOREIGN KEY (avaliacao_id) REFERENCES tb_avaliacao(id),
    FOREIGN KEY (criterio_id) REFERENCES tb_criterio(id)
);

-- Dados iniciais para teste
INSERT INTO tb_usuario (nome, email, senha, perfil) VALUES 
('Agatha Supervisor', 'agatha@empresa.com', '123456', 'SUPERVISOR'),
('João Analista', 'joao@empresa.com', '123456', 'ANALISTA');

INSERT INTO tb_criterio (descricao, peso) VALUES 
('Saudação Inicial', 1),
('Clareza na comunicação', 2),
('Resolução do problema', 3);