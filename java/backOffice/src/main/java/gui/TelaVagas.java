package gui;

import model.Vaga;
import services.IVagaService;
import errorHandler.GerenciadorExcecoes;

import java.awt.*;
import java.util.List;
import javax.swing.*;

public class TelaVagas extends TelaBase implements PainelDefault {

    private final IVagaService service;
    private List<Vaga> vagas;

    public TelaVagas(IVagaService service) {
        super("Gestão de Vagas - UniALFA", 950, 500);
        this.service = service;
    }

    @Override
    protected String labelBusca() {
        return "Buscar por título:";
    }

    @Override
    protected String[] colunas() {
        return new String[]{"ID", "Empresa", "Título", "Área", "Modalidade", "Carga H.", "Status"};
    }

    @Override
    protected void configurarColunas() {
        tabela.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(180);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(80);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(70);
        tabela.getColumnModel().getColumn(6).setPreferredWidth(80);
    }

    @Override
    protected void carregarDados() {
        vagas = service.listar();
        preencherTabela(vagas);
    }

    @Override
    protected void buscar(String termo) {
        if (termo.isBlank()) { carregarDados(); return; }
        List<Vaga> filtradas = vagas.stream()
                .filter(v -> v.getTitulo().toLowerCase().contains(termo.toLowerCase()))
                .toList();
        preencherTabela(filtradas);
    }

    @Override
    protected JPanel montarRodape() {
        JButton btnNova    = new JButton("Nova Vaga");
        JButton btnEditar  = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnStatus  = new JButton("Alternar Status");

        btnNova   .addActionListener(e -> abrirFormulario(null));
        btnEditar .addActionListener(e -> { Vaga s = getVaga(); if (s != null) abrirFormulario(s); });
        btnExcluir.addActionListener(e -> excluir());
        btnStatus .addActionListener(e -> alternarStatus());

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        rodape.add(btnNova);
        rodape.add(btnEditar);
        rodape.add(btnExcluir);
        rodape.add(btnStatus);
        return rodape;
    }

    private void preencherTabela(List<Vaga> lista) {
        modelo.setRowCount(0);
        for (Vaga v : lista) {
            modelo.addRow(new Object[]{
                    v.getId(),
                    v.getEmpresaNome(),
                    v.getTitulo(),
                    v.getArea(),
                    v.getModalidade(),
                    v.getCargaHoraria(),
                    v.isAberta() ? "✔ Aberta" : "✘ Fechada"
            });
        }
    }

    private Vaga getVaga() {
        int id = getIdSelecionado("uma vaga");
        if (id == -1) return null;
        return vagas.stream().filter(v -> v.getId() == id).findFirst().orElse(null);
    }

    private void abrirFormulario(Vaga vaga) {
        boolean editando = vaga != null;

        JTextField txtEmpresaId  = new JTextField(editando ? String.valueOf(vaga.getEmpresaId())    : "", 20);
        JTextField txtTitulo     = new JTextField(editando ? vaga.getTitulo()                       : "", 20);
        JTextField txtArea       = new JTextField(editando ? vaga.getArea()                         : "", 20);
        JTextField txtRequisitos = new JTextField(editando ? vaga.getRequisitos()                   : "", 20);
        JTextField txtCargaH     = new JTextField(editando ? String.valueOf(vaga.getCargaHoraria()) : "", 20);
        JTextArea  txtDescricao  = new JTextArea (editando ? vaga.getDescricao()                    : "",  3, 20);
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);

        String[] modalidades = {"Presencial", "Remoto", "Híbrido"};
        JComboBox<String> cmbModalidade = new JComboBox<>(modalidades);
        if (editando) cmbModalidade.setSelectedItem(vaga.getModalidade());

        JPanel form = new JPanel(new GridBagLayout());
        painelAdd(form, new JLabel("ID da Empresa *:"), 0, 0); painelAdd(form, txtEmpresaId,              1, 0);
        painelAdd(form, new JLabel("Título *:"),        0, 1); painelAdd(form, txtTitulo,                 1, 1);
        painelAdd(form, new JLabel("Área *:"),          0, 2); painelAdd(form, txtArea,                   1, 2);
        painelAdd(form, new JLabel("Requisitos:"),      0, 3); painelAdd(form, txtRequisitos,             1, 3);
        painelAdd(form, new JLabel("Carga Horária:"),   0, 4); painelAdd(form, txtCargaH,                 1, 4);
        painelAdd(form, new JLabel("Modalidade:"),      0, 5); painelAdd(form, cmbModalidade,             1, 5);
        painelAdd(form, new JLabel("Descrição:"),       0, 6); painelAdd(form, new JScrollPane(txtDescricao), 1, 6);

        int res = JOptionPane.showConfirmDialog(this, form,
                editando ? "Editar Vaga" : "Nova Vaga",
                JOptionPane.OK_CANCEL_OPTION);

        if (res != JOptionPane.OK_OPTION) return;

        try {
            Vaga v = editando ? vaga : new Vaga();
            v.setEmpresaId(Integer.parseInt(txtEmpresaId.getText().trim()));
            v.setTitulo(txtTitulo.getText().trim());
            v.setArea(txtArea.getText().trim());
            v.setRequisitos(txtRequisitos.getText().trim());
            v.setCargaHoraria(Float.parseFloat(txtCargaH.getText().trim()));
            v.setModalidade((String) cmbModalidade.getSelectedItem());
            v.setDescricao(txtDescricao.getText().trim());

            if (editando) service.editar(v);
            else          service.cadastrar(v);

            carregarDados();
            JOptionPane.showMessageDialog(this, editando ? "Vaga atualizada!" : "Vaga cadastrada!");
        } catch (Exception ex) {
            GerenciadorExcecoes.tratar(this, ex);
        }
    }

    private void excluir() {
        Vaga v = getVaga();
        if (v == null) return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "Excluir a vaga \"" + v.getTitulo() + "\"?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            service.excluir(v.getId());
            carregarDados();
        }
    }

    private void alternarStatus() {
        Vaga v = getVaga();
        if (v == null) return;

        service.alternarStatus(v);
        carregarDados();
        JOptionPane.showMessageDialog(this,
                "Vaga marcada como " + (v.isAberta() ? "ABERTA" : "FECHADA") + ".");
    }
}