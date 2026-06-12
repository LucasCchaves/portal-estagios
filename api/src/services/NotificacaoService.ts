import type { NotificacaoRepository } from "../repositories/NotificacaoRepository"
import type { Notificacao } from "../models/Notificacao"
import AppError from "../utils/AppError"

export class NotificacaoService {
  constructor(private readonly repository: NotificacaoRepository) {}

  async listarPorAluno(alunoId: number): Promise<Notificacao[]> {
    return this.repository.listarPorAluno(alunoId)
  }

  async marcarComoLida(id: number): Promise<Notificacao> {
    const notificacao = await this.repository.buscarPorId(id)
    if (!notificacao) throw new AppError("Notificação não encontrada", 404)
    notificacao.lida = 1
    return this.repository.salvar(notificacao)
  }
}