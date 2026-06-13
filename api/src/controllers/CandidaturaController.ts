import type { NextFunction, Request, Response } from "express"
import { z } from "zod"
import type { CandidaturaService } from "../services/CandidaturaService"
import AppError from "../utils/AppError"

export class CandidaturaController {
  constructor(private readonly service: CandidaturaService) {}

  private schemaCriar = z.object({
    aluno_id: z.number({ message: "Aluno obrigatório" }),
    vaga_id: z.number({ message: "Vaga obrigatória" }),
    observacao: z.string().optional(),
  })

  private schemaStatus = z.object({
    status: z.enum(["pendente", "aprovada", "rejeitada"], {
      message: "Status inválido"
    }),
  })

  listar = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const vagaId = req.query.vagaId ? Number(req.query.vagaId) : undefined
      const candidaturas = await this.service.listar(vagaId)
      res.json({ candidaturas })
    } catch (e) {
      next(e)
    }
  }

  buscarPorId = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const id = Number(req.params.id)
      if (!Number.isInteger(id) || id < 1) throw new AppError("Parâmetro id inválido", 400)
      const candidatura = await this.service.buscarPorId(id)
      res.json({ candidatura })
    } catch (e) {
      next(e)
    }
  }

  criar = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const body = this.schemaCriar.parse(req.body)
      const candidatura = await this.service.criar(body)
      res.status(201).json({ candidatura })
    } catch (e) {
      next(e)
    }
  }

  atualizarStatus = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const id = Number(req.params.id)
      if (!Number.isInteger(id) || id < 1) throw new AppError("Parâmetro id inválido", 400)
      const { status } = this.schemaStatus.parse(req.body)
      const candidatura = await this.service.atualizarStatus(id, status)
      res.json({ message: "Status atualizado", candidatura })
    } catch (e) {
      next(e)
    }
  }

  remover = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const id = Number(req.params.id)
      if (!Number.isInteger(id) || id < 1) throw new AppError("Parâmetro id inválido", 400)
      await this.service.remover(id)
      res.json({ message: "Candidatura removida" })
    } catch (e) {
      next(e)
    }
  }
}