package conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/** Conexão JDBC com SQL Server para a base EscolaFutebol. */
public class   Conexao {
    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String URL = valorOuPadrao("ESCOLAFUT_SQLSERVER_URL",
            "jdbc:sqlserver://localhost:1433;databaseName=EscolaFutebol;encrypt=true;trustServerCertificate=true");
    private static final String USUARIO = System.getenv("ESCOLAFUT_SQLSERVER_USER");
    private static final String SENHA = System.getenv("ESCOLAFUT_SQLSERVER_PASSWORD");

    private Connection conexao;
    public Statement statement;
    public ResultSet resultset;

    private static String valorOuPadrao(String nome, String padrao) {
        String valor = System.getenv(nome);
        return valor == null || valor.isBlank() ? padrao : valor;
    }

    public boolean conecta() {
        try {
            if (USUARIO == null || USUARIO.isBlank() || SENHA == null) {
                throw new SQLException("Configure as variáveis ESCOLAFUT_SQLSERVER_USER e ESCOLAFUT_SQLSERVER_PASSWORD.");
            }
            Class.forName(DRIVER);
            conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            return true;
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Driver JDBC do SQL Server não localizado. Confira a biblioteca mssql-jdbc na pasta lib.", "Conexão com SQL Server", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Não foi possível conectar ao SQL Server: " + ex.getMessage(), "Conexão com SQL Server", JOptionPane.ERROR_MESSAGE);
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
