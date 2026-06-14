package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {

    private static final String URL      = "jdbc:mysql://localhost:3306/db_estagio?useSSL=false&serverTimezone=America/Sao_Paulo";
    private static final String USUARIO  = "root";
    private static final String SENHA    = "";

    private static Connection conexao;

    private DatabaseConnection() {}

    public static Connection getConexao() throws SQLException {
        if (conexao == null || conexao.isClosed()) {
            conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
        }
        return conexao;
    }

    public static void fechar() {
        if (conexao != null) {
            try {
                conexao.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            } finally {
                conexao = null;
            }
        }
    }
}