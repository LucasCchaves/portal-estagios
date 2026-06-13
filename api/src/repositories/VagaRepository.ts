import type { Repository } from "typeorm"
import type { Vaga } from "../models/Vaga"

export class VagaRepository {
  constructor(private readonly repo: Repository<Vaga>) {}

 async listarTodos(): Promise<Vaga[]> {
  return this.repo.find({
    relations: { empresa: true },
    select: {
      id: true,
      titulo: true,
      descricao: true,
      area: true,
      requisitos: true,
      carga_horaria: true,
      modalidade: true,
      status: true,
      created_at: true,
      empresa: {
        id: true,
        nome: true,
        cnpj: true,
        email: true,
        telefone: true,
        area_atuacao: true,
        status: true,
        created_at: true,
      }
    },
    order: { id: "ASC" }
  })
}

async buscarPorId(id: number): Promise<Vaga | undefined> {
  const row = await this.repo.findOne({
    where: { id },
    relations: { empresa: true },
    select: {
      id: true,
      titulo: true,
      descricao: true,
      area: true,
      requisitos: true,
      carga_horaria: true,
      modalidade: true,
      status: true,
      created_at: true,
      empresa: {
        id: true,
        nome: true,
        cnpj: true,
        email: true,
        telefone: true,
        area_atuacao: true,
        status: true,
        created_at: true,
      }
    }
  })
  return row ?? undefined
}

  async criar(dados: Partial<Vaga>): Promise<Vaga> {
    const ent = this.repo.create(dados)
    return this.repo.save(ent)
  }

  async salvar(entidade: Vaga): Promise<Vaga> {
    return this.repo.save(entidade)
  }

  async remover(id: number): Promise<boolean> {
    const r = await this.repo.delete(id)
    return (r.affected ?? 0) > 0
  }
}