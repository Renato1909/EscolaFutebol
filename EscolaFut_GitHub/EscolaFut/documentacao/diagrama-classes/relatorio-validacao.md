# Relatório de validação — diagrama de classes do domínio

Data: 21/10/2026.

## Versão analisada e escopo

A referência desta entrega é exclusivamente a pasta
`C:\Users\Usuário\Desktop\ETEC\Projeto DS\EscolaFut_SQLServerFuncionando`.
O `pom.xml` define `src` como diretório de código-fonte, Java 21 e o driver JDBC do SQL Server. Foram examinados os 21 arquivos Java dessa pasta e o script `database/escola_futebol.sql`.

As pastas irmãs em `../backups` (`EscolaFut (2)`, `EscolaFut_SQLServer` e `EscolaFut_SQLServer - Copia`) são versões alternativas e não foram usadas para compor este modelo. Classes compiladas em `target`, documentos UML anteriores e o arquivo corrompido `TelaTurma.java.corrupted.bak` não são fontes de modelagem. A seleção corresponde à versão principal usada nesta conversa; não constitui certificação de todas as funcionalidades em execução.

Inventário dos pacotes examinados:

| Pacote | Classes |
|---|---|
| `modelo` | Pessoa, Aluno, Treinador, Turma, Matricula, Pagamento |
| `dao` | AlunoDAO, TreinadorDAO, TurmaDAO, MatriculaDAO, PagamentoDAO |
| `controle` | MenuPrincipal, Tela, TelaAluno, TelaTreinador, TelaTurma, TelaMatricula, TelaPagamento, Icones, DataInput |
| `conexao` | Conexao |

O diagrama principal contém somente as seis classes de domínio. As outras 15 classes justificam a separação entre interface, persistência e modelo; não são entidades adicionais do domínio. Não foi criado um segundo diagrama arquitetural porque o escopo prioritário solicitado é o domínio.

## Conferência dos membros

Todas as classes do desenho pertencem ao pacote `modelo`. Os nomes dos atributos, tipos, visibilidades, operações, parâmetros e retornos foram extraídos dos arquivos reais. O arquivo `inventario-dominio.json` registra cada assinatura, sem abreviação.

| Classe | Atributos declarados | Operações declaradas | Evidência |
|---|---:|---:|---|
| Pessoa | 4 | 10 | `src/modelo/Pessoa.java` |
| Aluno | 2 | 5 | `src/modelo/Aluno.java` |
| Treinador | 1 | 3 | `src/modelo/Treinador.java` |
| Turma | 7 | 14 | `src/modelo/Turma.java` |
| Matricula | 5 | 10 | `src/modelo/Matricula.java` |
| Pagamento | 6 | 12 | `src/modelo/Pagamento.java` |
| **Total** | **25** | **54** | **50 métodos e 4 construtores explícitos** |

Os atributos são privados. Os getters e setters são públicos. `Pessoa` possui dois construtores protegidos, incluindo o construtor com os parâmetros `id`, `nome`, `telefone` e `email`. `Aluno` e `Treinador` possuem um construtor público explícito cada. Construtores padrão implícitos de Turma, Matricula e Pagamento foram omitidos; não foram inventadas declarações ausentes do código.

Os construtores são representados sem retorno, conforme a UML; os demais métodos mostram seus retornos, inclusive `void`. Os tipos Java `LocalDate`, `LocalTime` e `BigDecimal` foram preservados. Não foram criados métodos de cadastro, cálculo ou consulta nas entidades: o CRUD pertence aos DAOs, e a leitura/validação dos campos pertence às telas.

## Herança, abstração e interfaces

`Pessoa` é explicitamente abstrata. Seu nome aparece em negrito e itálico, e as duas generalizações usam linha contínua e triângulo vazio apontando para a superclasse.

`Aluno extends Pessoa` e `Treinador extends Pessoa` estão implementados no código. Por isso, `id`, `nome`, `telefone`, `email` e seus acessores são mostrados apenas em Pessoa. Nenhum atributo herdado é redeclarado nas subclasses. O atributo `nome` de Turma é próprio de Turma e não representa duplicação por herança.

Pessoa não declara métodos abstratos. Nenhuma das seis classes de domínio implementa interfaces nem sobrescreve métodos. Não há realização a desenhar. As sobrescritas em componentes anônimos Swing e listeners são detalhes das classes de interface excluídas deste desenho. `Tela extends TelaAluno` existe no pacote controle, mas não é uma herança de domínio.

Pessoa não corresponde a uma tabela própria: seus campos são persistidos nas tabelas Aluno e Treinador pelos respectivos DAOs. Isso não elimina a generalização Java.

## Associações e multiplicidades

**As associações representam vínculos de domínio persistidos por IDs, e não referências Java entre objetos.** Turma mantém `idTreinador: int`; Matricula mantém `idAluno: int` e `idTurma: int`; Pagamento mantém `idMatricula: int`. Não há campos do tipo Aluno, Turma, Matricula, Treinador ou coleções dessas entidades dentro das seis classes.

As linhas são contínuas e sem setas de navegabilidade. Ausência de seta significa navegabilidade não especificada; não afirma implementação bidirecional. Os rótulos `idAluno`, `idTreinador`, `idTurma` e `idMatricula` identificam o mecanismo de persistência, sem inventar nomes de propriedades de objetos.

