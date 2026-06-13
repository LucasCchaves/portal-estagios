package services;

import dao.UsuarioDAO;
import model.Usuario;

import java.sql.SQLException;
import java.util.List;

public class UsuarioService {

    private final UsuarioDAO dao;

    public UsuarioService(UsuarioDAO dao) {
        this.dao = dao;
    }

    public List<Usuario> listar() {
        try {
            return dao.listarTodos();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários.", e);
        }
    }

    public List<Usuario> buscar(String termo) {
        if (termo == null || termo.isBlank()) return listar();
        try {
            return dao.buscarPorNome(termo.trim().toLowerCase());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuários.", e);
        }
    }

    public Usuario buscarPorId(int id) {
        try {
            return dao.buscarPorId(id)
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + id));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário.", e);
        }
    }

    public void cadastrar(Usuario usuario) {
        validar(usuario);
        try {
            dao.inserir(usuario);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar usuário.", e);
        }
    }

    public void editar(Usuario usuario) {
        validar(usuario);
        try {
            dao.atualizar(usuario);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao editar usuário.", e);
        }
    }

    public void excluir(int id) {
        try {
            dao.excluir(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir usuário.", e);
        }
    }
    public Usuario autenticar(String email, String senha) {
        try {
            Usuario u = dao.buscarPorEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Email ou senha incorretos."));

            if (!u.getSenha().equals(senha))
                throw new IllegalArgumentException("Email ou senha incorretos.");

            if (!u.getStatus().equals("ativo"))
                throw new IllegalArgumentException("Usuário inativo. Contate o administrador.");

            return u;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao autenticar.", e);
        }
    }

    private void validar(Usuario u) {
        if (u.getNome() == null || u.getNome().isBlank())
            throw new IllegalArgumentException("Nome é obrigatório.");
        if (u.getEmail() == null || u.getEmail().isBlank())
            throw new IllegalArgumentException("Email é obrigatório.");
        if (u.getSenha() == null || u.getSenha().isBlank())
            throw new IllegalArgumentException("Senha é obrigatória.");
    }
}