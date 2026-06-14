import { MigrationInterface, QueryRunner, TableColumn } from "typeorm"

export class AddSenhaEmpresa1781239664073 implements MigrationInterface {

  public async up(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.addColumn("empresa", new TableColumn({
      name: "senha",
      type: "varchar",
      length: "255",
      isNullable: true
    }))
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.dropColumn("empresa", "senha")
  }
}