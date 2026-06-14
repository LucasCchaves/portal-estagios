package dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IGenericDAO<T> {
    List<T> listarTodos() throws SQLException;
    Optional<T> buscarPorId(int id) throws SQLException;
    void inserir(T entidade) throws SQLException;
    void atualizar(T entidade) throws SQLException;
    void excluir(int id) throws SQLException;
}