// --- Order.java ---
import java.util.Map;
import java.util.HashMap;

// NEW CLASS: This holds all data for a completed order
public class Order {
    String orderId;
    String userEmail; // The email of the user who placed it
    String cartName; // Null if it's a personal order
    Map<Product, Integer> items;
    double totalAmount;
    
    // Shipping info, copied at time of order
    String shippingName;
    String shippingAddress;
    String shippingCity;
    String shippingPin;
    
    String paymentMethod;
    String orderStatus; // e.g., "Processing", "Pending", "Shipped"

    public Order(String orderId, String userEmail, String cartName, Map<Product, Integer> items, 
                 double totalAmount, String shippingName, String shippingAddress, String shippingCity, 
                 String shippingPin, String paymentMethod, String orderStatus) {
        
        this.orderId = orderId;
        this.userEmail = userEmail;
        this.cartName = cartName;
        this.items = new HashMap<>(items); // Create a new copy of the map
        this.totalAmount = totalAmount;
        this.shippingName = shippingName;
        this.shippingAddress = shippingAddress;
        this.shippingCity = shippingCity;
        this.shippingPin = shippingPin;
        this.paymentMethod = paymentMethod;
        this.orderStatus = orderStatus;
    }
    public String getOrderSummary() {
        return String.format("ID: %s | Status: %s | Total: ₹%.2f | Paid via: %s",
                             orderId, orderStatus, totalAmount, paymentMethod);
    }
}
