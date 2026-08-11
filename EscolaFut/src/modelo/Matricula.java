package modelo;

import java.time.LocalDate;

public class Matricula {
    private int idMatricula;
    private LocalDate dataMatricula;
    private String situacao;
    private int idAluno;
    private int idTurma;

    public int getIdMatricula() { return idMatricula; }
    public void setIdMatricula(int idMatricula) { this.idMatricula = idMatricula; }
    public LocalDate getDataMatricula() { return dataMatricula; }
    public void setDataMatricula(LocalDate dataMatricula) { this.dataMatricula = dataMatricula; }
    public String getSituacao() { return situacao; }
    public void setSituacao(String situacao) { this.situacao = situacao; }
    public int getIdAluno() { return idAluno; }
    public void setIdAluno(int idAluno) { this.idAluno = idAluno; }
    public int getIdTurma() { return idTurma; }
    public void setIdTurma(int idTurma) { this.idTurma = idTurma; }
}
