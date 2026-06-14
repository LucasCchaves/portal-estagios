package services;

import dao.VagaDAO;
import model.Vaga;
import java.sql.SQLException;
import java.util.List;

public class VagaService implements IVagaService {

    private final VagaDAO dao;

    public VagaService(VagaDAO dao) {
        this.dao = dao;
    }

    @Override
    public List<Vaga> listar() {
        try {
            return dao.listarTodos();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar vagas.", e);
        }
    }

    @Override
    public List<Vaga> buscar(String termo) {
        if (termo == null || termo.isBlank()) return listar();
        try {
            return dao.buscarPorTitulo(termo.trim().toLowerCase());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar vagas.", e);
        }
    }

    @Override
    public Vaga buscarPorId(int id) {
        try {
            return dao.buscarPorId(id)
                    .orElseThrow(() -> new IllegalArgumentException("Vaga não encontrada: " + id));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar vaga.", e);
        }
    }

    @Override
    public void cadastrar(Vaga vaga) {
        validar(vaga);
        try {
            dao.inserir(vaga);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar vaga.", e);
        }
    }

    @Override
    public void editar(Vaga vaga) {
        validar(vaga);
        try {
            dao.atualizar(vaga);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao editar vaga.", e);
        }
    }

    @Override
    public void excluir(int id) {
        try {
            dao.excluir(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir vaga.", e);
        }
    }

    @Override
    public void alternarStatus(Vaga vaga) {
        vaga.alternarStatus();
        editar(vaga);
    }

    private void validar(Vaga v) {
        if (v.getTitulo() == null || v.getTitulo().isBlank())
            throw new IllegalArgumentException("Título é obrigatório.");
        if (v.getArea() == null || v.getArea().isBlank())
            throw new IllegalArgumentException("Área é obrigatória.");
        if (v.getEmpresaId() <= 0)
            throw new IllegalArgumentException("Empresa é obrigatória.");
    }
}