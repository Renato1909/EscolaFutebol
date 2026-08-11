package dao;

import conexao.Conexao;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Aluno;

public class AlunoDAO {
    private final Conexao conexao;
    public AlunoDAO(Conexao conexao) { this.conexao = conexao; }

    public void inserir(Aluno a) throws SQLException {
        String sql = "INSERT INTO aluno(nome,data_nascimento,telefone,email,responsavel) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conexao.preparaSQL(sql)) {
            ps.setString(1, a.getNome()); ps.setDate(2, Date.valueOf(a.getDataNascimento()));
            ps.setString(3, a.getTelefone()); ps.setString(4, a.getEmail()); ps.setString(5, a.getResponsavel());
            ps.executeUpdate();
        }
    }

    public void atualizar(Aluno a) throws SQLException {
        String sql = "UPDATE aluno SET nome=?,data_nascimento=?,telefone=?,email=?,responsavel=? WHERE id_aluno=?";
        try (PreparedStatement ps = conexao.preparaSQL(sql)) {
            ps.setString(1, a.getNome()); ps.setDate(2, Date.valueOf(a.getDataNascimento()));
            ps.setString(3, a.getTelefone()); ps.setString(4, a.getEmail()); ps.setString(5, a.getResponsavel()); ps.setInt(6, a.getId());
            ps.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        try (PreparedStatement ps = conexao.preparaSQL("DELETE FROM aluno WHERE id_aluno=?")) { ps.setInt(1, id); ps.executeUpdate(); }
    }

    public List<Aluno> listar(String nome) throws SQLException {
        List<Aluno> lista = new ArrayList<>();
        try (PreparedStatement ps = conexao.preparaSQL("SELECT * FROM aluno WHERE nome LIKE ? ORDER BY nome")) {
            ps.setString(1, "%" + nome + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Aluno a = new Aluno(); a.setId(rs.getInt("id_aluno")); a.setNome(rs.getString("nome"));
                    a.setDataNascimento(rs.getDate("data_nascimento").toLocalDate()); a.setTelefone(rs.getString("telefone"));
                    a.setEmail(rs.getString("email")); a.setResponsavel(rs.getString("responsavel")); lista.add(a);
                }
            }
        }
        return lista;
    }
}
