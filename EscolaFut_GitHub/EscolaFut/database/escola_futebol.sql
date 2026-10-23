/*
  Banco EscolaFutebol - Microsoft SQL Server.
  Execute em uma base EscolaFutebol vazia.
*/
IF DB_ID(N'EscolaFutebol') IS NULL
BEGIN
    CREATE DATABASE EscolaFutebol;
END;
GO

USE EscolaFutebol;
GO

CREATE TABLE dbo.Aluno (
    id_aluno INT IDENTITY(1,1) NOT NULL,
    nome VARCHAR(120) NOT NULL,
    data_nascimento DATE NOT NULL,
    telefone VARCHAR(20) NULL,
    email VARCHAR(120) NULL,
    responsavel VARCHAR(120) NULL,
    CONSTRAINT PK_Aluno PRIMARY KEY (id_aluno)
);
GO

CREATE TABLE dbo.Treinador (
    id_treinador INT IDENTITY(1,1) NOT NULL,
    nome VARCHAR(120) NOT NULL,
    telefone VARCHAR(20) NULL,
    email VARCHAR(120) NULL,
    especialidade VARCHAR(100) NULL,
    CONSTRAINT PK_Treinador PRIMARY KEY (id_treinador)
);
GO

CREATE TABLE dbo.Turma (
    id_turma INT IDENTITY(1,1) NOT NULL,
    nome VARCHAR(80) NOT NULL,
    faixa_etaria VARCHAR(50) NOT NULL,
    dia_semana VARCHAR(20) NOT NULL,
    horario TIME NOT NULL,
    capacidade INT NOT NULL,
    id_treinador INT NOT NULL,
    CONSTRAINT PK_Turma PRIMARY KEY (id_turma),
    CONSTRAINT CK_Turma_Capacidade CHECK (capacidade > 0),
    CONSTRAINT FK_Turma_Treinador FOREIGN KEY (id_treinador)
        REFERENCES dbo.Treinador(id_treinador)
        ON UPDATE CASCADE ON DELETE NO ACTION
);
GO

CREATE TABLE dbo.Matricula (
    id_matricula INT IDENTITY(1,1) NOT NULL,
    data_matricula DATE NOT NULL,
    situacao VARCHAR(20) NOT NULL CONSTRAINT DF_Matricula_Situacao DEFAULT ('ATIVA'),
    id_aluno INT NOT NULL,
    id_turma INT NOT NULL,
    CONSTRAINT PK_Matricula PRIMARY KEY (id_matricula),
    CONSTRAINT UQ_Matricula_Aluno_Turma UNIQUE (id_aluno, id_turma),
    CONSTRAINT CK_Matricula_Situacao CHECK (situacao IN ('ATIVA', 'CANCELADA', 'TRANCADA')),
    CONSTRAINT FK_Matricula_Aluno FOREIGN KEY (id_aluno)
        REFERENCES dbo.Aluno(id_aluno)
        ON UPDATE CASCADE ON DELETE NO ACTION,
    CONSTRAINT FK_Matricula_Turma FOREIGN KEY (id_turma)
        REFERENCES dbo.Turma(id_turma)
        ON UPDATE CASCADE ON DELETE NO ACTION
);
GO

CREATE TABLE dbo.Pagamento (
    id_pagamento INT IDENTITY(1,1) NOT NULL,
    vencimento DATE NOT NULL,
    data_pagamento DATE NULL,
    valor DECIMAL(10,2) NOT NULL,
    situacao VARCHAR(20) NOT NULL CONSTRAINT DF_Pagamento_Situacao DEFAULT ('PENDENTE'),
    id_matricula INT NOT NULL,
    CONSTRAINT PK_Pagamento PRIMARY KEY (id_pagamento),
    CONSTRAINT CK_Pagamento_Valor CHECK (valor >= 0),
    CONSTRAINT CK_Pagamento_Situacao CHECK (situacao IN ('PENDENTE', 'PAGO', 'ATRASADO', 'CANCELADO')),
    CONSTRAINT FK_Pagamento_Matricula FOREIGN KEY (id_matricula)
        REFERENCES dbo.Matricula(id_matricula)
        ON UPDATE CASCADE ON DELETE NO ACTION
);
GO

-- Dados mínimos de demonstração
IF NOT EXISTS (SELECT 1 FROM dbo.Treinador WHERE email = 'carlos@craques.local')
BEGIN
    INSERT INTO dbo.Treinador (nome, telefone, email, especialidade)
    VALUES ('Carlos Henrique', '(11) 90000-0001', 'carlos@craques.local', 'Fundamentos');
END;
GO

IF NOT EXISTS (SELECT 1 FROM dbo.Aluno WHERE email = 'joao@exemplo.local')
BEGIN
    INSERT INTO dbo.Aluno (nome, data_nascimento, telefone, email, responsavel)
    VALUES ('João da Silva', '2012-05-14', '(11) 98888-1111', 'joao@exemplo.local', 'Maria da Silva');
END;
GO
