import { Entity, PrimaryGeneratedColumn, Column, CreateDateColumn } from "typeorm"

@Entity("usuario")
export class Usuario {

  @PrimaryGeneratedColumn()
  id: number

  @Column({ type: "varchar", length: 255 })
  nome: string

  @Column({ type: "varchar", length: 255, unique: true })
  email: string

  @Column({ type: "varchar", length: 255 })
  senha: string

  @Column({ type: "varchar", length: 50, default: "operador" })
  perfil: string

  @Column({ type: "varchar", length: 20, default: "ativo" })
  status: string

  @CreateDateColumn()
  created_at: Date
}