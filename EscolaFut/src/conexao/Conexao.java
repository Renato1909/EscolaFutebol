package conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/** Classe responsável pela conexão JDBC com o banco da Escola de Futebol. */
public class Conexao {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/escola_futebol?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo";
    private static final String USUARIO = "root";
    private static final String SENHA = "Ras2234588909#*_";

    private Connection conexao;
    public Statement statement;
    public ResultSet resultset;

    public boolean conecta() {
        try {
            Class.forName(DRIVER);
            conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            return true;
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Driver JDBC não localizado: " + ex.getMessage(), "Mensagem do Programa", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Banco de dados não localizado ou acesso recusado: " + ex.getMessage(), "Mensagem do Programa", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    public void desconecta() {
        try {
            if (resultset != null && !resultset.isClosed()) resultset.close();
            if (statement != null && !statement.isClosed()) statement.close();
            if (conexao != null && !conexao.isClosed()) conexao.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao fechar conexão: " + ex.getMessage());
        }
    }

    public void executaSQL(String sql) throws SQLException {
        statement = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        resultset = statement.executeQuery(sql);
    }

    public PreparedStatement preparaSQL(String sql) throws SQLException {
        return conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    }

    public Connection getConexao() {
        return conexao;
    }
}
