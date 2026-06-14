package model;
import errorHandler.GerenciadorExcecoes;
import java.time.LocalDate;
import java.time.LocalDateTime;
public class Candidatura {
    private int id;
    private int alunoId;
    private String alunoNome;
    private int vagaId;
    private String vagaTitulo;
    private String observacao;
    private StatusCandidatura status;
    private final LocalDate dataCandidatura;
    private LocalDateTime updated;
    public Candidatura() {
        this.dataCandidatura = LocalDate.now();
        this.status          = StatusCandidatura.ANALISANDO;
        this.updated         = LocalDateTime.now();
    }
    private Candidatura(int id, int alunoId, String alunoNome,
                        int vagaId, String vagaTitulo, String observacao,
                        StatusCandidatura status, LocalDate dataCandidatura,
                        LocalDateTime updated) {
        this.id              = id;
        this.alunoId         = alunoId;
        this.alunoNome       = alunoNome;
        this.vagaId          = vagaId;
        this.vagaTitulo      = vagaTitulo;
        this.observacao      = observacao;
        this.status          = status;
        this.dataCandidatura = dataCandidatura;
        this.updated         = updated;
    }
    public static Candidatura reconstituir(int id, int alunoId, String alunoNome,
                                           int vagaId, String vagaTitulo,
                                           String observacao, StatusCandidatura status,
                                           LocalDate dataCandidatura, LocalDateTime updated) {
        return new Candidatura(id, alunoId, alunoNome, vagaId, vagaTitulo,
                observacao, status, dataCandidatura, updated);
    }
    public boolean isAprovado() {
        return this.status == StatusCandidatura.APROVADO;
    }

    public int getId()                      { return id; }
    public int getAlunoId()                 { return alunoId; }
    public String getAlunoNome()            { return alunoNome; }
    public int getVagaId()                  { return vagaId; }
    public String getVagaTitulo()           { return vagaTitulo; }
    public String getObservacao()           { return observacao; }
    public StatusCandidatura getStatus()    { return status; }
    public LocalDate getDataCandidatura()   { return dataCandidatura; }
    public LocalDateTime getUpdated()       { return updated; }
    public void setId(int id) {
        this.id = id;
    }
    public void setAlunoId(int alunoId) {
        if (alunoId <= 0) throw new IllegalArgumentException("ID do aluno inválido.");
        this.alunoId = alunoId;
    }
    public void setVagaId(int vagaId) {
        try {
            if (vagaId <= 0) throw new IllegalArgumentException("ID da vaga inválido.");
            this.vagaId = vagaId;
        } catch (IllegalArgumentException ex) {
            GerenciadorExcecoes.tratar(null, ex);
            throw ex;
        }
    }
    public void setStatus(StatusCandidatura status) {
        try {
            if (status == null) throw new IllegalArgumentException("Status não pode ser nulo.");
            this.status  = status;
            this.updated = LocalDateTime.now();
        } catch (IllegalArgumentException ex) {
            GerenciadorExcecoes.tratar(null, ex);
            throw ex;
        }
    }
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
    public void setAlunoNome(String alunoNome)   { this.alunoNome  = alunoNome; }
    public void setVagaTitulo(String vagaTitulo) { this.vagaTitulo = vagaTitulo; }
    @Override
    public String toString() {
        return String.format(
                "Candidatura{id=%d, aluno='%s', vaga='%s', status=%s}",
                id, alunoNome, vagaTitulo, status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Candidatura)) return false;
        Candidatura outro = (Candidatura) o;
        return id != 0 && id == outro.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}