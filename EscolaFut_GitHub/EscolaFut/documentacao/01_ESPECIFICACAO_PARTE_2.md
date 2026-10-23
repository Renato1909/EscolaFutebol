# ESTUDO DE CASO — Sistema de Gerenciamento Escola de Futebol
## Escola de Futebol Craques do Futuro — Parte 2

### 1. Introdução — Estudo de Caso
A Escola de Futebol Craques do Futuro oferece treinamento esportivo para crianças, adolescentes e adultos. Atualmente, as informações dos alunos são registradas em planilhas, o que dificulta o controle das matrículas, pagamentos, turmas e treinadores. Com o aumento da quantidade de alunos, torna-se necessária a informatização das rotinas administrativas. O sistema proposto será desenvolvido em Java, utilizará JDBC para comunicação com um banco de dados relacional SQL Server e disponibilizará operações de cadastro, consulta, atualização e exclusão das principais informações.

### 2. Objetivo Geral
Desenvolver um sistema desktop para centralizar e organizar o gerenciamento da Escola de Futebol Craques do Futuro, permitindo controlar alunos, treinadores, turmas, matrículas e pagamentos de forma integrada por meio de Java, JDBC e banco de dados relacional.

### 3. Objetivos Específicos
- Cadastrar, consultar, atualizar e excluir alunos.
- Cadastrar, consultar, atualizar e excluir treinadores.
- Criar e manter turmas, incluindo faixa etária, dia, horário, capacidade e treinador responsável.
- Registrar matrículas ligando aluno e turma.
- Controlar a situação de cada matrícula.
- Registrar e consultar pagamentos vinculados às matrículas.
- Oferecer pesquisas e tabelas para facilitar a localização de registros.
- Manter integridade e consistência entre as informações relacionadas.

### 4. Justificativa
O uso de planilhas isoladas aumenta a possibilidade de duplicidade, perda e inconsistência de informações e dificulta relacionar os dados administrativos da escola. Um sistema informatizado permite centralizar os registros, reduzir retrabalho, consultar informações com maior rapidez e manter vínculos claros entre alunos, turmas, treinadores, matrículas e pagamentos.

### 5. Problema
A escola não possui atualmente um sistema integrado para controlar os seus dados. As informações distribuídas em planilhas dificultam a atualização, a pesquisa e o acompanhamento das matrículas e pagamentos, além de tornar mais trabalhoso verificar quais alunos pertencem a cada turma e qual treinador é responsável por cada uma delas.

### 6. Solução Proposta
Desenvolver uma aplicação desktop em Java Swing com conexão JDBC ao SQL Server. O sistema será dividido em módulos independentes, mas integrados pelo banco de dados. Cada módulo oferecerá as operações necessárias de cadastro e manutenção, e os relacionamentos serão implementados com chaves primárias e estrangeiras.

### 7. Escopo do Sistema
**Incluído:** aluno, treinador, turma, matrícula, pagamento, pesquisa/listagem, interface Swing, JDBC e SQL Server.

**Fora do escopo nesta versão:** aplicativo móvel, portal web público, integração bancária real, emissão fiscal, catraca eletrônica e serviços externos.

### 8. Regras de Negócio
- RN01 — Cada aluno deve possuir um identificador único.
- RN02 — Cada treinador deve possuir um identificador único.
- RN03 — Cada turma deve possuir exatamente um treinador responsável.
- RN04 — Cada matrícula deve relacionar exatamente um aluno e uma turma.
- RN05 — Um aluno não pode possuir duas matrículas para a mesma turma.
- RN06 — Matrículas devem possuir situação controlada (ATIVA, CANCELADA ou TRANCADA).
- RN07 — A capacidade da turma deve ser maior que zero.
- RN08 — Um pagamento deve pertencer a uma matrícula existente.
- RN09 — O valor do pagamento não pode ser negativo.
- RN10 — Registros referenciados por outros registros não devem ser excluídos quando a exclusão quebrar a integridade referencial.
- RN11 — Dados obrigatórios devem ser preenchidos antes da gravação.
- RN12 — Exclusões devem ser confirmadas pelo usuário.

### 9. Requisitos Funcionais
- RF01 — Cadastrar aluno.
- RF02 — Consultar e pesquisar aluno por nome.
- RF03 — Atualizar aluno.
- RF04 — Excluir aluno quando permitido pelas regras de integridade.
- RF05 — Cadastrar, consultar, atualizar e excluir treinador.
- RF06 — Cadastrar, consultar, atualizar e excluir turma.
- RF07 — Registrar matrícula.
- RF08 — Consultar matrículas.
- RF09 — Alterar a situação de matrícula.
- RF10 — Excluir matrícula quando permitido.
- RF11 — Registrar pagamento.
- RF12 — Consultar pagamentos.
- RF13 — Atualizar pagamento.
- RF14 — Excluir pagamento quando permitido.
- RF15 — Exibir registros em tabelas nas interfaces gráficas.
- RF16 — Navegar entre os módulos por um menu principal.

### 10. Requisitos Não Funcionais
- RNF01 — O sistema será implementado em Java.
- RNF02 — A interface gráfica será desktop, utilizando Swing/JFrame.
- RNF03 — O banco de dados será relacional SQL Server.
- RNF04 — A conexão será realizada através de JDBC e Microsoft JDBC Driver for SQL Server.
- RNF05 — O banco deverá utilizar chaves primárias e estrangeiras.
- RNF06 — Comandos com dados informados pelo usuário devem utilizar PreparedStatement.
- RNF07 — O sistema deve exibir mensagens compreensíveis em situações de erro.
- RNF08 — O código será organizado em packages por responsabilidade: conexao, controle, dao e modelo.
- RNF09 — As tabelas deverão utilizar o mecanismo InnoDB para suportar integridade referencial.
- RNF10 — O sistema deverá poder ser executado no ambiente Java/NetBeans utilizado nas aulas.

### 11. Estrutura Geral do Sistema
O sistema é dividido nos módulos **Aluno**, **Treinador**, **Turma**, **Matrícula**, **Pagamento** e **Conexão/Infraestrutura JDBC**. A interface inicial (`MenuPrincipal`) dá acesso às telas de cada módulo. As classes de interface ficam em `controle`, os objetos de domínio em `modelo`, as operações SQL em `dao` e a conexão com SQL Server em `conexao`.
