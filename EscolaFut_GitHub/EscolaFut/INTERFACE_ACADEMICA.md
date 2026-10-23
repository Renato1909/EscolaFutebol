# Interface acadêmica do EscolaFut

Esta versão foi ajustada para permanecer próxima do conteúdo apresentado nas aulas.

## Componentes usados

As telas utilizam apenas componentes comuns do Java Swing:

- `JFrame` para as janelas;
- `JPanel` para organização dos componentes;
- `JLabel` para rótulos;
- `JTextField` para entrada de dados;
- `JButton` para as ações;
- `JTable` dentro de `JScrollPane` para exibição dos registros;
- `JOptionPane` para mensagens e confirmações.

O material da disciplina apresenta justamente a criação de um JFrame com labels, campos de texto e JTable, além de uma classe de conexão JDBC. Por isso, nesta versão não são usados componentes visuais externos nem bibliotecas de interface modernas.

## Botões e ícones

Os botões são `JButton` comuns. Os ícones são arquivos PNG pequenos armazenados em:

`src/controle/icones/`

A classe `Icones.java` apenas carrega esses arquivos e os associa aos botões. Não existe desenho de ícones via Java2D, animação, efeito de hover ou biblioteca de terceiros.

No menu principal:

- Alunos -> ícone de aluno;
- Treinadores -> ícone de treinador;
- Turmas -> ícone de grupo;
- Matrículas -> ícone de documento;
- Pagamentos -> ícone de pagamento;
- Sair -> ícone de saída.

Nas telas de cadastro são usados os botões:

- Novo;
- Salvar;
- Editar;
- Excluir;
- Fechar.

Essa separação deixa as operações CRUD mais fáceis de demonstrar e explicar.

## Organização do código

O fluxo básico é:

`JFrame -> evento do JButton -> DAO -> Conexao/JDBC -> SQL Server`

Exemplo no cadastro de aluno:

1. O usuário preenche os `JTextField`.
2. O botão Salvar chama o método `salvar()`.
3. O método cria um objeto `Aluno`.
4. `AlunoDAO.inserir()` prepara e executa o `INSERT`.
5. `Conexao` fornece a conexão JDBC com o SQL Server.
6. A JTable é atualizada para mostrar os dados gravados.

## Sobre o editor visual do NetBeans

A tela original da Parte 1 (`Tela.java`) continua acompanhada de `Tela.form` e pode ser aberta no editor visual do NetBeans.

As telas novas da Parte 2 são JFrames Swing convencionais escritos de forma explícita em Java para que a equipe consiga ler e explicar facilmente como os componentes são montados. Elas não dependem de código visual personalizado ou de frameworks externos.
