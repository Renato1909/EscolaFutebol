-- Escola de Futebol Craques do Futuro
-- MySQL 8.x | DDL + dados mínimos para teste
CREATE DATABASE IF NOT EXISTS escola_futebol CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE escola_futebol;

CREATE TABLE IF NOT EXISTS aluno (
    id_aluno INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(120) NOT NULL,
    data_nascimento DATE NOT NULL,
    telefone VARCHAR(20),
    email VARCHAR(120),
    responsavel VARCHAR(120),
    PRIMARY KEY (id_aluno)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS treinador (
    id_treinador INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(120) NOT NULL,
    telefone VARCHAR(20),
    email VARCHAR(120),
    especialidade VARCHAR(100),
    PRIMARY KEY (id_treinador)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS turma (
    id_turma INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(80) NOT NULL,
    faixa_etaria VARCHAR(50) NOT NULL,
    dia_semana VARCHAR(20) NOT NULL,
    horario TIME NOT NULL,
    capacidade INT NOT NULL,
    id_treinador INT NOT NULL,
    PRIMARY KEY (id_turma),
    CONSTRAINT ck_turma_capacidade CHECK (capacidade > 0),
    CONSTRAINT fk_turma_treinador FOREIGN KEY (id_treinador) REFERENCES treinador(id_treinador)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS matricula (
    id_matricula INT NOT NULL AUTO_INCREMENT,
    data_matricula DATE NOT NULL,
    situacao ENUM('ATIVA','CANCELADA','TRANCADA') NOT NULL DEFAULT 'ATIVA',
    id_aluno INT NOT NULL,
    id_turma INT NOT NULL,
    PRIMARY KEY (id_matricula),
    CONSTRAINT uq_matricula_aluno_turma UNIQUE (id_aluno, id_turma),
    CONSTRAINT fk_matricula_aluno FOREIGN KEY (id_aluno) REFERENCES aluno(id_aluno)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_matricula_turma FOREIGN KEY (id_turma) REFERENCES turma(id_turma)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS pagamento (
    id_pagamento INT NOT NULL AUTO_INCREMENT,
    vencimento DATE NOT NULL,
    data_pagamento DATE NULL,
    valor DECIMAL(10,2) NOT NULL,
    situacao ENUM('PENDENTE','PAGO','ATRASADO','CANCELADO') NOT NULL DEFAULT 'PENDENTE',
    id_matricula INT NOT NULL,
    PRIMARY KEY (id_pagamento),
    CONSTRAINT ck_pagamento_valor CHECK (valor >= 0),
    CONSTRAINT fk_pagamento_matricula FOREIGN KEY (id_matricula) REFERENCES matricula(id_matricula)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- Dados mínimos de demonstração
INSERT INTO treinador (nome, telefone, email, especialidade)
SELECT 'Carlos Henrique', '(11) 90000-0001', 'carlos@craques.local', 'Fundamentos'
WHERE NOT EXISTS (SELECT 1 FROM treinador LIMIT 1);

INSERT INTO aluno (nome, data_nascimento, telefone, email, responsavel)
SELECT 'João da Silva', '2012-05-14', '(11) 98888-1111', 'joao@exemplo.local', 'Maria da Silva'
WHERE NOT EXISTS (SELECT 1 FROM aluno LIMIT 1);
