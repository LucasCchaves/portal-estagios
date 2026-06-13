package services;

import dao.CandidaturaDAO;
import model.Candidatura;
import model.StatusCandidatura;
import java.sql.SQLException;
import java.util.List;

public class CandidaturaService implements ICandidaturaService {

    private final CandidaturaDAO dao;

    public CandidaturaService(CandidaturaDAO dao) {
        this.dao = dao;
    }
    @Override
    public List<Candidatura> listar() {
        try {
            return dao.listarTodos();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar candidaturas.", e);
        }
    }

    @Override
    public List<Candidatura> buscar(String termo) {
        if (termo == null || termo.isBlank()) return listar();
        try {
            return dao.buscarPorAlunoNome(termo.trim().toLowerCase());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar candidaturas.", e);
        }
    }
    @Override
    public Candidatura buscarPorId(int id) {
        try {
            return dao.buscarPorId(id)
                    .orElseThrow(() -> new IllegalArgumentException("Candidatura não encontrada: " + id));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar candidatura.", e);
        }
    }
    @Override
    public void cadastrar(Candidatura candidatura) {
        validar(candidatura);
        try {
            dao.inserir(candidatura);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar candidatura.", e);
        }
    }
    @Override
    public void editar(Candidatura candidatura) {
        validar(candidatura);
        try {
            dao.atualizar(candidatura);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao editar candidatura.", e);
        }
    }
    @Override
    public void excluir(int id) {
        try {
            dao.excluir(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir candidatura.", e);
        }
    }
    @Override
    public void alterarStatus(Candidatura candidatura, StatusCandidatura novoStatus) {
        candidatura.setStatus(novoStatus); // encapsulado no modelo — atualiza updated automaticamente
        editar(candidatura);
    }

    private void validar(Candidatura c) {
        if (c.getAlunoId() <= 0)
            throw new IllegalArgumentException("ID do aluno é obrigatório.");
        if (c.getVagaId() <= 0)
            throw new IllegalArgumentException("ID da vaga é obrigatório.");
        if (c.getStatus() == null)
            throw new IllegalArgumentException("Status é obrigatório.");
    }
}