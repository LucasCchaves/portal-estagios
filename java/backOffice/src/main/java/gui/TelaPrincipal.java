package gui;

import dao.*;
import services.*;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class TelaPrincipal extends JFrame {

    private  String usuarioLogado;

    private IAlunoService       alunoService;
    private IEmpresaService     empresaService;
    private IVagaService        vagaService;
    private ICandidaturaService candidaturaService;

    public TelaPrincipal(String usuarioLogado) {
        this.usuarioLogado = usuarioLogado;

        try {
            Connection conn = DatabaseConnection.getConexao(); // reusa a conexão já aberta
            this.alunoService       = new AlunoService      (new AlunoDAO(conn));
            this.empresaService     = new EmpresaService    (new EmpresaDAO(conn));
            this.vagaService        = new VagaService       (new VagaDAO(conn));
            this.candidaturaService = new CandidaturaService(new CandidaturaDAO(conn));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao conectar ao banco de dados: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }
        setTitle("Portal de Estágios - UniALFA");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(montarMenuBar(), BorderLayout.NORTH);
        add(montarCentro(),  BorderLayout.CENTER);
        add(montarRodape(),  BorderLayout.SOUTH);
    }

    private JMenuBar montarMenuBar() {
        JMenuBar bar = new JMenuBar();
        bar.setLayout(new BorderLayout());

        JLabel lblLogo = new JLabel("  UniALFA");
        lblLogo.setFont(new Font("Arial", Font.BOLD, 14));

        JButton btnSair = new JButton("Sair");
        btnSair.setForeground(new Color(163, 45, 45));
        btnSair.setBackground(UIManager.getColor("MenuBar.background"));
        btnSair.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(240, 149, 149), 1, true),
                BorderFactory.createEmptyBorder(4, 12, 4, 12)
        ));
        btnSair.setFocusPainted(false);
        btnSair.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSair.addActionListener(e -> confirmarSaida());

        JPanel direita = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        direita.setOpaque(false);
        direita.add(btnSair);

        bar.add(lblLogo, BorderLayout.WEST);
        bar.add(direita, BorderLayout.EAST);

        return bar;
    }

    private JPanel montarCentro() {
        JPanel wrapper = new JPanel(new BorderLayout());

        JLabel lblBemVindo = new JLabel("Bem-vindo, " + usuarioLogado + "!", SwingConstants.CENTER);
        lblBemVindo.setFont(new Font("Arial", Font.PLAIN, 14));
        lblBemVindo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        wrapper.add(lblBemVindo, BorderLayout.NORTH);

        JPanel grade = new JPanel(new GridLayout(0, 2, 20, 20));
        grade.setBorder(BorderFactory.createEmptyBorder(20, 60, 40, 60));

        grade.add(criarBotaoAtalho("👨‍🎓  Alunos",       "Cadastrar, editar e importar alunos",
                () -> new TelaAlunos(alunoService).setVisible(true)));

        grade.add(criarBotaoAtalho("🏢  Empresas",      "Aprovar, bloquear e consultar empresas",
                () -> new TelaEmpresa(empresaService).setVisible(true)));

        grade.add(criarBotaoAtalho("💼  Vagas",          "Consultar vagas cadastradas",
                () -> new TelaVagas(vagaService).setVisible(true)));

        grade.add(criarBotaoAtalho("📋  Candidaturas",  "Visualizar candidaturas e status",
                () -> new TelaCandidaturas(candidaturaService).setVisible(true)));

        grade.add(criarBotaoAtalho("📊  Relatórios",    "Gerar relatórios em .txt",
                () -> new TelaRelatorios(alunoService, empresaService, vagaService, candidaturaService, "Alunos").setVisible(true)));

        wrapper.add(grade, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel montarRodape() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 6));
        rodape.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        rodape.add(new JLabel("UniALFA — Portal de Estágios  |  Back Office Institucional"));
        return rodape;
    }

    private JPanel criarBotaoAtalho(String titulo, String descricao, Runnable acao) {
        JPanel card = new JPanel(new BorderLayout(0, 6));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel lblDesc = new JLabel("<html><center>" + descricao + "</center></html>", SwingConstants.CENTER);
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 11));
        lblDesc.setForeground(Color.GRAY);

        card.add(lblTitulo, BorderLayout.CENTER);
        card.add(lblDesc,   BorderLayout.SOUTH);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) { acao.run(); }
            public void mouseEntered(java.awt.event.MouseEvent e) { card.setBackground(new Color(240, 248, 255)); }
            public void mouseExited (java.awt.event.MouseEvent e) { card.setBackground(UIManager.getColor("Panel.background")); }
        });

        return card;
    }

    private void confirmarSaida() {
        int res = JOptionPane.showConfirmDialog(this,
                "Deseja encerrar o sistema?", "Sair",
                JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
            DatabaseConnection.fechar();
            System.exit(0);
        }
    }
}