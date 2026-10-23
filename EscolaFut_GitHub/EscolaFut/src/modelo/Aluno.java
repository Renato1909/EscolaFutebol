package modelo;

import java.time.LocalDate;

public class Aluno extends Pessoa {
    private LocalDate dataNascimento;
    private String responsavel;

    public Aluno() {}

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
    public String getResponsavel() { return responsavel; }
    public void setResponsavel(String responsavel) { this.responsavel = responsavel; }
}
