package gui;

import model.Aluno;
import model.Candidatura;
import model.StatusCandidatura;
import model.Vaga;
import services.IAlunoService;
import services.ICandidaturaService;
import services.IVagaService;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TelaCandidaturas extends TelaBase {
    private final ICandidaturaService service;
    private final IAlunoService       alunoService;
    private final IVagaService        vagaService;
    private List<Candidatura> candidaturas;

    public TelaCandidaturas(ICandidaturaService service, IAlunoService alunoService, IVagaService vagaService) {
        super("Gestão de Candidaturas - UniALFA", 900, 500);
        this.service      = service;
        this.alunoService = alunoService;
        this.vagaService  = vagaService;
        carregarDados();
    }

    @Override
    protected String labelBusca() { return "Buscar por aluno:"; }

    @Override
    protected String[] colunas() {
        return new String[]{"ID", "Aluno", "Vaga", "Status", "Observação", "Data", "Atualizado"};
    }

    @Override
    protected void configurarColunas() {
        tabela.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(80);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(90);
        tabela.getColumnModel().getColumn(6).setPreferredWidth(90);
    }

    @Override
    protected void carregarDados() {
        candidaturas = service.listar();
        preencherTabela(candidaturas);
    }

    @Override
    protected void buscar(String termo) {
        if (termo.isBlank()) { carregarDados(); return; }
        List<Candidatura> filtradas = candidaturas.stream()
                .filter(c -> c.getAlunoNome().toLowerCase().contains(termo.toLowerCase()))
                .toList();
        preencherTabela(filtradas);
    }

    @Override
    protected JPanel montarRodape() {
        JButton btnNova    = new JButton("Nova Candidatura");
        JButton btnEditar  = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnStatus  = new JButton("Alterar Status");

        btnNova   .addActionListener(e -> abrirFormulario(null));
        btnEditar .addActionListener(e -> { Candidatura s = getCandidaturaSelecionada(); if (s != null) abrirFormulario(s); });
        btnExcluir.addActionListener(e -> excluir());
        btnStatus .addActionListener(e -> alterarStatus());

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        rodape.add(btnNova);
        rodape.add(btnEditar);
        rodape.add(btnExcluir);
        rodape.add(btnStatus);
        return rodape;
    }

    private void preencherTabela(List<Candidatura> lista) {
        modelo.setRowCount(0);
        for (Candidatura c : lista) {
            modelo.addRow(new Object[]{
                    c.getId(), c.getAlunoNome(), c.getVagaTitulo(),
                    c.getStatus(), c.getObservacao(),
                    c.getDataCandidatura(), c.getUpdated()
            });
        }
    }

    private Candidatura getCandidaturaSelecionada() {
        int id = getIdSelecionado("uma candidatura");
        if (id == -1) return null;
        return candidaturas.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }

    private void abrirFormulario(Candidatura candidatura) {
        boolean editando = candidatura != null;

        // Combobox de alunos
        List<Aluno> listaAlunos = alunoService.listar();
        JComboBox<Aluno> cmbAluno = new JComboBox<>(listaAlunos.toArray(new Aluno[0]));
        cmbAluno.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Aluno) setText(((Aluno) value).getNome());
                return this;
            }
        });
        if (editando) {
            listaAlunos.stream()
                    .filter(a -> a.getId() == candidatura.getAlunoId())
                    .findFirst()
                    .ifPresent(cmbAluno::setSelectedItem);
        }

        // Combobox de vagas
        List<Vaga> listaVagas = vagaService.listar();
        JComboBox<Vaga> cmbVaga = new JComboBox<>(listaVagas.toArray(new Vaga[0]));
        cmbVaga.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Vaga) setText(((Vaga) value).getTitulo());
                return this;
            }
        });
        if (editando) {
            listaVagas.stream()
                    .filter(v -> v.getId() == candidatura.getVagaId())
                    .findFirst()
                    .ifPresent(cmbVaga::setSelectedItem);
        }

        JTextField txtObs = new JTextField(editando ? candidatura.getObservacao() : "", 20);
        JComboBox<StatusCandidatura> cmbStatus = new JComboBox<>(StatusCandidatura.values());
        if (editando) cmbStatus.setSelectedItem(candidatura.getStatus());

        JPanel form = new JPanel(new GridBagLayout());
        painelAdd(form, new JLabel("Aluno *:"),      0, 0); painelAdd(form, cmbAluno,  1, 0);
        painelAdd(form, new JLabel("Vaga *:"),       0, 1); painelAdd(form, cmbVaga,   1, 1);
        painelAdd(form, new JLabel("Status:"),       0, 2); painelAdd(form, cmbStatus, 1, 2);
        painelAdd(form, new JLabel("Observação:"),   0, 3); painelAdd(form, txtObs,    1, 3);

        int res = JOptionPane.showConfirmDialog(this, form,
                editando ? "Editar Candidatura" : "Nova Candidatura",
                JOptionPane.OK_CANCEL_OPTION);

        if (res != JOptionPane.OK_OPTION) return;

        try {
            Candidatura c = editando ? candidatura : new Candidatura();
            Aluno alunoSelecionado = (Aluno) cmbAluno.getSelectedItem();
            Vaga  vagaSelecionada  = (Vaga)  cmbVaga.getSelectedItem();

            c.setAlunoId(alunoSelecionado.getId());
            c.setAlunoNome(alunoSelecionado.getNome());
            c.setVagaId(vagaSelecionada.getId());
            c.setVagaTitulo(vagaSelecionada.getTitulo());
            c.setObservacao(txtObs.getText().trim());
            c.setStatus((StatusCandidatura) cmbStatus.getSelectedItem());

            if (editando) service.editar(c);
            else          service.cadastrar(c);

            carregarDados();
            JOptionPane.showMessageDialog(this, editando ? "Candidatura atualizada!" : "Candidatura cadastrada!");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Dados inválidos", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void excluir() {
        Candidatura c = getCandidaturaSelecionada();
        if (c == null) return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "Excluir a candidatura de \"" + c.getAlunoNome() + "\"?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            service.excluir(c.getId());
            carregarDados();
        }
    }

    private void alterarStatus() {
        Candidatura c = getCandidaturaSelecionada();
        if (c == null) return;

        StatusCandidatura novoStatus = (StatusCandidatura) JOptionPane.showInputDialog(
                this, "Selecione o novo status:", "Alterar Status",
                JOptionPane.PLAIN_MESSAGE, null,
                StatusCandidatura.values(), c.getStatus());

        if (novoStatus == null) return;

        service.alterarStatus(c, novoStatus);
        carregarDados();
        JOptionPane.showMessageDialog(this, "Status atualizado para: " + novoStatus);
    }
}