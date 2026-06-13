package errorHandler;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;


public final class GerenciadorExcecoes {

    private GerenciadorExcecoes() {}

    public static void tratar(Component parent, Exception ex) {
        if (ex instanceof IllegalArgumentException) {
            mostrarAviso(parent, ex.getMessage());

        } else if (ex instanceof NumberFormatException) {
            mostrarAviso(parent, "Valor numérico inválido. Verifique os campos e tente novamente.");

        } else if (ex instanceof java.io.IOException) {
            mostrarErro(parent, "Erro ao acessar arquivo: " + ex.getMessage());

        } else if (ex instanceof RuntimeException && ex.getCause() instanceof SQLException) {
            mostrarErro(parent, "Erro no banco de dados: " + ex.getCause().getMessage());

        } else {
            mostrarErro(parent, "Erro inesperado: " + ex.getMessage());
        }
        ex.printStackTrace();
    }

    private static void mostrarAviso(Component parent, String mensagem) {
        JOptionPane.showMessageDialog(parent, mensagem, "Dados inválidos", JOptionPane.WARNING_MESSAGE);
    }

    private static void mostrarErro(Component parent, String mensagem) {
        JOptionPane.showMessageDialog(parent, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}