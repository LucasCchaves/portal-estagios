package services;

import model.Vaga;
import java.util.List;

public interface IVagaService {
    List<Vaga> listar();
    List<Vaga> buscar(String termo);
    Vaga buscarPorId(int id);
    void cadastrar(Vaga vaga);
    void editar(Vaga vaga);
    void excluir(int id);
    void alternarStatus(Vaga vaga);
}