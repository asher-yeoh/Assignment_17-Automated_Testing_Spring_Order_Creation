package learning.appointmentapp.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

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
 * ShoppingServiceTest
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ShoppingServiceTest {

    @Autowired
    ShoppingService shoppingService;

    @Autowired
    ProductRepository productRepo;

    @Autowired
    OrderRepository orderRepo;

    @Autowired
    PaymentRepository paymentRepo;

    @Autowired
    LineItemRepository lineItemRepo;

    public Product seedProduct(String name, int price, int quantity) {

        Product seededProduct = new Product();

        seededProduct.setName(name);
        seededProduct.setPrice(price);
        seededProduct.setQuantity(quantity);
        productRepo.save(seededProduct);

        return seededProduct;
    }

    public Order seedOrder() {

        Order seededOrder = new Order();
        orderRepo.save(seededOrder);

        return seededOrder;
    }

    public Payment seedPayment(Order order, boolean paid, boolean refunded, int amount) {

        Payment seededPayment = new Payment();

        seededPayment.setOrder(order);
        seededPayment.setPaid(paid);
        seededPayment.setRefunded(refunded);
        seededPayment.setAmount(amount);
        paymentRepo.save(seededPayment);

        return seededPayment;
    }

    public LineItem seedLineItem(Order order, Product product, int quantity, int price) {

        LineItem seededLineItem = new LineItem();

        seededLineItem.setOrder(order);
        seededLineItem.setProduct(product);
        seededLineItem.setQuantity(quantity);
        seededLineItem.setPrice(price);
        lineItemRepo.save(seededLineItem);

        return seededLineItem;
    }

    @Test
    public void testCreateOrderValid() {

        // GIVE
        // Order with 2 handphones and 3 laptops.
        // Selected items are valid products.
        // Selected items have sufficient inventory.
        Product laptop = seedProduct("Dell Latitude 7390 2-in-1", 6500, 17);
        Product handphone = seedProduct("iPhone XS", 6000, 6);

        List<Cart> cart = new ArrayList<Cart>();
        
        cart.add(new Cart("iPhone XS", 2));
        cart.add(new Cart("Dell Latitude 7390 2-in-1", 3));

        // WHEN:
        // An order is created.
        List<LineItem> result = shoppingService.checkCreateOrder(cart);

        // THEN:
        // Check if this is a order.
        // Check if the line items are created with correct details - product_id/quantity/price.
        assertEquals(2, result.size());

        assertNotEquals(null, result.get(0).getOrder().getId());
        assertEquals(handphone.getId(), result.get(0).getProduct().getId());
        assertEquals(2, result.get(0).getQuantity());
        assertEquals(12000, result.get(0).getPrice());
        
        assertNotEquals(null, result.get(1).getOrder().getId());
        assertEquals(laptop.getId(), result.get(1).getProduct().getId());
        assertEquals(3, result.get(1).getQuantity());
        assertEquals(19500, result.get(1).getPrice());
    }

    @Test
    public void testCreateOrderInvalidProduct() {

        // GIVE
        // Order with 2 handphones and 3 laptops.
        // Some of the selected items are invalid products.
        seedProduct("Dell Latitude 7390 2-in-1", 6500, 17);
        seedProduct("iPhone XS", 6000, 6);

        List<Cart> cart = new ArrayList<Cart>();
        
        cart.add(new Cart("iPhone XS Plus", 2));
        cart.add(new Cart("Dell Latitude 7390 2-in-1", 3));

        // WHEN:
        // An order is created.
        List<LineItem> result = shoppingService.checkCreateOrder(cart);

        // THEN:
        // Check if this is an invalid order.
        assertEquals(null, result);
    }

    @Test
    public void testCreateOrderInvalidNoInventory() {

        // GIVE
        // Order with 2 handphones and 3 laptops.
        // Some of the selected items have no inventory.
        seedProduct("Dell Latitude 7390 2-in-1", 6500, 17);
        seedProduct("iPhone XS", 6000, 0);

        List<Cart> cart = new ArrayList<Cart>();
        
        cart.add(new Cart("iPhone XS", 2));
        cart.add(new Cart("Dell Latitude 7390 2-in-1", 3));

        // WHEN:
        // An order is created.
        List<LineItem> result = shoppingService.checkCreateOrder(cart);

        // THEN:
        // Check if this is an invalid order.
        assertEquals(null, result);
    }

    @Test
    public void testCreatePaymentValid() {

        // GIVE
        // Order with line items already created.
        Product laptop = seedProduct("Dell Latitude 7390 2-in-1", 6500, 17);
        Product handphone = seedProduct("iPhone XS", 6000, 6);

        Order order = seedOrder();
        orderRepo.save(order);

        LineItem lineItem1 = seedLineItem(order, handphone, 2, 12000);
        lineItemRepo.save(lineItem1);

        LineItem lineItem2 = seedLineItem(order, laptop, 3, 19500);        
        lineItemRepo.save(lineItem2);

        Long orderId = order.getId();
   
        // WHEN:
        // Payment is created for the order.
        Payment result = shoppingService.checkCreatePayment(orderId);

        // THEN:
        // Check if this is a valid payment.
        // Check if the payment is created with correct details - order_id/paid=true/refunded=false/amount=<subtotal of all line items>.
        assertNotEquals(null, result);
        assertNotEquals(null, result.getId());
        assertEquals(orderId, result.getOrder().getId());
        assertEquals(true, result.getPaid());
        assertEquals(false, result.getRefunded());
        assertEquals(31500, result.getAmount());
    }

    @Test
    public void testCreatePaymentInValidOrder() {

        // GIVE
        // Invalid order is given which does not exist in Orders table.
        Product laptop = seedProduct("Dell Latitude 7390 2-in-1", 6500, 17);
        Product handphone = seedProduct("iPhone XS", 6000, 6);

        Order validOrder = seedOrder();
        orderRepo.save(validOrder);

        Order invalidOrder = seedOrder();
        orderRepo.save(invalidOrder);

        LineItem lineItem1 = seedLineItem(validOrder, handphone, 2, 12000);
        LineItem lineItem2 = seedLineItem(validOrder, laptop, 3, 19500);

        lineItemRepo.save(lineItem1);
        lineItemRepo.save(lineItem2);

        Long orderId = invalidOrder.getId();

        orderRepo.deleteById(invalidOrder.getId());
   
        // WHEN:
        // Payment is created for the order.
        Payment result = shoppingService.checkCreatePayment(orderId);

        // THEN:
        // Check if this is an invalid payment.
        assertEquals(null, result);
    }

    @Test
    public void testRefundPaymentValid() {

        // GIVE
        // Payment is created for an order with line items.
        // Order is already paid and refund status is not true.
        Product laptop = seedProduct("Dell Latitude 7390 2-in-1", 6500, 17);
        Product handphone = seedProduct("iPhone XS", 6000, 6);

        Order order = seedOrder();
        orderRepo.save(order);

        LineItem lineItem1 = seedLineItem(order, handphone, 2, 12000);
        lineItemRepo.save(lineItem1);

        LineItem lineItem2 = seedLineItem(order, laptop, 3, 19500);        
        lineItemRepo.save(lineItem2);

        Payment payment = seedPayment(order, true, false, 31500);

        Long orderId = payment.getOrder().getId();
        Long paymentId = payment.getId();
   
        // WHEN:
        // Refund is requested for a payment.
        Payment result = shoppingService.checkRefundPayment(orderId);

        // THEN:
        // Check if this is a valid refund.
        // Check if the refund is recorded with correct details - order_id/paid=true/refunded=true.
        assertNotEquals(null, result);
        assertEquals(paymentId, result.getId());
        assertEquals(orderId, result.getOrder().getId());
        assertEquals(true, result.getPaid());
        assertEquals(true, result.getRefunded());
    }

    @Test
    public void testRefundPaymentInvalidUnpaid() {

        // GIVE
        // Payment is created for an order with line items.
        // Order is still unpaid and refund status is not true.
        Product laptop = seedProduct("Dell Latitude 7390 2-in-1", 6500, 17);
        Product handphone = seedProduct("iPhone XS", 6000, 6);

        Order order = seedOrder();
        orderRepo.save(order);

        LineItem lineItem1 = seedLineItem(order, handphone, 2, 12000);
        lineItemRepo.save(lineItem1);

        LineItem lineItem2 = seedLineItem(order, laptop, 3, 19500);        
        lineItemRepo.save(lineItem2);

        Payment payment = seedPayment(order, false, false, 31500);

        Long orderId = payment.getOrder().getId();
        Long paymentId = payment.getId();
   
        // WHEN:
        // Refund is requested for a payment.
        Payment result = shoppingService.checkRefundPayment(orderId);

        // THEN:
        // Check if this is an invalid refund where order is not paid yet.
        // Check if the refund is kept with correct details - order_id/paid=false/refunded=false.
        assertNotEquals(null, result);
        assertEquals(paymentId, result.getId());
        assertEquals(orderId, result.getOrder().getId());
        assertEquals(false, result.getPaid());
        assertEquals(false, result.getRefunded());
    }

    @Test
    public void testRefundPaymentInvalidInvalidOrder() {

        // GIVE
        // Payment is created for an order with line items.
        // Order is already paid and refund status is already true.
        Product laptop = seedProduct("Dell Latitude 7390 2-in-1", 6500, 17);
        Product handphone = seedProduct("iPhone XS", 6000, 6);

        Order order = seedOrder();
        orderRepo.save(order);

        LineItem lineItem1 = seedLineItem(order, handphone, 2, 12000);
        lineItemRepo.save(lineItem1);

        LineItem lineItem2 = seedLineItem(order, laptop, 3, 19500);        
        lineItemRepo.save(lineItem2);

        Payment payment = seedPayment(order, true, true, 31500);

        Long orderId = payment.getOrder().getId();
        Long paymentId = payment.getId();
   
        // WHEN:
        // Refund is requested for a payment.
        Payment result = shoppingService.checkRefundPayment(orderId);

        // THEN:
        // Check if this is an invalid refund where refund status is already true.
        // Check if the refund is kept with correct details - order_id/paid=true/refunded=false.
        assertNotEquals(null, result);
        assertEquals(paymentId, result.getId());
        assertEquals(orderId, result.getOrder().getId());
        assertEquals(true, result.getPaid());
        assertEquals(true, result.getRefunded());
    }

    @Test
    public void testRefundPaymentInvalidRefunded() {

        // GIVE
        // Payment is created for an order with line items.
        // Order is already paid and refund status is already true.
        Product laptop = seedProduct("Dell Latitude 7390 2-in-1", 6500, 17);
        Product handphone = seedProduct("iPhone XS", 6000, 6);

        Order validOrder = seedOrder();
        orderRepo.save(validOrder);

        Order invalidOrder = seedOrder();
        orderRepo.save(invalidOrder);

        LineItem lineItem1 = seedLineItem(validOrder, handphone, 2, 12000);
        lineItemRepo.save(lineItem1);

        LineItem lineItem2 = seedLineItem(validOrder, laptop, 3, 19500);        
        lineItemRepo.save(lineItem2);

        seedPayment(validOrder, true, true, 31500);

        Long orderId = invalidOrder.getId();
   
        // WHEN:
        // Refund is requested for a payment.
        Payment result = shoppingService.checkRefundPayment(orderId);

        // THEN:
        // Check if this is an invalid refund where order does not eist in Order table.
        assertEquals(null, result);
    }
}