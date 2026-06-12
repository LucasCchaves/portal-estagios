import { MigrationInterface, QueryRunner, TableColumn } from "typeorm"

export class AddSenhaAluno1781239656050 implements MigrationInterface {

  public async up(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.addColumn("aluno", new TableColumn({
      name: "senha",
      type: "varchar",
      length: "255",
      isNullable: true
    }))
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.dropColumn("aluno", "senha")
  }
}