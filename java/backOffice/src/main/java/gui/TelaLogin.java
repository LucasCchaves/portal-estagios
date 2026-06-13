package gui;

import dao.DatabaseConnection;
import dao.UsuarioDAO;
import model.Usuario;
import services.UsuarioService;
import errorHandler.GerenciadorExcecoes;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class TelaLogin extends JFrame {

    private final UsuarioService service;

    private JTextField     txtEmail;
    private JPasswordField txtSenha;

    public TelaLogin() {
        try {
            Connection conn = DatabaseConnection.getConexao();
            this.service = new UsuarioService(new UsuarioDAO(conn));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao conectar ao banco: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }

        setTitle("Portal de Estágios - UniALFA");
        setSize(380, 260);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        add(montarTopo(),       BorderLayout.NORTH);
        add(montarFormulario(), BorderLayout.CENTER);
        add(montarRodape(),     BorderLayout.SOUTH);
    }

    private JLabel montarTopo() {
        JLabel lbl = new JLabel("UniALFA — Portal de Estágios", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 15));
        lbl.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        return lbl;
    }

    private JPanel montarFormulario() {
        txtEmail = new JTextField(20);
        txtSenha = new JPasswordField(20);

        txtSenha.addActionListener(e -> autenticar());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        GridBagConstraints c = new GridBagConstraints();
        c.fill    = GridBagConstraints.HORIZONTAL;
        c.insets  = new Insets(6, 8, 6, 8);
        c.weightx = 1;

        c.gridx = 0; c.gridy = 0; form.add(new JLabel("Email:"), c);
        c.gridx = 0; c.gridy = 1; form.add(txtEmail, c);
        c.gridx = 0; c.gridy = 2; form.add(new JLabel("Senha:"), c);
        c.gridx = 0; c.gridy = 3; form.add(txtSenha, c);

        return form;
    }

    private JPanel montarRodape() {
        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.addActionListener(e -> autenticar());

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        rodape.add(btnEntrar);
        return rodape;
    }

    private void autenticar() {
        String email = txtEmail.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();

        if (email.isBlank() || senha.isBlank()) {
            JOptionPane.showMessageDialog(this,
                    "Preencha email e senha.", "Dados inválidos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Usuario usuario = service.autenticar(email, senha);
            dispose();
            new TelaPrincipal(usuario.getNome()).setVisible(true);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(), "Acesso negado",
                    JOptionPane.WARNING_MESSAGE);
            txtSenha.setText("");
        } catch (Exception ex) {
            GerenciadorExcecoes.tratar(this, ex);
        }
    }
}