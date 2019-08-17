package learning.appointmentapp.entities;

/**
 * Cart
 */
public class Cart {

    public String selectedProduct;
    public int quantity;

    public Cart(String selectedProduct, int quantity) {

        this.selectedProduct = selectedProduct;
        this.quantity = quantity;
    }
}