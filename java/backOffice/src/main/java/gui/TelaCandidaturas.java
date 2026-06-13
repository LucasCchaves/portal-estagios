package gui;

import model.Candidatura;
import model.StatusCandidatura;
import services.ICandidaturaService;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TelaCandidaturas extends TelaBase {
    private final ICandidaturaService service;
    private List<Candidatura> candidaturas;
    public TelaCandidaturas(ICandidaturaService service) {
        super("Gestão de Candidaturas - UniALFA", 900, 500);
        this.service = service;
    }

    @Override
    protected String labelBusca() {
        return "Buscar por aluno:";
    }
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
        if (termo.isBlank()) {
            carregarDados();
            return;
        }
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
        btnStatus .addActionListener(e -> alterarStatus()); // delega ao service

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
                    c.getId(),
                    c.getAlunoNome(),
                    c.getVagaTitulo(),
                    c.getStatus(),
                    c.getObservacao(),
                    c.getDataCandidatura(),
                    c.getUpdated()
            });
        }
    }
    private Candidatura getCandidaturaSelecionada() {
        int id = getIdSelecionado("uma candidatura");
        if (id == -1) return null;
        return candidaturas.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }
    private void abrirFormulario(Candidatura candidatura) {
        boolean editando = candidatura != null;

        JTextField txtAlunoId = new JTextField(editando ? String.valueOf(candidatura.getAlunoId()) : "", 20);
        JTextField txtVagaId  = new JTextField(editando ? String.valueOf(candidatura.getVagaId())  : "", 20);
        JTextField txtObs     = new JTextField(editando ? candidatura.getObservacao()              : "", 20);

        JComboBox<StatusCandidatura> cmbStatus = new JComboBox<>(StatusCandidatura.values());
        if (editando) cmbStatus.setSelectedItem(candidatura.getStatus());

        JPanel form = new JPanel(new GridLayout(4, 2, 6, 6));
        form.add(new JLabel("ID do Aluno *:")); form.add(txtAlunoId);
        form.add(new JLabel("ID da Vaga *:"));  form.add(txtVagaId);
        form.add(new JLabel("Status:"));        form.add(cmbStatus);
        form.add(new JLabel("Observação:"));    form.add(txtObs);

        int res = JOptionPane.showConfirmDialog(
                this, form,
                editando ? "Editar Candidatura" : "Nova Candidatura",
                JOptionPane.OK_CANCEL_OPTION);

        if (res != JOptionPane.OK_OPTION) return;

        try {
            Candidatura c = editando ? candidatura : new Candidatura();
            c.setAlunoId(Integer.parseInt(txtAlunoId.getText().trim()));
            c.setVagaId(Integer.parseInt(txtVagaId.getText().trim()));
            c.setObservacao(txtObs.getText().trim());
            // status via enum — type-safe
            c.setStatus((StatusCandidatura) cmbStatus.getSelectedItem());

            if (editando) service.editar(c);
            else          service.cadastrar(c);

            carregarDados();
            JOptionPane.showMessageDialog(this, editando ? "Candidatura atualizada!" : "Candidatura cadastrada!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID do aluno e da vaga devem ser números.",
                    "Dados inválidos", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Dados inválidos", JOptionPane.WARNING_MESSAGE);
        }
    }
    private void excluir() {
        Candidatura c = getCandidaturaSelecionada();
        if (c == null) return;

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Excluir a candidatura de \"" + c.getAlunoNome() + "\"?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            service.excluir(c.getId());
            carregarDados();
        }
    }
    private void alterarStatus() {
        Candidatura c = getCandidaturaSelecionada();
        if (c == null) return;
        StatusCandidatura novoStatus = (StatusCandidatura) JOptionPane.showInputDialog(
                this,
                "Selecione o novo status:",
                "Alterar Status",
                JOptionPane.PLAIN_MESSAGE,
                null,
                StatusCandidatura.values(),
                c.getStatus());

        if (novoStatus == null) return;

        service.alterarStatus(c, novoStatus);
        carregarDados();
        JOptionPane.showMessageDialog(this, "Status atualizado para: " + novoStatus);
    }
}