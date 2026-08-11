# EscolaFutebol
# ⚽ EscolaFut — Sistema de Gerenciamento para Escola de Futebol

Sistema desktop desenvolvido em **Java** para gerenciamento de uma escola de futebol, utilizando **Java Swing**, **JDBC** e **MySQL**.

O projeto foi desenvolvido a partir do estudo de caso **Escola de Futebol Craques do Futuro**, com o objetivo de substituir controles realizados manualmente em planilhas por uma aplicação integrada capaz de armazenar, consultar e gerenciar as principais informações da escola.

## 📌 Sobre o projeto

A Escola de Futebol Craques do Futuro necessita controlar informações relacionadas aos seus alunos, treinadores, turmas, matrículas e pagamentos.

O **EscolaFut** propõe a centralização desses dados em um banco de dados relacional, disponibilizando uma aplicação desktop desenvolvida em Java para realização das principais operações de gerenciamento.

O sistema utiliza o padrão **CRUD (Create, Read, Update e Delete)**, permitindo cadastrar, consultar, atualizar e excluir registros através de uma interface gráfica.

## 🎯 Objetivo

Desenvolver um sistema de gerenciamento para uma escola de futebol que permita organizar e administrar informações de maneira centralizada, reduzindo a dependência de planilhas e facilitando o acesso aos dados.

### Objetivos específicos

- Gerenciar alunos cadastrados na escola;
- Gerenciar treinadores;
- Organizar as turmas;
- Controlar matrículas;
- Registrar e acompanhar pagamentos;
- Permitir operações de cadastro, consulta, alteração e exclusão;
- Integrar a aplicação Java a um banco de dados MySQL;
- Aplicar conceitos de programação orientada a objetos;
- Utilizar JDBC para comunicação entre a aplicação e o banco de dados.

## 🛠️ Tecnologias utilizadas

- **Java**
- **Java Swing**
- **JDBC (Java Database Connectivity)**
- **MySQL 8**
- **MySQL Connector/J 8.0.31**
- **Apache NetBeans**
- **SQL**
- **Ant**

## 🗂️ Estrutura do projeto

```text
EscolaFut/
│
├── src/
│   ├── conexao/
│   │   └── Conexao.java
│   │
│   ├── controle/
│   │   ├── MenuPrincipal.java
│   │   ├── TelaAluno.java
│   │   ├── TelaTreinador.java
│   │   ├── TelaTurma.java
│   │   ├── TelaMatricula.java
│   │   └── TelaPagamento.java
│   │
│   ├── dao/
│   │   └── classes responsáveis pelo acesso aos dados
│   │
│   └── modelo/
│       └── classes de domínio do sistema
│
├── database/
│   └── escola_futebol.sql
│
├── documentacao/
│   └── documentação do projeto
│
├── lib/
│   └── mysql-connector-j-8.0.31.jar
│
├── nbproject/
├── build.xml
└── README.md
```

## 🧩 Módulos do sistema

### 👨‍🎓 Alunos

Responsável pelo gerenciamento dos alunos matriculados na escola.

Permite realizar operações de cadastro, consulta, alteração e exclusão de alunos.

### 🧑‍🏫 Treinadores

Responsável pelo cadastro e gerenciamento dos treinadores da escola.

Cada treinador pode estar associado às turmas pelas quais é responsável.

### ⚽ Turmas

Permite organizar as turmas oferecidas pela escola, mantendo informações necessárias para sua identificação e relacionamento com treinadores e matrículas.

### 📝 Matrículas

Responsável por relacionar os alunos às respectivas turmas.

A matrícula funciona como uma das principais ligações entre os diferentes módulos do sistema.

### 💳 Pagamentos

Permite registrar e consultar informações financeiras relacionadas às matrículas dos alunos.

## 🗄️ Banco de dados

O sistema utiliza o banco de dados **MySQL**.

O banco principal possui as seguintes tabelas:

```text
aluno
treinador
turma
matricula
pagamento
```

Os principais relacionamentos podem ser representados de forma simplificada como:

```text
TREINADOR
    │
    └──< TURMA
           │
           └──< MATRICULA >── ALUNO
                    │
                    └──< PAGAMENTO
```

Assim, um treinador pode estar relacionado a turmas, alunos podem possuir matrículas, as matrículas relacionam alunos e turmas e os pagamentos ficam associados às respectivas matrículas.

## 🚀 Como executar

### Pré-requisitos

Antes de executar o projeto, tenha instalado:

- Java Development Kit (JDK);
- Apache NetBeans;
- MySQL Server 8.x;
- MySQL Workbench.

