package dao;

import conexao.Conexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import modelo.Turma;

public class TurmaDAO {
    private final Conexao conexao;
    public TurmaDAO(Conexao conexao){this.conexao=conexao;}
    public void inserir(Turma t)throws SQLException{ try(PreparedStatement ps=conexao.preparaSQL("INSERT INTO turma(nome,faixa_etaria,dia_semana,horario,capacidade,id_treinador) VALUES (?,?,?,?,?,?)")){ preencher(ps,t); ps.executeUpdate(); } }
    public void atualizar(Turma t)throws SQLException{ try(PreparedStatement ps=conexao.preparaSQL("UPDATE turma SET nome=?,faixa_etaria=?,dia_semana=?,horario=?,capacidade=?,id_treinador=? WHERE id_turma=?")){ preencher(ps,t); ps.setInt(7,t.getIdTurma()); ps.executeUpdate(); } }
    private void preencher(PreparedStatement ps,Turma t)throws SQLException{ ps.setString(1,t.getNome());ps.setString(2,t.getFaixaEtaria());ps.setString(3,t.getDiaSemana());ps.setTime(4,Time.valueOf(t.getHorario()));ps.setInt(5,t.getCapacidade());ps.setInt(6,t.getIdTreinador()); }
    public void excluir(int id)throws SQLException{try(PreparedStatement ps=conexao.preparaSQL("DELETE FROM turma WHERE id_turma=?")){ps.setInt(1,id);ps.executeUpdate();}}
    public List<Turma> listar()throws SQLException{List<Turma> l=new ArrayList<>();try(PreparedStatement ps=conexao.preparaSQL("SELECT * FROM turma ORDER BY nome");ResultSet rs=ps.executeQuery()){while(rs.next()){Turma t=new Turma();t.setIdTurma(rs.getInt("id_turma"));t.setNome(rs.getString("nome"));t.setFaixaEtaria(rs.getString("faixa_etaria"));t.setDiaSemana(rs.getString("dia_semana"));t.setHorario(rs.getTime("horario").toLocalTime());t.setCapacidade(rs.getInt("capacidade"));t.setIdTreinador(rs.getInt("id_treinador"));l.add(t);}}return l;}
}
