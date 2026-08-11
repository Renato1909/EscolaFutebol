# EscolaFut — Parte 2

## O que foi acrescentado
- Banco `escola_futebol` com ALUNO, TREINADOR, TURMA, MATRICULA e PAGAMENTO.
- Nova conexão JDBC apontando para `escola_futebol`.
- Packages `modelo` e `dao`.
- Classes de domínio e DAOs com PreparedStatement.
- Menu principal e telas CRUD básicas para os cinco módulos.
- Documentação dos itens 1 a 11 solicitados no estudo de caso.
- Base textual de DER/modelo lógico e UML para as entregas posteriores.

## Como executar
1. Inicie o MySQL.
2. Importe `database/escola_futebol.sql` no MySQL/phpMyAdmin/Workbench.
3. Confirme em `src/conexao/Conexao.java` se usuário e senha do MySQL correspondem ao seu ambiente.
4. Abra o projeto no NetBeans.
5. Execute `controle.MenuPrincipal`.

## Observação
A tela `controle.Tela` e o `bd_cliente.sql` foram mantidos no projeto como evidência/base da Parte 1. A Parte 2 utiliza o banco `escola_futebol` e começa por `MenuPrincipal`.
