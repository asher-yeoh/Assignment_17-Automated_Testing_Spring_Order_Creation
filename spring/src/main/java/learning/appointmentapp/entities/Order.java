package learning.appointmentapp.entities;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Order
 */
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "order")
    private Set<Payment> payments;

    @OneToMany(mappedBy = "order")
    private Set<LineItem> lineItems;

    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public Set<Payment> getPayments() {
    	return this.payments;
    }

    public void setPayments(Set<Payment> payments) {
    	this.payments = payments;
    }

    public Set<LineItem> getLineItems() {
    	return this.lineItems;
    }

    public void setLineItems(Set<LineItem> lineItems) {
    	this.lineItems = lineItems;
    }

}