| Associação | Multiplicidades | Evidência e interpretação |
|---|---|---|
| Treinador — Turma | Treinador `1`; Turma `0..*` | `Turma.id_treinador NOT NULL` com FK para a PK de Treinador; cada turma persistida tem um treinador. O DDL não exige que todo treinador tenha turmas e não torna essa FK única. |
| Aluno — Matricula | Aluno `1`; Matricula `0..*` | `Matricula.id_aluno NOT NULL` com FK para Aluno; cada matrícula persistida pertence a um aluno. Um aluno pode não ter matrículas ou ter várias. |
| Turma — Matricula | Turma `1`; Matricula `0..*` | `Matricula.id_turma NOT NULL` com FK para Turma; cada matrícula persistida pertence a uma turma. Uma turma pode existir sem matrículas e ter várias. |
| Matricula — Pagamento | Matricula `1`; Pagamento `0..*` | `Pagamento.id_matricula NOT NULL` com FK para Matricula; cada pagamento persistido pertence a uma matrícula. O código não cria automaticamente um pagamento ao cadastrar a matrícula. |

Os DAOs confirmam que os IDs são escritos com `setInt` e recuperados com `getInt`. As telas solicitam os códigos em campos de texto. Nenhuma classe-modelo carrega automaticamente os objetos relacionados.

Não foi acrescentada uma associação direta Aluno — Turma: o vínculo ocorre por Matricula, que tem atributos próprios. Matricula é uma classe comum implementada em Java; não foi convertida graficamente em uma association class UML. Também não foram inventadas classes Escola, Responsavel, Categoria, Usuario ou enums de situação.

## Restrições complementares e limites

- `UQ_Matricula_Aluno_Turma` torna único o par aluno/turma. Assim, o mesmo aluno não pode ter dois registros de matrícula para a mesma turma, independentemente da situação. Isso não reduz a multiplicidade geral de cada associação a `0..1`.
- `CK_Turma_Capacidade` impõe capacidade maior que zero. Não foi encontrada verificação da quantidade de matrículas em função da capacidade nas telas, DAOs ou DDL. Portanto, não se afirma que esse limite de lotação é aplicado.
- `CK_Pagamento_Valor` permite valor maior ou igual a zero.
- Situação de matrícula aceita ATIVA, CANCELADA e TRANCADA no DDL; situação de pagamento aceita PENDENTE, PAGO, ATRASADO e CANCELADO. No Java os dois atributos são `String`, não enums.
- `data_pagamento` é anulável. PagamentoDAO trata esse caso com `setNull` e leitura condicional; a tela permite deixá-lo vazio. O atributo continua `LocalDate` no desenho: a nulabilidade de um valor não é uma associação com multiplicidade `0..1`.
- As quatro FKs usam `ON DELETE NO ACTION`. Não há evidência de propriedade de ciclo de vida ou exclusão automática de partes. Não foram utilizados losangos de composição ou agregação. A política de exclusão, isoladamente, também não prova uma semântica de agregação.
- As multiplicidades descrevem **registros válidos persistidos sob o DDL analisado**. Os objetos Java são mutáveis, e um objeto recém-criado pode ter IDs iguais a zero e referências nulas antes da gravação. Não se afirma que essas invariantes sejam garantidas a todo instante em memória.
- Não houve consulta ao esquema instalado ou teste de execução do sistema nesta tarefa. Se o banco efetivo tiver sido criado com outro script, suas regras podem divergir. A evidência usada é o DDL do projeto principal.

## Formatos e reprodução

`dominio.puml` é a fonte PlantUML editável. **Não é arquivo nativo do Astah.** Nenhum arquivo `.asta` foi produzido ou apresentado como compatível: não foi encontrada instalação/API do Astah no ambiente.

O ambiente bloqueou os downloads do executável PlantUML. As imagens foram geradas **a partir da leitura do .puml** por `RenderizarDiagrama.java`, um renderizador local do subconjunto de sintaxe empregado. Ele lê membros e relacionamentos, mede textos com Java2D e usa o posicionamento manual de `layout.properties`. Não se trata do renderizador oficial PlantUML. O código e as coordenadas estão incluídos para reconstrução local sem bibliotecas externas.

O PlantUML oficial também pode ler a fonte para gerar as imagens; seu posicionamento automático pode diferir da composição manual desta entrega. O README contém ambos os caminhos de reprodução. Referência do formato: [PlantUML](https://plantuml.com/download).

## Verificação da entrega

- Seis classes reais; 25 atributos e 54 operações declaradas conferidos com o código.
- Duas generalizações, quatro associações e multiplicidades justificadas pelo DDL.
- Três compartimentos em cada caixa; ausência de ícones, gradientes, sombras ou cantos arredondados.
- Nome da classe abstrata em itálico; triângulo de generalização vazio; ausência de losangos e de setas de navegabilidade sem evidência.
- Verificação geométrica de sobreposição de caixas e passagem de linhas pelo interior de classes.
- Medição das linhas de texto com FontMetrics para evitar corte de membros; inspeção visual do PNG gerado.
- SVG de 1600 × 2430 unidades e PNG de 4800 × 7290 pixels, com metadados de 300 dpi.
- SVG e PNG gerados da mesma leitura da fonte. Para manter todos os acessores legíveis em impressão, recomenda-se página A3 ou inserção com espaço suficiente; o SVG permite ampliação sem perda.
- Integridade dos arquivos Java, do DDL e do pom.xml conferida pelo snapshot SHA-256 em `fontes-verificadas.json`. Todos os arquivos desta tarefa ficam nesta pasta; o sistema não foi alterado.

Os resultados automatizados constam em `validacao-geometria.txt` e `validacao-final.txt`.

