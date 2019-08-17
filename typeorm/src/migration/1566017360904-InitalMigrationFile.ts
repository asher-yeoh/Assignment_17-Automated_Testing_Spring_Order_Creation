import {MigrationInterface, QueryRunner} from "typeorm";

export class InitalMigrationFile1566017360904 implements MigrationInterface {

    public async up(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query(`CREATE TABLE "payments" ("id" int NOT NULL IDENTITY(1,1), "order_id" int, "paid" bit NOT NULL, "refunded" bit NOT NULL, "amount" int NOT NULL, CONSTRAINT "PK_197ab7af18c93fbb0c9b28b4a59" PRIMARY KEY ("id"))`);
        await queryRunner.query(`CREATE TABLE "orders" ("id" int NOT NULL IDENTITY(1,1), CONSTRAINT "PK_710e2d4957aa5878dfe94e4ac2f" PRIMARY KEY ("id"))`);
        await queryRunner.query(`CREATE TABLE "products" ("id" int NOT NULL IDENTITY(1,1), "name" nvarchar(255) NOT NULL, "price" int NOT NULL, CONSTRAINT "PK_0806c755e0aca124e67c0cf6d7d" PRIMARY KEY ("id"))`);
        await queryRunner.query(`CREATE TABLE "line_items" ("id" int NOT NULL IDENTITY(1,1), "order_id" int, "product_id" int, "quantity" int NOT NULL, "price" int NOT NULL, CONSTRAINT "PK_6d227c876e374542dc9bb44dfb4" PRIMARY KEY ("id"))`);
        await queryRunner.query(`ALTER TABLE "payments" ADD CONSTRAINT "FK_b2f7b823a21562eeca20e72b006" FOREIGN KEY ("order_id") REFERENCES "orders"("id") ON DELETE NO ACTION ON UPDATE NO ACTION`);
        await queryRunner.query(`ALTER TABLE "line_items" ADD CONSTRAINT "FK_364f2fd50813438fe69360aef27" FOREIGN KEY ("order_id") REFERENCES "orders"("id") ON DELETE NO ACTION ON UPDATE NO ACTION`);
        await queryRunner.query(`ALTER TABLE "line_items" ADD CONSTRAINT "FK_450feb799862e681a2b41b6421a" FOREIGN KEY ("product_id") REFERENCES "products"("id") ON DELETE NO ACTION ON UPDATE NO ACTION`);
    }

    public async down(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query(`ALTER TABLE "line_items" DROP CONSTRAINT "FK_450feb799862e681a2b41b6421a"`);
        await queryRunner.query(`ALTER TABLE "line_items" DROP CONSTRAINT "FK_364f2fd50813438fe69360aef27"`);
        await queryRunner.query(`ALTER TABLE "payments" DROP CONSTRAINT "FK_b2f7b823a21562eeca20e72b006"`);
        await queryRunner.query(`DROP TABLE "line_items"`);
        await queryRunner.query(`DROP TABLE "products"`);
        await queryRunner.query(`DROP TABLE "orders"`);
        await queryRunner.query(`DROP TABLE "payments"`);
    }

}
