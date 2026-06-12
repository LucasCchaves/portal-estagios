import { MigrationInterface, QueryRunner, Table } from "typeorm"

export class CreateUsuario1781235613974 implements MigrationInterface {

  public async up(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.createTable(new Table({
      name: "usuario",
      columns: [
        {
          name: "id",
          type: "int",
          isPrimary: true,
          isGenerated: true,
          generationStrategy: "increment"
        },
        {
          name: "nome",
          type: "varchar",
          length: "255"
        },
        {
          name: "email",
          type: "varchar",
          length: "255",
          isUnique: true
        },
        {
          name: "senha",
          type: "varchar",
          length: "255"
        },
        {
          name: "perfil",
          type: "varchar",
          length: "50",
          default: "'operador'"
        },
        {
          name: "status",
          type: "varchar",
          length: "20",
          default: "'ativo'"
        },
        {
          name: "created_at",
          type: "datetime",
          default: "CURRENT_TIMESTAMP"
        }
      ]
    }))
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.dropTable("usuario")
  }
}