# EscolaFut — Parte 1 (Java + JDBC + MySQL)

Projeto organizado a partir do exemplo fornecido pelo professor.

## O que foi implementado
- Formulário Swing/JFrame no package `controle`.
- Package `conexao` com a classe `Conexao`.
- Driver JDBC MySQL Connector/J 8.0.31 dentro de `lib/`.
- Conexão com o banco MySQL `clientes` em `127.0.0.1:3306`.
- JTable não editável com Código, Nome, Data Nascimento, Telefone e Email.
- CRUD: cadastrar, consultar/listar, atualizar e excluir.
- Pesquisa dinâmica por nome.
- Navegação: primeiro, anterior, próximo e último registro.
- Uso de `PreparedStatement` nas operações de escrita e pesquisa.

## Antes de executar
1. Inicie o MySQL (XAMPP/EasyPHP/WAMP ou serviço MySQL local).
2. Importe `bd_cliente.sql` no MySQL/phpMyAdmin.
3. Abra a pasta `EscolaFut` no NetBeans.
4. Confira se `lib/mysql-connector-j-8.0.31.jar` aparece nas bibliotecas do projeto.
5. Execute `controle.Tela`.

## Configuração padrão
- Banco: `clientes`
- Host: `127.0.0.1`
- Porta: `3306`
- Usuário: `root`
- Senha: vazia

Caso sua instalação tenha senha, altere `USUARIO` e `SENHA` em `src/conexao/Conexao.java`.

## Observação importante
O material antigo usa `com.mysql.jdbc.Driver`. Com o Connector/J 8.0.31 fornecido, o projeto usa `com.mysql.cj.jdbc.Driver`, equivalente atual para o driver MySQL.
