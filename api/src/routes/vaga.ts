import { Router } from "express"
import { AppDataSource } from "../database/data-source"
import { Vaga } from "../models/Vaga"
import { Empresa } from "../models/Empresa"
import { VagaRepository } from "../repositories/VagaRepository"
import { EmpresaRepository } from "../repositories/EmpresaRepository"
import { VagaService } from "../services/VagaService"
import { VagaController } from "../controllers/VagaController"

const router = Router()

const vagaRepository = new VagaRepository(AppDataSource.getRepository(Vaga))
const empresaRepository = new EmpresaRepository(AppDataSource.getRepository(Empresa))
const vagaService = new VagaService(vagaRepository, empresaRepository)
const vagaController = new VagaController(vagaService)

router.get("/", vagaController.listar)
router.get("/:id", vagaController.buscarPorId)
router.post("/", vagaController.criar)
router.put("/:id", vagaController.atualizar)
router.delete("/:id", vagaController.remover)

export default router