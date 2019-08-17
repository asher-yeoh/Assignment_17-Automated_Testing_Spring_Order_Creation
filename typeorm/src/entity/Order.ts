import { Entity, PrimaryGeneratedColumn, OneToMany } from "typeorm";
import { Payment } from "./Payment";
import { LineItem } from "./LineItem";

@Entity("orders")
export class Order {

    @PrimaryGeneratedColumn()
    id: number;

    @OneToMany(type => Payment, payment => payment.order)
    payments: Payment[];

    @OneToMany(type => LineItem, lineItem => lineItem.order)
    lineItems: LineItem[];

}
