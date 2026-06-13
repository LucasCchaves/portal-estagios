<?php

class Candidatura{
    public int $id;
    public int $alunoId;
    public int $vagaId;
    public string $status;
    public string $observacao;
    public ?string $createdAt;
    public ?string $updatedAt;

    public function __construct(int $id, int $alunoId, int $vagaId, string $status, string $observacao, ?string $createdAt, ?string $updatedAt){
        $this->id = $id;
        $this->alunoId = $alunoId;
        $this->vagaId = $vagaId;
        $this->status = $status;
        $this->observacao = $observacao;
        $this->createdAt = $createdAt;
        $this->updatedAt = $updatedAt;
    }
};


?>