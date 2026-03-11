// --- MainFrame.java ---
import javax.swing.*;
import java.awt.*;
import java.io.*; // For file handling
import java.util.ArrayList; 
import java.util.Arrays; 
import java.util.HashMap;
import java.util.List; // NEW
import java.util.Map;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    // --- All Panels ---
    private LoginPanel loginPanel;
    private SignUpPanel signUpPanel;
    private SharedSignUpPanel sharedSignUpPanel; 
    private SharedLoginPanel sharedLoginPanel; 
    private ProductPanel productPanel;
    CartPanel cartPanel;
    private ShippingPanel shippingPanel;
    PaymentPanel paymentPanel;
    ConfirmPanel confirmPanel;
    OrderDetailsPanel orderDetailsPanel;
    OrderHistoryPanel orderHistoryPanel; // NEW
    
    // --- Mock Databases ---
    public Map<String, User> userDatabase; // Key: Email
    public Map<String, SharedCart> sharedCartDatabase; // Key: CartName
    public List<Order> allOrders; // NEW: To store all orders
    
    // --- Mock Carts ---
    public Map<Product, Integer> personalCart; 
    
    // --- Current Session State ---
    public User currentLoggedInUser; 
    public Map<Product, Integer> currentCart; 
    public SharedCart currentSharedCart; 
    public boolean isSharedCartSession = false; 

    // --- NEW: Temporary shipping details ---
    public String tempShippingName, tempShippingAddress, tempShippingCity, tempShippingPin;
    
    // --- NEW: File constants ---
    private static final String USERS_FILE = "users.txt";
    private static final String SHARED_CARTS_FILE = "shared_carts.txt";
    private static final String SHARED_ITEMS_FILE = "shared_cart_items.txt";
    private static final String ORDERS_FILE = "orders.txt"; // NEW
    private static final String ORDER_ITEMS_FILE = "order_items.txt"; // NEW

    public MainFrame() {
        setTitle("Go Cart");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize databases
        userDatabase = new HashMap<>();
        sharedCartDatabase = new HashMap<>(); 
        personalCart = new HashMap<>();
        allOrders = new ArrayList<>(); // NEW

        // --- Load all data from files on startup ---
        loadUsers();
        loadSharedCarts();
        loadSharedCartItems();
        loadOrders(); // NEW (This loads from both orders.txt and order_items.txt)

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize all screen panels
        loginPanel = new LoginPanel(this);
        signUpPanel = new SignUpPanel(this);
        sharedSignUpPanel = new SharedSignUpPanel(this); 
        sharedLoginPanel = new SharedLoginPanel(this); 
        productPanel = new ProductPanel(this);
        cartPanel = new CartPanel(this);
        shippingPanel = new ShippingPanel(this);
        paymentPanel = new PaymentPanel(this);
        confirmPanel = new ConfirmPanel(this);
        orderDetailsPanel = new OrderDetailsPanel(this);
        orderHistoryPanel = new OrderHistoryPanel(this); // NEW

        // Add panels to the CardLayout
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(signUpPanel, "SIGNUP");
        mainPanel.add(sharedSignUpPanel, "SHARED_SIGNUP"); 
        mainPanel.add(sharedLoginPanel, "SHARED_LOGIN"); 
        mainPanel.add(productPanel, "PRODUCTS");
        mainPanel.add(cartPanel, "CART");
        mainPanel.add(shippingPanel, "SHIPPING");
        mainPanel.add(paymentPanel, "PAYMENT");
        mainPanel.add(confirmPanel, "CONFIRM");
        mainPanel.add(orderDetailsPanel, "ORDER_DETAILS");
        mainPanel.add(orderHistoryPanel, "ORDER_HISTORY"); // NEW

        add(mainPanel);
        cardLayout.show(mainPanel, "LOGIN");
    }

    public void switchToPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    // --- LOGIN METHODS ---

    public void userLoggedIn(User user) {
        this.currentLoggedInUser = user;
        this.currentCart = this.personalCart; 
        this.isSharedCartSession = false;
        this.currentSharedCart = null; 
        
        productPanel.setWelcomeMessage("Welcome, " + user.username);
    }
    
    public void sharedUserLoggedIn(User user, SharedCart cart) {
        this.currentLoggedInUser = user;
        this.currentCart = cart.items; 
        this.isSharedCartSession = true;
        this.currentSharedCart = cart; 
        
        productPanel.setWelcomeMessage("Welcome, " + user.username + " (Cart: " + cart.cartName + ")");
    }
    
    public void logout() {
        // Clear session
        currentLoggedInUser = null;
        currentCart = null;
        currentSharedCart = null;
        isSharedCartSession = false;
        
        // Clear volatile data
        personalCart.clear();
        tempShippingName = "";
        tempShippingAddress = "";
        tempShippingCity = "";
        tempShippingPin = "";
        
        switchToPanel("LOGIN");
    }

    // --- NEW: Method to get orders for a specific user ---
    public List<Order> getOrdersForUser(String userEmail) {
        List<Order> userOrders = new ArrayList<>();
        for (Order order : allOrders) {
            if (order.userEmail.equals(userEmail)) {
                userOrders.add(order);
            }
            // Also add orders from shared carts they are a member of
            else if (order.cartName != null) {
                SharedCart sc = sharedCartDatabase.get(order.cartName);
                if (sc != null && sc.memberEmails.contains(userEmail)) {
                    userOrders.add(order);
                }
            }
        }
        return userOrders;
    }
    
    // --- DATA PERSISTENCE METHODS ---

    private void loadUsers() {
        File file = new File(USERS_FILE);
        if (!file.exists()) return; 

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    User user = new User(parts[0], parts[1], parts[2], parts[3]);
                    userDatabase.put(user.email, user);
                }
            }
            System.out.println("Loaded " + userDatabase.size() + " users.");
        } catch (IOException e) {
            System.err.println("Error loading users file: " + e.getMessage());
        }
    }

    private void loadSharedCarts() {
        File file = new File(SHARED_CARTS_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    List<String> memberEmails = new ArrayList<>(Arrays.asList(parts[4].split(";")));
                    SharedCart cart = new SharedCart(parts[0], parts[1], memberEmails, parts[2], parts[3]);
                    sharedCartDatabase.put(cart.cartName, cart);
                }
            }
            System.out.println("Loaded " + sharedCartDatabase.size() + " shared carts.");
        } catch (IOException e) {
            System.err.println("Error loading shared carts file: " + e.getMessage());
        }
    }

    private void loadSharedCartItems() {
        File file = new File(SHARED_ITEMS_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    SharedCart cart = sharedCartDatabase.get(parts[0]);
                    if (cart != null) {
                        Product product = new Product(parts[1], Double.parseDouble(parts[2]));
                        cart.items.put(product, Integer.parseInt(parts[3]));
                    }
                }
            }
            System.out.println("Loaded shared cart items.");
        } catch (IOException e) {
            System.err.println("Error loading shared cart items file: " + e.getMessage());
        }
    }
    
    // --- NEW: Load Orders (2-part process) ---
    private void loadOrders() {
        Map<String, Order> tempOrderMap = new HashMap<>();
        
        // 1. Load main order data from orders.txt
        File ordersFile = new File(ORDERS_FILE);
        if (!ordersFile.exists()) return; // No orders to load

        try (BufferedReader reader = new BufferedReader(new FileReader(ordersFile))) {
            String line;
            // Format: orderId,userEmail,cartName,total,paymentMethod,status,shipName,shipAddress,shipCity,shipPin
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 10) {
                    Order order = new Order(parts[0], parts[1], parts[2].equals("null") ? null : parts[2], 
                                            new HashMap<>(), Double.parseDouble(parts[3]), 
                                            parts[6], parts[7], parts[8], parts[9], parts[4], parts[5]);
                    allOrders.add(order);
                    tempOrderMap.put(order.orderId, order);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading orders file: " + e.getMessage());
        }

        // 2. Load item data from order_items.txt and add to orders
        File itemsFile = new File(ORDER_ITEMS_FILE);
        if (!itemsFile.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(itemsFile))) {
            String line;
            // Format: orderId,productName,productPrice,quantity
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    Order order = tempOrderMap.get(parts[0]);
                    if (order != null) {
                        Product product = new Product(parts[1], Double.parseDouble(parts[2]));
                        order.items.put(product, Integer.parseInt(parts[3]));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading order items file: " + e.getMessage());
        }
        System.out.println("Loaded " + allOrders.size() + " orders.");
    }

    /**
     * Saves all in-memory data to the text files (Users, Carts, Cart Items).
     * This method overwrites the files. Orders are saved separately.
     */
    public synchronized void saveAllData() {
        // ... (This method is unchanged, it still saves users, shared carts, and shared cart items) ...
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (User user : userDatabase.values()) {
                writer.write(user.username + "," + user.email + "," + user.phone + "," + user.password);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving users file: " + e.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SHARED_CARTS_FILE))) {
            for (SharedCart cart : sharedCartDatabase.values()) {
                String memberEmails = String.join(";", cart.memberEmails);
                writer.write(cart.cartName + "," + cart.password + "," + cart.phone1 + "," + cart.phone2 + "," + memberEmails);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving shared carts file: " + e.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SHARED_ITEMS_FILE))) {
            for (SharedCart cart : sharedCartDatabase.values()) {
                for (Map.Entry<Product, Integer> entry : cart.items.entrySet()) {
                    Product product = entry.getKey();
                    int quantity = entry.getValue();
                    writer.write(cart.cartName + "," + product.name + "," + product.price + "," + quantity);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving shared cart items file: " + e.getMessage());
        }
        
        System.out.println("User/Cart data saved to files.");
    }

    /**
     * NEW: Appends a new order to the orders and order_items files.
     * This is called when an order is confirmed.
     */
    public synchronized void addNewOrder(Order order) {
        allOrders.add(order); // Add to in-memory list
        
        // 1. Append to orders.txt (true for append mode)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ORDERS_FILE, true))) {
            writer.write(order.orderId + "," + order.userEmail + "," + order.cartName + "," + 
                         order.totalAmount + "," + order.paymentMethod + "," + order.orderStatus + "," +
                         order.shippingName + "," + order.shippingAddress + "," + order.shippingCity + "," +
                         order.shippingPin);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving new order: " + e.getMessage());
        }
        
        // 2. Append to order_items.txt
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ORDER_ITEMS_FILE, true))) {
            for (Map.Entry<Product, Integer> entry : order.items.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                writer.write(order.orderId + "," + product.name + "," + product.price + "," + quantity);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving new order items: " + e.getMessage());
        }
        System.out.println("New order " + order.orderId + " saved.");
    }
}