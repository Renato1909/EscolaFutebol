# Sistema de Gerenciamento — Escola de Futebol Craques do Futuro

## 1. Introdução — Estudo de Caso
A Escola de Futebol Craques do Futuro oferece treinamento esportivo para crianças, adolescentes e adultos. Atualmente, as informações são registradas em planilhas, o que dificulta o controle de matrículas, pagamentos, turmas e treinadores. A proposta é informatizar esse processo com Java, JDBC e banco de dados relacional, permitindo operações de cadastro, consulta, atualização e exclusão.

## 2. Objetivo Geral
Desenvolver um sistema desktop em Java integrado a um banco de dados relacional por JDBC para centralizar e tornar mais seguro, rápido e organizado o gerenciamento da Escola de Futebol Craques do Futuro.

## 3. Objetivos Específicos
- Cadastrar e manter os dados dos alunos.
- Cadastrar treinadores e suas informações profissionais.
- Organizar turmas, horários e capacidade.
- Registrar matrículas e acompanhar sua situação.
- Registrar e consultar pagamentos.
- Permitir pesquisa, atualização e exclusão de dados conforme as regras de negócio.
- Reduzir duplicidade, inconsistência e perda de informações existentes em planilhas.

## 4. Justificativa
O crescimento da quantidade de alunos aumenta a complexidade do controle manual. Um sistema informatizado centraliza os dados, reduz retrabalho, melhora a consulta das informações e cria uma base estruturada para acompanhar alunos, turmas, treinadores, matrículas e pagamentos.

## 5. Problema
O uso de planilhas separadas dificulta localizar informações, manter dados atualizados, relacionar alunos às turmas, acompanhar pagamentos e controlar os responsáveis por cada treinamento.

## 6. Solução Proposta
Construir uma aplicação desktop Java Swing conectada via JDBC ao MySQL. A aplicação será organizada em módulos e utilizará um banco relacional com chaves primárias e estrangeiras para representar os relacionamentos do negócio.

## 7. Escopo do Sistema
Inclui cadastro e manutenção de aluno, treinador, turma, matrícula e pagamento; consultas; pesquisa; controle dos vínculos entre essas entidades; interface gráfica em Java Swing; persistência em banco relacional. Não inclui, nesta etapa, aplicativo móvel, portal público, pagamentos bancários reais ou integração com serviços externos.

## 8. Regras de Negócio
1. Cada aluno deve possuir um identificador único.
2. Cada treinador deve possuir um identificador único.
3. Cada turma deve estar vinculada a um treinador responsável.
4. Uma matrícula deve vincular um aluno a uma turma.
5. O mesmo aluno não deve possuir duas matrículas ativas idênticas na mesma turma.
6. A quantidade de matrículas ativas não deve ultrapassar a capacidade definida para a turma.
7. Cada pagamento deve estar relacionado a uma matrícula.
8. Registros relacionados não devem ser excluídos de forma que quebrem a integridade referencial.
9. Campos definidos como obrigatórios devem ser validados antes da gravação.
10. Alterações e exclusões devem solicitar confirmação quando houver risco de perda de dados.

## 9. Requisitos Funcionais
- RF01 — Cadastrar aluno.
- RF02 — Consultar/listar aluno.
- RF03 — Atualizar aluno.
- RF04 — Excluir aluno quando permitido pelas regras de integridade.
- RF05 — Cadastrar, consultar, atualizar e excluir treinador.
- RF06 — Cadastrar, consultar, atualizar e excluir turma.
- RF07 — Efetuar matrícula de aluno em turma.
- RF08 — Alterar situação/cancelar matrícula.
- RF09 — Registrar e consultar pagamento.
- RF10 — Pesquisar registros por critérios relevantes, como nome.
- RF11 — Exibir dados em tabelas na interface gráfica.

## 10. Requisitos Não Funcionais
- RNF01 — Aplicação desenvolvida em Java.
- RNF02 — Acesso ao banco por JDBC.
- RNF03 — Banco de dados relacional MySQL (conforme os arquivos e o conector fornecidos).
- RNF04 — Interface desktop utilizando Swing/JFrame.
- RNF05 — Uso de chaves primárias e estrangeiras para integridade dos relacionamentos.
- RNF06 — Operações SQL parametrizadas com PreparedStatement sempre que houver entrada do usuário.
- RNF07 — Mensagens de erro e confirmação compreensíveis ao usuário.
- RNF08 — Código organizado em packages/classes por responsabilidade.

## 11. Estrutura Geral do Sistema
Módulos independentes propostos:
- Aluno
- Treinador
- Turma
- Matrícula
- Pagamento
- Conexão/infraestrutura JDBC

## 12–14. Base para DER, Modelo Lógico e MER
Entidades propostas em singular: ALUNO, TREINADOR, TURMA, MATRICULA e PAGAMENTO.

Relacionamentos principais:
- TREINADOR ministra TURMA — um treinador pode ministrar uma ou mais turmas; cada turma deve possuir um treinador responsável.
- ALUNO realiza MATRICULA — um aluno pode realizar uma ou mais matrículas; cada matrícula pertence a um aluno.
- TURMA recebe MATRICULA — uma turma pode receber várias matrículas; cada matrícula pertence a uma turma.
- MATRICULA possui PAGAMENTO — uma matrícula pode possuir vários pagamentos; cada pagamento pertence a uma matrícula.

Modelo lógico inicial:
- aluno(id_aluno PK, nome, data_nascimento, telefone, email, responsavel)
- treinador(id_treinador PK, nome, telefone, email, especialidade)
- turma(id_turma PK, nome, faixa_etaria, dia_semana, horario, capacidade, id_treinador FK)
- matricula(id_matricula PK, data_matricula, situacao, id_aluno FK, id_turma FK)
- pagamento(id_pagamento PK, vencimento, data_pagamento, valor, situacao, id_matricula FK)

> A modelagem definitiva deve manter os nomes de tabela/entidade no singular, conforme a convenção fornecida em aula.

## Cronograma do professor
- Parte 1 / sistema exemplo: 13/08/2026.
- Itens 2 a 11: 27/08/2026.
- DER: 03/09/2026.
- Modelo Lógico Relacional: 08/09/2026.
- MER: 10/09/2026.
- Script DDL: 17/09/2026.
- Diagrama de Classes UML: 24/09/2026.
- Apresentação do sistema: 24 e 26/11/2026.
