import type { CandidaturaRepository } from "../repositories/CandidaturaRepository"
import type { AlunoRepository } from "../repositories/AlunoRepository"
import type { VagaRepository } from "../repositories/VagaRepository"
import type { NotificacaoRepository } from "../repositories/NotificacaoRepository"
import type { Candidatura } from "../models/Candidatura"
import AppError from "../utils/AppError"

export class CandidaturaService {
  constructor(
    private readonly repository: CandidaturaRepository,
    private readonly alunoRepository: AlunoRepository,
    private readonly vagaRepository: VagaRepository,
    private readonly notificacaoRepository: NotificacaoRepository,
  ) {}

  async listar(vagaId?: number): Promise<Candidatura[]> {
    return this.repository.listarTodos(vagaId)
  }

  async buscarPorId(id: number): Promise<Candidatura> {
    const candidatura = await this.repository.buscarPorId(id)
    if (!candidatura) throw new AppError("Candidatura não encontrada", 404)
    return candidatura
  }

  async criar(dados: { aluno_id: number, vaga_id: number, observacao?: string }): Promise<Candidatura> {
    const aluno = await this.alunoRepository.buscarPorId(dados.aluno_id)
    if (!aluno) throw new AppError("Aluno não encontrado", 404)

    const vaga = await this.vagaRepository.buscarPorId(dados.vaga_id)
    if (!vaga) throw new AppError("Vaga não encontrada", 404)

    return this.repository.criar({
      aluno,
      vaga,
      observacao: dados.observacao,
      data_candidatura: new Date(),
    })
  }

  async atualizarStatus(id: number, status: "pendente" | "aprovada" | "rejeitada"): Promise<Candidatura> {
    const candidatura = await this.repository.buscarPorIdComAluno(id)
    if (!candidatura) throw new AppError("Candidatura não encontrada", 404)

    candidatura.status = status
    const result = await this.repository.salvar(candidatura)

    await this.notificacaoRepository.criar({
      mensagem: `Sua candidatura foi ${status}.`,
      aluno: candidatura.aluno,
      candidatura: result,
    })

    return result
  }

  async remover(id: number): Promise<void> {
    const ok = await this.repository.remover(id)
    if (!ok) throw new AppError("Candidatura não encontrada", 404)
  }
}