package services;

import model.Candidatura;
import model.StatusCandidatura;

import java.util.List;

public interface ICandidaturaService {
    List<Candidatura> listar();
    List<Candidatura> buscar(String termo);
    Candidatura buscarPorId(int id);
    void cadastrar(Candidatura candidatura);
    void editar(Candidatura candidatura);
    void excluir(int id);
    void alterarStatus(Candidatura candidatura, StatusCandidatura novoStatus);
}