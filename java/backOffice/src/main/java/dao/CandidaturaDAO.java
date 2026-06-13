package dao;

import model.Candidatura;
import model.StatusCandidatura;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CandidaturaDAO implements IGenericDAO<Candidatura> {

    private final Connection connection;

    public CandidaturaDAO(Connection connection) {
        this.connection = connection;
    }
    public List<Candidatura> listarTodos() throws SQLException {
        String sql = """
                SELECT c.id, c.aluno_id, a.nome AS aluno_nome,
                       c.vaga_id, v.titulo AS vaga_titulo,
                       c.observacao, c.status,
                       c.data_candidatura, c.updated_at
                FROM candidatura c
                JOIN aluno a ON a.id = c.aluno_id
                JOIN vaga  v ON v.id = c.vaga_id
                ORDER BY c.id
                """;

        List<Candidatura> lista = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Candidatura> buscarPorAlunoNome(String termo) throws SQLException {
        String sql = """
                SELECT c.id, c.aluno_id, a.nome AS aluno_nome,
                       c.vaga_id, v.titulo AS vaga_titulo,
                       c.observacao, c.status,
                       c.data_candidatura, c.updated_at
                FROM candidatura c
                JOIN aluno a ON a.id = c.aluno_id
                JOIN vaga  v ON v.id = c.vaga_id
                WHERE LOWER(a.nome) LIKE ?
                ORDER BY c.id
                """;

        List<Candidatura> lista = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + termo + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    @Override
    public Optional<Candidatura> buscarPorId(int id) throws SQLException {
        String sql = """
                SELECT c.id, c.aluno_id, a.nome AS aluno_nome,
                       c.vaga_id, v.titulo AS vaga_titulo,
                       c.observacao, c.status,
                       c.data_candidatura, c.updated_at
                FROM candidatura c
                JOIN aluno a ON a.id = c.aluno_id
                JOIN vaga  v ON v.id = c.vaga_id
                WHERE c.id = ?
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
    public void inserir(Candidatura c) throws SQLException {
        String sql = """
                INSERT INTO candidatura (aluno_id, vaga_id, status, observacao, data_candidatura, updated_at)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, c.getAlunoId());
            ps.setInt(2, c.getVagaId());
            ps.setString(3, c.getStatus().name());
            ps.setString(4, c.getObservacao());
            ps.setDate(5, Date.valueOf(c.getDataCandidatura()));
            ps.setTimestamp(6, Timestamp.valueOf(c.getUpdated()));
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) c.setId(keys.getInt(1));
            }
        }
    }

    @Override
    public void atualizar(Candidatura c) throws SQLException {
        String sql = """
                UPDATE candidatura
                SET aluno_id = ?, vaga_id = ?, status = ?, observacao = ?, updated_at = ?
                WHERE id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, c.getAlunoId());
            ps.setInt(2, c.getVagaId());
            ps.setString(3, c.getStatus().name());
            ps.setString(4, c.getObservacao());
            ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(6, c.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM candidatura WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Candidatura mapear(ResultSet rs) throws SQLException {
        return Candidatura.reconstituir(
                rs.getInt("id"),
                rs.getInt("aluno_id"),
                rs.getString("aluno_nome"),
                rs.getInt("vaga_id"),
                rs.getString("vaga_titulo"),
                rs.getString("observacao"),
                StatusCandidatura.valueOf(rs.getString("status")),
                rs.getDate("data_candidatura").toLocalDate(),
                rs.getTimestamp("updated_at").toLocalDateTime()
        );
    }
}