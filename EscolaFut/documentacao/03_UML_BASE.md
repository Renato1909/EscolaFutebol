# Diagrama de Classes UML — Base

A generalização foi representada pela classe abstrata `Pessoa`, da qual `Aluno` e `Treinador` herdam atributos comuns. As associações do domínio seguem o banco: Treinador–Turma, Aluno–Matricula, Turma–Matricula e Matricula–Pagamento.

```mermaid
classDiagram
    class Pessoa {
      <<abstract>>
      -int id
      -String nome
      -String telefone
      -String email
      +getId() int
      +setId(int)
      +getNome() String
      +setNome(String)
    }
    class Aluno {
      -LocalDate dataNascimento
      -String responsavel
    }
    class Treinador {
      -String especialidade
    }
    class Turma {
      -int idTurma
      -String nome
      -String faixaEtaria
      -String diaSemana
      -LocalTime horario
      -int capacidade
      -int idTreinador
    }
    class Matricula {
      -int idMatricula
      -LocalDate dataMatricula
      -String situacao
      -int idAluno
      -int idTurma
    }
    class Pagamento {
      -int idPagamento
      -LocalDate vencimento
      -LocalDate dataPagamento
      -BigDecimal valor
      -String situacao
      -int idMatricula
    }
    Pessoa <|-- Aluno
    Pessoa <|-- Treinador
    Treinador "1" --> "0..*" Turma : ministra
    Aluno "1" --> "0..*" Matricula : realiza
    Turma "1" --> "0..*" Matricula : recebe
    Matricula "1" *-- "0..*" Pagamento : possui
```
