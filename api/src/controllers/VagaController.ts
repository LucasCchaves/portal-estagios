import type { NextFunction, Request, Response } from "express"
import { z } from "zod"
import type { VagaService } from "../services/VagaService"
import AppError from "../utils/AppError"

export class VagaController {
  constructor(private readonly service: VagaService) {}

  private schemaCriar = z.object({
    titulo: z.string({ message: "Título obrigatório" }).trim().min(1),
    descricao: z.string({ message: "Descrição obrigatória" }).min(1),
    area: z.string({ message: "Área obrigatória" }).min(1),
    requisitos: z.string().optional(),
    carga_horaria: z.number().optional(),
    modalidade: z.string().optional(),
    status: z.string().optional(),
    empresa_id: z.number({ message: "Empresa obrigatória" }),
  })

  private schemaAtualizar = z.object({
    titulo: z.string().trim().min(1).optional(),
    descricao: z.string().min(1).optional(),
    area: z.string().min(1).optional(),
    requisitos: z.string().optional(),
    carga_horaria: z.number().optional(),
    modalidade: z.string().optional(),
    status: z.string().optional(),
    empresa_id: z.number().optional(),
  })

  listar = async (_req: Request, res: Response, next: NextFunction) => {
    try {
      const vagas = await this.service.listar()
      res.json({ vagas })
    } catch (e) {
      next(e)
    }
  }

  buscarPorId = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const id = Number(req.params.id)
      if (!Number.isInteger(id) || id < 1) throw new AppError("Parâmetro id inválido", 400)
      const vaga = await this.service.buscarPorId(id)
      res.json({ vaga })
    } catch (e) {
      next(e)
    }
  }

  criar = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const body = this.schemaCriar.parse(req.body)
      const vaga = await this.service.criar(body)
      res.status(201).json({ vaga })
    } catch (e) {
      next(e)
    }
  }

  atualizar = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const id = Number(req.params.id)
      if (!Number.isInteger(id) || id < 1) throw new AppError("Parâmetro id inválido", 400)
      const body = this.schemaAtualizar.parse(req.body)
      const vaga = await this.service.atualizar(id, body)
      res.json({ message: "Vaga atualizada", vaga })
    } catch (e) {
      next(e)
    }
  }

  remover = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const id = Number(req.params.id)
      if (!Number.isInteger(id) || id < 1) throw new AppError("Parâmetro id inválido", 400)
      await this.service.remover(id)
      res.json({ message: "Vaga removida" })
    } catch (e) {
      next(e)
    }
  }
}