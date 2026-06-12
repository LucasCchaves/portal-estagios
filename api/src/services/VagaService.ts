import type { VagaRepository } from "../repositories/VagaRepository"
import type { EmpresaRepository } from "../repositories/EmpresaRepository"
import type { Vaga } from "../models/Vaga"
import AppError from "../utils/AppError"

export class VagaService {
  constructor(
    private readonly repository: VagaRepository,
    private readonly empresaRepository: EmpresaRepository,
  ) {}

  async listar(): Promise<Vaga[]> {
    return this.repository.listarTodos()
  }

  async buscarPorId(id: number): Promise<Vaga> {
    const vaga = await this.repository.buscarPorId(id)
    if (!vaga) throw new AppError("Vaga não encontrada", 404)
    return vaga
  }

  async criar(dados: Partial<Vaga> & { empresa_id: number }): Promise<Vaga> {
    const empresa = await this.empresaRepository.buscarPorId(dados.empresa_id)
    if (!empresa) throw new AppError("Empresa não encontrada", 404)
    return this.repository.criar({ ...dados, empresa })
  }

  async atualizar(id: number, dados: Partial<Vaga> & { empresa_id?: number }): Promise<Vaga> {
    const vaga = await this.repository.buscarPorId(id)
    if (!vaga) throw new AppError("Vaga não encontrada", 404)

    if (dados.empresa_id) {
      const empresa = await this.empresaRepository.buscarPorId(dados.empresa_id)
      if (!empresa) throw new AppError("Empresa não encontrada", 404)
      vaga.empresa = empresa
    }

    if (dados.titulo !== undefined) vaga.titulo = dados.titulo
    if (dados.descricao !== undefined) vaga.descricao = dados.descricao
    if (dados.area !== undefined) vaga.area = dados.area
    if (dados.requisitos !== undefined) vaga.requisitos = dados.requisitos
    if (dados.carga_horaria !== undefined) vaga.carga_horaria = dados.carga_horaria
    if (dados.modalidade !== undefined) vaga.modalidade = dados.modalidade
    if (dados.status !== undefined) vaga.status = dados.status

    return this.repository.salvar(vaga)
  }

  async remover(id: number): Promise<void> {
    const ok = await this.repository.remover(id)
    if (!ok) throw new AppError("Vaga não encontrada", 404)
  }
}