package services;
import model.Aluno;
import java.io.IOException;
import java.util.List;
public interface IAlunoService {
    List<Aluno> listar();
    List<Aluno> buscar(String termo);
    Aluno buscarPorId(int id);
    void cadastrar(Aluno aluno);
    void editar(Aluno aluno);
    void excluir(int id);
    void bloquear(Aluno aluno);
    void ativar(Aluno aluno);
    void alternarAptidao(Aluno aluno);
    int importarDeTxt(String caminho) throws IOException;
}
