import { Router } from "express"
import { AppDataSource } from "../database/data-source"
import { Notificacao } from "../models/Notificacao"
import { NotificacaoRepository } from "../repositories/NotificacaoRepository"
import { NotificacaoService } from "../services/NotificacaoService"
import { NotificacaoController } from "../controllers/NotificacaoController"

const router = Router()

const notificacaoRepository = new NotificacaoRepository(AppDataSource.getRepository(Notificacao))
const notificacaoService = new NotificacaoService(notificacaoRepository)
const notificacaoController = new NotificacaoController(notificacaoService)

router.get("/:alunoId", notificacaoController.listarPorAluno)
router.put("/:id/lida", notificacaoController.marcarComoLida)

export default router