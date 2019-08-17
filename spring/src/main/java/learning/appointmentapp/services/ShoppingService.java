package learning.appointmentapp.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import learning.appointmentapp.entities.Cart;
import learning.appointmentapp.entities.LineItem;
import learning.appointmentapp.entities.Order;
import learning.appointmentapp.entities.Payment;
import learning.appointmentapp.entities.Product;
import learning.appointmentapp.repositories.LineItemRepository;
import learning.appointmentapp.repositories.OrderRepository;
import learning.appointmentapp.repositories.PaymentRepository;
import learning.appointmentapp.repositories.ProductRepository;

/**
 * ShoppingService
 */
@Service
public class ShoppingService {

    @Autowired
    ProductRepository productRepo;

    @Autowired
    OrderRepository orderRepo;

    @Autowired
    PaymentRepository paymentRepo;

    @Autowired
    LineItemRepository lineItemRepo;

    public List<LineItem> checkCreateOrder(List<Cart> cart) {

        // Check if the selected items are isValid products.
        // Check if the selected items have sufficient inventory.
        // Create the order.
        // Create the line items for the order.
        List<Product> products = productRepo.findAll();

        ArrayList<Boolean> checklist = new ArrayList<Boolean>();

        boolean isValid = true;

        for (Cart item: cart) {

            boolean isAddItem = false;
            
            innerloop:
            for (Product product: products) {

                if ((item.selectedProduct.equals(product.getName())) && (item.quantity <= product.getQuantity())) {

                    isAddItem = true;
                    
                    break innerloop;
                }
            }

            checklist.add(isAddItem);
        }

        for (boolean check: checklist) {
            if (!check) {

                isValid = false;
                
                break;
            }
        }

        if (isValid) {

            Order order = new Order();

            orderRepo.save(order);

            List<LineItem> lineItems = new ArrayList<LineItem>();

            for (Cart item: cart) {

                innerloop:
                for (Product product: products) {

                    if ((item.selectedProduct.equals(product.getName()))) {

                        int subtotal = item.quantity * product.getPrice();

                        LineItem lineItem = new LineItem();

                        lineItem.setOrder(order);
                        lineItem.setProduct(product);
                        lineItem.setQuantity(item.quantity);
                        lineItem.setPrice(subtotal);

                        lineItemRepo.save(lineItem);

                        lineItems.add(lineItem);

                        break innerloop;
                    }
                }
            }

            return lineItems;

        } else {

            return null;
        }
    }

    public Payment checkCreatePayment(Long orderId) {

        // Check if the selected order_id exists in Orders table.
        // Get the grand total of subtotal of all line items for this order.
        List<Order> orders = orderRepo.findAll();
        List<LineItem> lineItems = lineItemRepo.findAll();

        Order selectedOrder = null;

        for (Order order: orders) {
            if (orderId == order.getId()) {

                selectedOrder = order;

                break;
            }
        }

        if (selectedOrder != null) {

            int amount = 0;

            for (LineItem lineItem: lineItems) {
                if (orderId == lineItem.getOrder().getId()) {
                    
                    amount += lineItem.getPrice();
                }
            }
    
            Payment payment = new Payment();
    
            payment.setOrder(selectedOrder);
            payment.setPaid(true);
            payment.setRefunded(false);
            payment.setAmount(amount);
    
            paymentRepo.save(payment);
    
            return payment;
        
        } else {

            return null;
        }
    }

    public Payment checkRefundPayment(Long orderId) {

        // Check if the selected order_id exists in Orders table.
        // Check if the payment_id for the order exists in Payments table.
        // Check if paid=true and refunded=false for the payment.
        List<Order> orders = orderRepo.findAll();
        List<Payment> payments = paymentRepo.findAll();

        Order selectedOrder = null;

        Payment approvedRefund = null;

        for (Order order: orders) {
            if (orderId == order.getId()) {

                selectedOrder = order;

                break;
            }
        }

        if (selectedOrder != null) {

            for (Payment payment: payments) {
                if (orderId == payment.getOrder().getId()) {

                    if ((payment.getPaid() == true) && (payment.getRefunded() == false)) {

                        payment.setRefunded(true);

                        approvedRefund = payment;

                    } else {
                        
                        approvedRefund = payment;
                    }
                }
            }

            return approvedRefund;

        } else {

            return null;
        }
    }
}