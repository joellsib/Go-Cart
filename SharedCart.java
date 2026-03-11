// --- SharedCart.java ---
import java.util.List;
import java.util.Map;
import java.util.HashMap;

// This class holds all shared cart data
public class SharedCart {

    String cartName; // The "single username" for the cart
    String password;
    List<String> memberEmails; // All users who are part of this cart
    String phone1;
    String phone2;
    
    // The actual shopping cart items
    Map<Product, Integer> items;
    
    // --- paymentStatus map has been REMOVED ---
    
    // Shipping details, filled by the first user to check out
    String shippingName = "";
    String shippingAddress = "";
    String shippingCity = "";
    String shippingPin = "";

    public SharedCart(String cartName, String password, List<String> memberEmails, String phone1, String phone2) {
        this.cartName = cartName;
        this.password = password;
        this.memberEmails = memberEmails;
        this.phone1 = phone1;
        this.phone2 = phone2;
        
        this.items = new HashMap<>();
        
        // --- paymentStatus initialization has been REMOVED ---
    }
    
    // --- resetPaymentStatus() method has been REMOVED ---
    // --- allMembersHavePaid() method has been REMOVED ---
}