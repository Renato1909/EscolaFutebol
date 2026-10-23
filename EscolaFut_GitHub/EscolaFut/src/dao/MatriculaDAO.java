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

    public MatriculaDAO(Conexao conexao) {
        this.conexao = conexao;
    }

    public void inserir(Matricula m) throws SQLException {
        String sql = "INSERT INTO dbo.matricula(data_matricula,situacao,id_aluno,id_turma) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conexao.preparaSQL(sql)) {
            ps.setDate(1, Date.valueOf(m.getDataMatricula()));
            ps.setString(2, m.getSituacao());
            ps.setInt(3, m.getIdAluno());
            ps.setInt(4, m.getIdTurma());
            ps.executeUpdate();
        }
    }

    public void atualizar(Matricula m) throws SQLException {
        String sql = "UPDATE dbo.matricula SET data_matricula=?, situacao=?, id_aluno=?, id_turma=? WHERE id_matricula=?";
        try (PreparedStatement ps = conexao.preparaSQL(sql)) {
            ps.setDate(1, Date.valueOf(m.getDataMatricula()));
            ps.setString(2, m.getSituacao());
            ps.setInt(3, m.getIdAluno());
            ps.setInt(4, m.getIdTurma());
            ps.setInt(5, m.getIdMatricula());
            ps.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        try (PreparedStatement ps = conexao.preparaSQL("DELETE FROM dbo.matricula WHERE id_matricula=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Matricula> listar() throws SQLException {
        List<Matricula> lista = new ArrayList<>();
        String sql = "SELECT * FROM dbo.matricula ORDER BY id_matricula DESC";
        try (PreparedStatement ps = conexao.preparaSQL(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Matricula m = new Matricula();
                m.setIdMatricula(rs.getInt("id_matricula"));
                m.setDataMatricula(rs.getDate("data_matricula").toLocalDate());
                m.setSituacao(rs.getString("situacao"));
                m.setIdAluno(rs.getInt("id_aluno"));
                m.setIdTurma(rs.getInt("id_turma"));
                lista.add(m);
            }
        }
        return lista;
    }
}
