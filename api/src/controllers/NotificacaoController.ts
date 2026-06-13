import type { NextFunction, Request, Response } from "express"
import type { NotificacaoService } from "../services/NotificacaoService"
import AppError from "../utils/AppError"

export class NotificacaoController {
  constructor(private readonly service: NotificacaoService) {}

  listarPorAluno = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const alunoId = Number(req.params.alunoId)
      if (!Number.isInteger(alunoId) || alunoId < 1) throw new AppError("Parâmetro alunoId inválido", 400)
      const notificacoes = await this.service.listarPorAluno(alunoId)
      res.json({ notificacoes })
    } catch (e) {
      next(e)
    }
  }

  marcarComoLida = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const id = Number(req.params.id)
      if (!Number.isInteger(id) || id < 1) throw new AppError("Parâmetro id inválido", 400)
      const notificacao = await this.service.marcarComoLida(id)
      res.json({ message: "Notificação marcada como lida", notificacao })
    } catch (e) {
      next(e)
    }
  }
}