O **MySQL Connector/J 8.0.31** já está disponível na pasta `lib` do projeto.

### 1. Clone o repositório

```bash
git clone URL_DO_SEU_REPOSITORIO
```

Ou faça o download do projeto em formato ZIP.

### 2. Configure o banco de dados

Abra o **MySQL Workbench** e conecte-se à sua instância local do MySQL.

Depois abra:

```text
database/escola_futebol.sql
```

Execute o script completo.

Ele será responsável pela criação do banco:

```text
escola_futebol
```

e das tabelas necessárias para funcionamento da aplicação.

### 3. Configure a conexão

Abra:

```text
src/conexao/Conexao.java
```

Confira as configurações de conexão com seu MySQL.

Exemplo:

```java
private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
private static final String USUARIO = "root";
private static final String SENHA = "";
```

A URL deve apontar para o banco `escola_futebol` no servidor MySQL configurado.

Por padrão, o projeto utiliza:

```text
Host: 127.0.0.1
Porta: 3306
Banco: escola_futebol
Usuário: root
```

> **Importante:** não publique sua senha real do MySQL no GitHub.

Configure a senha apenas no ambiente local ou utilize uma solução apropriada para armazenar credenciais fora do código-fonte.

### 4. Abra no Apache NetBeans

No NetBeans:

```text
File → Open Project
```

Selecione a pasta do projeto `EscolaFut`.

### 5. Compile o projeto

Utilize:

```text
Run → Clean and Build Project
```

ou:

```text
Shift + F11
```

A compilação deverá terminar com:

```text
BUILD SUCCESSFUL
```

### 6. Execute

A classe principal da aplicação é:

```text
controle.MenuPrincipal
```

Execute o projeto pelo NetBeans para abrir o menu principal do sistema.

## 🔌 Conexão JDBC

A comunicação entre Java e MySQL é realizada através do **JDBC**.

O projeto utiliza:

```java
com.mysql.cj.jdbc.Driver
```

com o **MySQL Connector/J 8.0.31**.

A arquitetura simplificada da comunicação é:

```text
Interface Java Swing
        │
        ▼
      DAO
        │
        ▼
      JDBC
        │
        ▼
MySQL Connector/J
        │
        ▼
MySQL Server
        │
        ▼
escola_futebol
```

## 🏗️ Organização do código

O projeto foi dividido em diferentes pacotes para separar responsabilidades.

### `modelo`

Contém as classes que representam as entidades do sistema, como aluno, treinador, turma, matrícula e pagamento.

### `dao`

Contém as classes responsáveis pela comunicação entre os objetos Java e o banco de dados.

### `conexao`

Responsável pela configuração e gerenciamento da conexão JDBC com o MySQL.

### `controle`

Contém as telas e componentes responsáveis pela interação do usuário com a aplicação.

## 🔄 CRUD

Os módulos do sistema foram estruturados em torno das quatro operações fundamentais:

| Operação | Descrição |
|---|---|
| Create | Cadastro de novos registros |
| Read | Consulta e listagem dos registros |
| Update | Alteração de informações existentes |
| Delete | Exclusão de registros |

## 📚 Conceitos aplicados

Durante o desenvolvimento são utilizados conceitos como:

- Programação Orientada a Objetos;
- Classes e objetos;
- Herança;
- Encapsulamento;
- Associação entre classes;
- Banco de dados relacional;
- Chaves primárias;
- Chaves estrangeiras;
- Integridade referencial;
- SQL;
- JDBC;
- DAO;
- CRUD;
- Interfaces gráficas com Java Swing.

## 📖 Contexto acadêmico

Este projeto foi desenvolvido como atividade acadêmica a partir do estudo de caso **Escola de Futebol Craques do Futuro**.

A proposta envolve o desenvolvimento progressivo de um sistema Java integrado a banco de dados, passando pelas etapas de levantamento e documentação de requisitos, modelagem do banco de dados, implementação SQL, modelagem UML e desenvolvimento da aplicação.

## 🔒 Segurança

Credenciais pessoais de banco de dados não devem ser enviadas ao repositório.

Antes de realizar um commit, certifique-se de que arquivos contendo senhas, tokens ou outras informações privadas não estejam sendo publicados.

## 📄 Status do projeto

🚧 **Em desenvolvimento**

A estrutura principal do sistema, banco de dados, conexão JDBC e módulos CRUD estão sendo implementados e testados.

## 👨‍💻 Autor

Desenvolvido como projeto acadêmico para a disciplina de Desenvolvimento de Sistemas.
