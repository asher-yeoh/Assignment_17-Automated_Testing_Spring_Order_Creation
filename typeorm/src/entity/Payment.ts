import { Column, Entity, PrimaryGeneratedColumn, ManyToOne, JoinColumn } from "typeorm";
import { Order } from "./Order";

@Entity("payments")
export class Payment {

    @PrimaryGeneratedColumn()
    id: number;

    @ManyToOne(type => Order, order => order.payments)
    @JoinColumn({name: "order_id"})
    order: Order;

    @Column()
    paid: boolean;

    @Column()
    refunded: boolean;

    @Column()
    amount: number;

}
