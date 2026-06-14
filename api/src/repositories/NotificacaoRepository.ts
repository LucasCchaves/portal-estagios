import type { Repository } from "typeorm"
import type { Notificacao } from "../models/Notificacao"

export class NotificacaoRepository {
  constructor(private readonly repo: Repository<Notificacao>) {}

  async listarPorAluno(alunoId: number): Promise<Notificacao[]> {
    return this.repo.find({
      where: { aluno: { id: alunoId } },
      relations: { candidatura: true },
      order: { created_at: "DESC" }
    })
  }

  async buscarPorId(id: number): Promise<Notificacao | undefined> {
    const row = await this.repo.findOne({ where: { id } })
    return row ?? undefined
  }

  async criar(dados: Partial<Notificacao>): Promise<Notificacao> {
    const ent = this.repo.create(dados)
    return this.repo.save(ent)
  }

  async salvar(entidade: Notificacao): Promise<Notificacao> {
    return this.repo.save(entidade)
  }
}