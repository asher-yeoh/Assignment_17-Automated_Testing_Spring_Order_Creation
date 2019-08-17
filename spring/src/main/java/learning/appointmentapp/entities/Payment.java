package learning.appointmentapp.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Payment
 */
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "paid")
    private boolean paid;

    @Column(name = "refunded")
    private boolean refunded;

    @Column(name = "amount")
    private int amount;

    @ManyToOne()
    @JoinColumn(name = "order_id")
    private Order order;
    
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public boolean getPaid() {
        return this.paid;
    }
    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public boolean getRefunded() {
        return this.refunded;
    }
    public void setRefunded(boolean refunded) {
        this.refunded = refunded;
    }
    
    public int getAmount() {
        return this.amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Order getOrder() {
        return this.order;
    }
    public void setOrder(Order order) {
        this.order = order;
    }  
    
}