package services;

import model.Empresa;
import java.util.List;

public interface IEmpresaService {

    List<Empresa> listar();
    List<Empresa> buscar(String termo);
    Empresa buscarPorId(int id);
    void cadastrar(Empresa empresa);
    void editar(Empresa empresa);
    void excluir(int id);
    void alternarStatus(Empresa empresa);
}