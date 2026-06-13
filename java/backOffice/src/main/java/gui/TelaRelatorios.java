package gui;

import errorHandler.GerenciadorExcecoes;
import model.Aluno;
import model.Candidatura;
import model.Empresa;
import model.Vaga;
import services.IAlunoService;
import services.ICandidaturaService;
import services.IEmpresaService;
import services.IVagaService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class TelaRelatorios extends JFrame implements PainelDefault {
    private final IAlunoService        alunoService;
    private final IEmpresaService      empresaService;
    private final IVagaService         vagaService;
    private final ICandidaturaService  candidaturaService;

    private JTable tabela;
    private DefaultTableModel modelo;
    private String tipoAtual;
    private final Map<String, Runnable> carregadores;
    public TelaRelatorios(IAlunoService alunoService, IEmpresaService empresaService,
                          IVagaService vagaService, ICandidaturaService candidaturaService,
                          String tipoInicial) {
        this.alunoService       = alunoService;
        this.empresaService     = empresaService;
        this.vagaService        = vagaService;
        this.candidaturaService = candidaturaService;

        this.carregadores = Map.of(
                "alunos",        this::carregarAlunos,
                "empresas",      this::carregarEmpresas,
                "vagas",         this::carregarVagas,
                "candidaturas",  this::carregarCandidaturas
        );
        setTitle("Relatórios - UniALFA");
        setSize(950, 580);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(montarTopo(),    BorderLayout.NORTH);
        add(montarTabela(),  BorderLayout.CENTER);
        add(montarRodape(),  BorderLayout.SOUTH);

        carregarRelatorio(tipoInicial);
    }
    private JPanel montarTopo() {
        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        topo.add(new JLabel("Relatório:"));

        String[] tipos = {"Alunos", "Empresas", "Vagas", "Candidaturas"};
        JComboBox<String> combo = new JComboBox<>(tipos);
        combo.addActionListener(e ->
                carregarRelatorio(((String) combo.getSelectedItem()).toLowerCase()));

        topo.add(combo);
        return topo;
    }
    private JScrollPane montarTabela() {
        modelo = new DefaultTableModel() {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(modelo);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getTableHeader().setReorderingAllowed(false);
        return new JScrollPane(tabela);
    }
    private JPanel montarRodape() {
        JButton btnTxt = new JButton("Exportar .txt");
        btnTxt.addActionListener(e -> exportarTxt());

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        rodape.add(btnTxt);
        return rodape;
    }
    private void carregarRelatorio(String tipo) {
        tipoAtual = tipo;
        modelo.setRowCount(0);

        // OCP: sem switch — só consulta o mapa
        Runnable loader = carregadores.get(tipo);
        if (loader != null) loader.run();
    }
    private void carregarAlunos() {
        modelo.setColumnIdentifiers(new String[]{"ID", "Nome", "Email", "CPF", "Matrícula", "Curso", "Período", "Apto", "Status"});
        List<Aluno> lista = alunoService.listar();
        for (Aluno a : lista) {
            modelo.addRow(new Object[]{
                    a.getId(), a.getNome(), a.getEmail(), a.getCpf(),
                    a.getMatricula(), a.getCurso(), a.getPeriodo(),
                    a.isApto() ? "Sim" : "Não", a.getStatus()
            });
        }
        setTitle("Relatório de Alunos - UniALFA");
    }
    private void carregarEmpresas() {
        modelo.setColumnIdentifiers(new String[]{"ID", "Nome", "CNPJ", "Email", "Telefone", "Área", "Status"});
        List<Empresa> lista = empresaService.listar();
        for (Empresa e : lista) {
            modelo.addRow(new Object[]{
                    e.getId(), e.getNome(), e.getCnpj(), e.getEmail(),
                    e.getTelefone(), e.getAreaAtuacao(), e.getStatus()
            });
        }
        setTitle("Relatório de Empresas - UniALFA");
    }
    private void carregarVagas() {
        modelo.setColumnIdentifiers(new String[]{"ID", "Empresa", "Título", "Área", "Requisitos", "Carga H.", "Modalidade", "Status"});
        List<Vaga> lista = vagaService.listar();
        for (Vaga v : lista) {
            modelo.addRow(new Object[]{
                    v.getId(), v.getEmpresaNome(), v.getTitulo(), v.getArea(),
                    v.getRequisitos(), v.getCargaHoraria(), v.getModalidade(), v.getStatus()
            });
        }
        setTitle("Relatório de Vagas - UniALFA");
    }
    private void carregarCandidaturas() {
        modelo.setColumnIdentifiers(new String[]{"ID", "Aluno", "Vaga", "Status", "Observação", "Data", "Atualizado"});
        List<Candidatura> lista = candidaturaService.listar();
        for (Candidatura c : lista) {
            modelo.addRow(new Object[]{
                    c.getId(), c.getAlunoNome(), c.getVagaTitulo(),
                    c.getStatus(), c.getObservacao(),
                    c.getDataCandidatura(), c.getUpdated()
            });
        }
        setTitle("Relatório de Candidaturas - UniALFA");
    }

    private void exportarTxt() {
        JFileChooser chooser = new JFileChooser();
        String nomeArquivo = "relatorio_" + tipoAtual + "_"
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
                + ".txt";
        chooser.setSelectedFile(new java.io.File(nomeArquivo));

        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        try (FileWriter fw = new FileWriter(chooser.getSelectedFile())) {
            String dataHora = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

            fw.write("====================================================\n");
            fw.write("  PORTAL DE ESTÁGIOS - UNIALFA\n");
            fw.write("  Relatório: " + tipoAtual.toUpperCase() + "\n");
            fw.write("  Gerado em: " + dataHora + "\n");
            fw.write("====================================================\n\n");

            StringBuilder cabecalho = new StringBuilder();
            for (int i = 0; i < modelo.getColumnCount(); i++) {
                cabecalho.append(String.format("%-20s", modelo.getColumnName(i)));
            }
            fw.write(cabecalho + "\n");
            fw.write("-".repeat(modelo.getColumnCount() * 20) + "\n");

            for (int i = 0; i < modelo.getRowCount(); i++) {
                StringBuilder linha = new StringBuilder();
                for (int j = 0; j < modelo.getColumnCount(); j++) {
                    Object val = modelo.getValueAt(i, j);
                    linha.append(String.format("%-20s", val != null ? val.toString() : ""));
                }
                fw.write(linha + "\n");
            }

            fw.write("\n====================================================\n");
            fw.write("  Total de registros: " + modelo.getRowCount() + "\n");
            fw.write("====================================================\n");

            JOptionPane.showMessageDialog(this,
                    "Relatório exportado com sucesso!\n" + chooser.getSelectedFile().getAbsolutePath(),
                    "Exportado", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            GerenciadorExcecoes.tratar(this, ex); // centralizado
        }
    }
}