package dao;

import conexao.Conexao;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Matricula;

public class MatriculaDAO {
    private final Conexao conexao;
    public MatriculaDAO(Conexao conexao){this.conexao=conexao;}
    public void inserir(Matricula m)throws SQLException{try(PreparedStatement ps=conexao.preparaSQL("INSERT INTO matricula(data_matricula,situacao,id_aluno,id_turma) VALUES (?,?,?,?)")){ps.setDate(1,Date.valueOf(m.getDataMatricula()));ps.setString(2,m.getSituacao());ps.setInt(3,m.getIdAluno());ps.setInt(4,m.getIdTurma());ps.executeUpdate();}}
    public void atualizarSituacao(int id,String situacao)throws SQLException{try(PreparedStatement ps=conexao.preparaSQL("UPDATE matricula SET situacao=? WHERE id_matricula=?")){ps.setString(1,situacao);ps.setInt(2,id);ps.executeUpdate();}}
    public void excluir(int id)throws SQLException{try(PreparedStatement ps=conexao.preparaSQL("DELETE FROM matricula WHERE id_matricula=?")){ps.setInt(1,id);ps.executeUpdate();}}
    public List<Matricula> listar()throws SQLException{List<Matricula> l=new ArrayList<>();try(PreparedStatement ps=conexao.preparaSQL("SELECT * FROM matricula ORDER BY id_matricula DESC");ResultSet rs=ps.executeQuery()){while(rs.next()){Matricula m=new Matricula();m.setIdMatricula(rs.getInt("id_matricula"));m.setDataMatricula(rs.getDate("data_matricula").toLocalDate());m.setSituacao(rs.getString("situacao"));m.setIdAluno(rs.getInt("id_aluno"));m.setIdTurma(rs.getInt("id_turma"));l.add(m);}}return l;}
}
