import { Router } from "express"
import { AppDataSource } from "../database/data-source"
import { Candidatura } from "../models/Candidatura"
import { Aluno } from "../models/Aluno"
import { Vaga } from "../models/Vaga"
import { Notificacao } from "../models/Notificacao"
import { CandidaturaRepository } from "../repositories/CandidaturaRepository"
import { AlunoRepository } from "../repositories/AlunoRepository"
import { VagaRepository } from "../repositories/VagaRepository"
import { NotificacaoRepository } from "../repositories/NotificacaoRepository"
import { CandidaturaService } from "../services/CandidaturaService"
import { CandidaturaController } from "../controllers/CandidaturaController"

const router = Router()

const candidaturaRepository = new CandidaturaRepository(AppDataSource.getRepository(Candidatura))
const alunoRepository = new AlunoRepository(AppDataSource.getRepository(Aluno))
const vagaRepository = new VagaRepository(AppDataSource.getRepository(Vaga))
const notificacaoRepository = new NotificacaoRepository(AppDataSource.getRepository(Notificacao))
const candidaturaService = new CandidaturaService(candidaturaRepository, alunoRepository, vagaRepository, notificacaoRepository)
const candidaturaController = new CandidaturaController(candidaturaService)

router.get("/", candidaturaController.listar)
router.get("/:id", candidaturaController.buscarPorId)
router.post("/", candidaturaController.criar)
router.put("/:id/status", candidaturaController.atualizarStatus)
router.delete("/:id", candidaturaController.remover)

export default router