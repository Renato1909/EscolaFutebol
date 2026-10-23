package dao;

import conexao.Conexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Treinador;

public class TreinadorDAO {
    private final Conexao conexao;
    public TreinadorDAO(Conexao conexao) { this.conexao = conexao; }

    public void inserir(Treinador t) throws SQLException {
        try (PreparedStatement ps = conexao.preparaSQL("INSERT INTO dbo.treinador(nome,telefone,email,especialidade) VALUES (?,?,?,?)")) {
            ps.setString(1,t.getNome()); ps.setString(2,t.getTelefone()); ps.setString(3,t.getEmail()); ps.setString(4,t.getEspecialidade()); ps.executeUpdate();
        }
    }
    public void atualizar(Treinador t) throws SQLException {
        try (PreparedStatement ps = conexao.preparaSQL("UPDATE dbo.treinador SET nome=?,telefone=?,email=?,especialidade=? WHERE id_treinador=?")) {
            ps.setString(1,t.getNome()); ps.setString(2,t.getTelefone()); ps.setString(3,t.getEmail()); ps.setString(4,t.getEspecialidade()); ps.setInt(5,t.getId()); ps.executeUpdate();
        }
    }
    public void excluir(int id) throws SQLException { try (PreparedStatement ps=conexao.preparaSQL("DELETE FROM dbo.treinador WHERE id_treinador=?")){ ps.setInt(1,id); ps.executeUpdate(); } }
    public List<Treinador> listar(String nome) throws SQLException {
        List<Treinador> lista=new ArrayList<>();
        try(PreparedStatement ps=conexao.preparaSQL("SELECT * FROM dbo.treinador WHERE nome LIKE ? ORDER BY nome")){ ps.setString(1,"%"+nome+"%"); try(ResultSet rs=ps.executeQuery()){ while(rs.next()){ Treinador t=new Treinador(); t.setId(rs.getInt("id_treinador")); t.setNome(rs.getString("nome")); t.setTelefone(rs.getString("telefone")); t.setEmail(rs.getString("email")); t.setEspecialidade(rs.getString("especialidade")); lista.add(t); } } }
        return lista;
    }
}
