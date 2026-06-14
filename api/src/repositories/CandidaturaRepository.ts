import type { Repository } from "typeorm"
import type { Candidatura } from "../models/Candidatura"

export class CandidaturaRepository {
  constructor(private readonly repo: Repository<Candidatura>) {}

  async listarTodos(vagaId?: number): Promise<Candidatura[]> {
  return this.repo.find({
    where: vagaId ? { vaga: { id: vagaId } } : {},
    relations: { aluno: true, vaga: true },
    select: {
      id: true,
      status: true,
      observacao: true,
      data_candidatura: true,
      updated_at: true,
      aluno: {
        id: true,
        nome: true,
        email: true,
        cpf: true,
        matricula: true,
        curso: true,
        periodo: true,
        apto: true,
        status: true,
        created_at: true,
      },
      vaga: {
        id: true,
        titulo: true,
        descricao: true,
        area: true,
        requisitos: true,
        carga_horaria: true,
        modalidade: true,
        status: true,
        created_at: true,
      }
    },
    order: { id: "ASC" }
  })
}

async buscarPorId(id: number): Promise<Candidatura | undefined> {
  const row = await this.repo.findOne({
    where: { id },
    relations: { aluno: true, vaga: true },
    select: {
      id: true,
      status: true,
      observacao: true,
      data_candidatura: true,
      updated_at: true,
      aluno: {
        id: true,
        nome: true,
        email: true,
        cpf: true,
        matricula: true,
        curso: true,
        periodo: true,
        apto: true,
        status: true,
        created_at: true,
      },
      vaga: {
        id: true,
        titulo: true,
        descricao: true,
        area: true,
        requisitos: true,
        carga_horaria: true,
        modalidade: true,
        status: true,
        created_at: true,
      }
    }
  })
  return row ?? undefined
}

  async buscarPorIdComAluno(id: number): Promise<Candidatura | undefined> {
    const row = await this.repo.findOne({
      where: { id },
      relations: { aluno: true }
    })
    return row ?? undefined
  }

  async criar(dados: Partial<Candidatura>): Promise<Candidatura> {
    const ent = this.repo.create(dados)
    return this.repo.save(ent)
  }

  async salvar(entidade: Candidatura): Promise<Candidatura> {
    return this.repo.save(entidade)
  }

  async remover(id: number): Promise<boolean> {
    const r = await this.repo.delete(id)
    return (r.affected ?? 0) > 0
  }
}