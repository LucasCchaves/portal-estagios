<?php

    require_once '../shared/ApiClient.php';
    require_once '../shared/Vaga.php';
    require_once '../shared/Candidatura.php';

class EmpresaService{

    private ApiClient $api;

    public function __construct(ApiClient $api){
        $this->api = $api;
    }

    public function listarVagas(int $empresaId): array{
        return $this->api->get('/api/vagas?empresaId=' . $empresaId);
    }
    
    public function buscarVaga(int $vagaId): array{
        return $this->api->get('/api/vagas/' . $vagaId);
    }

    public function criarVaga(array $dados): array{
        return $this->api->post('/api/vagas', $dados);
    }
    
    public function editarVaga(int $vagaId, array $dados): array{
        return $this->api->patch('/api/vagas/'. $vagaId, $dados);
    }

    public function deletarVaga(int $vagaId): bool{
        return $this->api->delete('/api/vagas/' . $vagaId);
    }

    public function listarCandidatos(int $vagaId): array{
        return $this->api->get('/api/candidaturas?vagaId=' . $vagaId);
    }

    public function atualizarStatusCandidatura(int $id, string $status): array {
        $dados = ['status' => $status];
        return $this->api->put('/api/candidaturas/' . $id . '/status', $dados);
    }

    public function login(string $email, string $senha): ?array {
    $resposta = $this->api->get('/api/empresas');
    
    foreach ($resposta['empresas'] as $empresa) {
        if ($empresa['email'] === $email && $empresa['senha'] === $senha) {
            return $empresa;
        }
    }
    
    return null;
}
    
}



