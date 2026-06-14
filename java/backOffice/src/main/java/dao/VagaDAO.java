package dao;

import model.StatusVaga;
import model.Vaga;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VagaDAO implements IGenericDAO<Vaga> {

    private final Connection connection;

    public VagaDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Vaga> listarTodos() throws SQLException {
        List<Vaga> lista = new ArrayList<>();
        String sql = """
                SELECT v.*, e.nome AS empresa_nome
                FROM vaga v
                JOIN empresa e ON v.empresa_id = e.id
                ORDER BY v.titulo
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Vaga> buscarPorTitulo(String termo) throws SQLException {
        List<Vaga> lista = new ArrayList<>();
        String sql = """
                SELECT v.*, e.nome AS empresa_nome
                FROM vaga v
                JOIN empresa e ON v.empresa_id = e.id
                WHERE LOWER(v.titulo) LIKE ?
                ORDER BY v.titulo
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + termo + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    @Override
    public Optional<Vaga> buscarPorId(int id) throws SQLException {
        String sql = """
                SELECT v.*, e.nome AS empresa_nome
                FROM vaga v
                JOIN empresa e ON v.empresa_id = e.id
                WHERE v.id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public void inserir(Vaga vaga) throws SQLException {
        String sql = """
                INSERT INTO vaga (empresa_id, titulo, descricao, area, requisitos, carga_horaria, modalidade, status, created_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preencherStatement(ps, vaga);
            ps.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) vaga.setId(keys.getInt(1));
            }
        }
    }

    @Override
    public void atualizar(Vaga vaga) throws SQLException {
        String sql = """
                UPDATE vaga
                SET empresa_id = ?, titulo = ?, descricao = ?, area = ?, requisitos = ?,
                    carga_horaria = ?, modalidade = ?, status = ?
                WHERE id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            preencherStatement(ps, vaga);
            ps.setInt(9, vaga.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM vaga WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // — helpers —

    private void preencherStatement(PreparedStatement ps, Vaga v) throws SQLException {
        ps.setInt   (1, v.getEmpresaId());
        ps.setString(2, v.getTitulo());
        ps.setString(3, v.getDescricao());
        ps.setString(4, v.getArea());
        ps.setString(5, v.getRequisitos());
        ps.setFloat (6, v.getCargaHoraria());
        ps.setString(7, v.getModalidade());
        ps.setString(8, v.getStatus().name());
    }

    private Vaga mapear(ResultSet rs) throws SQLException {
        return Vaga.reconstituir(
                rs.getInt("id"),
                rs.getInt("empresa_id"),
                rs.getString("empresa_nome"),
                rs.getString("titulo"),
                rs.getString("descricao"),
                rs.getString("area"),
                rs.getString("requisitos"),
                rs.getFloat("carga_horaria"),
                rs.getString("modalidade"),
                StatusVaga.valueOf(rs.getString("status")),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}