package modelo;

import java.time.LocalTime;

public class Turma {
    private int idTurma;
    private String nome;
    private String faixaEtaria;
    private String diaSemana;
    private LocalTime horario;
    private int capacidade;
    private int idTreinador;

    public int getIdTurma() { return idTurma; }
    public void setIdTurma(int idTurma) { this.idTurma = idTurma; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getFaixaEtaria() { return faixaEtaria; }
    public void setFaixaEtaria(String faixaEtaria) { this.faixaEtaria = faixaEtaria; }
    public String getDiaSemana() { return diaSemana; }
    public void setDiaSemana(String diaSemana) { this.diaSemana = diaSemana; }
    public LocalTime getHorario() { return horario; }
    public void setHorario(LocalTime horario) { this.horario = horario; }
    public int getCapacidade() { return capacidade; }
    public void setCapacidade(int capacidade) { this.capacidade = capacidade; }
    public int getIdTreinador() { return idTreinador; }
    public void setIdTreinador(int idTreinador) { this.idTreinador = idTreinador; }
}
