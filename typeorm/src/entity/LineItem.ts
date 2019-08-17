import { Column, Entity, PrimaryGeneratedColumn, ManyToOne, JoinColumn } from "typeorm";
import { Order } from "./Order";
import { Product } from "./Product";

@Entity("line_items")
export class LineItem {

    @PrimaryGeneratedColumn()
    id: number;

    @ManyToOne(type => Order, order => order.lineItems)
    @JoinColumn({name: "order_id"})
    order: Order;

    @ManyToOne(type => Product, product => product.lineItems)
    @JoinColumn({name: "product_id"})
    product: Product;

    @Column()
    quantity: number;

    @Column()
    price: number;

}
