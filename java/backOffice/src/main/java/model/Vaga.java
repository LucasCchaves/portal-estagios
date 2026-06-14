package model;

import utils.Validador;
import java.time.LocalDateTime;

public class Vaga {

    private int id;
    private int empresaId;
    private String empresaNome;
    private String titulo;
    private String descricao;
    private String area;
    private String requisitos;
    private float cargaHoraria;
    private String modalidade;
    private StatusVaga status;
    private final LocalDateTime createdAt;

    public Vaga() {
        this.createdAt = LocalDateTime.now();
        this.status    = StatusVaga.ABERTA;
    }

    private Vaga(int id, int empresaId, String empresaNome, String titulo,
                 String descricao, String area, String requisitos,
                 float cargaHoraria, String modalidade,
                 StatusVaga status, LocalDateTime createdAt) {
        this.id           = id;
        this.empresaId    = empresaId;
        this.empresaNome  = empresaNome;
        this.titulo       = titulo;
        this.descricao    = descricao;
        this.area         = area;
        this.requisitos   = requisitos;
        this.cargaHoraria = cargaHoraria;
        this.modalidade   = modalidade;
        this.status       = status;
        this.createdAt    = createdAt;
    }

    public static Vaga reconstituir(int id, int empresaId, String empresaNome,
                                    String titulo, String descricao, String area,
                                    String requisitos, float cargaHoraria,
                                    String modalidade, StatusVaga status,
                                    LocalDateTime createdAt) {
        return new Vaga(id, empresaId, empresaNome, titulo, descricao, area,
                requisitos, cargaHoraria, modalidade, status, createdAt);
    }

    public void alternarStatus() {
        this.status = isAberta() ? StatusVaga.FECHADA : StatusVaga.ABERTA;
    }

    public boolean isAberta() {
        return this.status == StatusVaga.ABERTA;
    }

    public int getId()              { return id; }
    public int getEmpresaId()       { return empresaId; }
    public String getEmpresaNome()  { return empresaNome; }
    public String getTitulo()       { return titulo; }
    public String getDescricao()    { return descricao; }
    public String getArea()         { return area; }
    public String getRequisitos()   { return requisitos; }
    public float getCargaHoraria()  { return cargaHoraria; }
    public String getModalidade()   { return modalidade; }
    public StatusVaga getStatus()   { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(int id)                   { this.id = id; }
    public void setEmpresaId(int empresaId)     { this.empresaId = empresaId; }
    public void setEmpresaNome(String nome)     { this.empresaNome = nome; }

    public void setTitulo(String titulo) {
        Validador.exigirNaoVazio(titulo, "Título");
        this.titulo = titulo.trim();
    }

    public void setDescricao(String descricao)  { this.descricao = descricao; }
    public void setArea(String area) {
        Validador.exigirNaoVazio(area, "Área");
        this.area = area.trim();
    }

    public void setRequisitos(String requisitos) { this.requisitos = requisitos; }
    public void setCargaHoraria(float cargaHoraria) { this.cargaHoraria = cargaHoraria; }
    public void setModalidade(String modalidade) { this.modalidade = modalidade; }

    public void setStatus(StatusVaga status) {
        if (status == null) throw new IllegalArgumentException("Status não pode ser nulo.");
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("Vaga{id=%d, titulo='%s', status=%s}", id, titulo, status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vaga)) return false;
        Vaga outra = (Vaga) o;
        return id == outra.id;
    }

    @Override
    public int hashCode() { return id; }
}