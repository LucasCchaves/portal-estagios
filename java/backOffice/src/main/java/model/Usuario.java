package model;

import java.time.LocalDateTime;

public class Usuario {

    private int id;
    private String nome;
    private String email;
    private String senha;
    private String perfil;
    private String status;
    private LocalDateTime createdAt;

    public Usuario() {
        this.createdAt = LocalDateTime.now();
        this.perfil    = "operador";
        this.status    = "ativo";
    }

    public static Usuario reconstituir(int id, String nome, String email, String senha,
                                       String perfil, String status, LocalDateTime createdAt) {
        Usuario u   = new Usuario();
        u.id        = id;
        u.nome      = nome;
        u.email     = email;
        u.senha     = senha;
        u.perfil    = perfil;
        u.status    = status;
        u.createdAt = createdAt;
        return u;
    }

    public int getId()                  { return id; }
    public String getNome()             { return nome; }
    public String getEmail()            { return email; }
    public String getSenha()            { return senha; }
    public String getPerfil()           { return perfil; }
    public String getStatus()           { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(int id)           { this.id     = id; }
    public void setNome(String nome)    { this.nome   = nome; }
    public void setEmail(String email)  { this.email  = email; }
    public void setSenha(String senha)  { this.senha  = senha; }
    public void setPerfil(String perfil){ this.perfil = perfil; }
    public void setStatus(String status){ this.status = status; }

    @Override
    public String toString() {
        return String.format(
                "Usuario{id=%d, nome='%s', email='%s', perfil='%s', status='%s'}",
                id, nome, email, perfil, status);
    }
}