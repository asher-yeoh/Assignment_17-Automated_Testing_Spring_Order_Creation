import { Column, Entity, PrimaryGeneratedColumn, OneToMany } from "typeorm";
import { LineItem } from "./LineItem";

@Entity("products")
export class Product {

    @PrimaryGeneratedColumn()
    id: number;

    @OneToMany(type => LineItem, lineItem => lineItem.product)
    lineItems: LineItem[];

    @Column()
    name: String;

    @Column()
    price: number;

    @Column()
    quantity: number;

}
