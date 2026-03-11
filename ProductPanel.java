// --- ProductPanel.java ---
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 
import java.io.File; // NEW IMPORT
import java.util.ArrayList;
import java.util.List;

public class ProductPanel extends JPanel {

    private MainFrame mainFrame;
    private JLabel welcomeLabel;
    private List<Product> productList;
    private final int IMG_WIDTH = 150; // NEW: Define image size
    private final int IMG_HEIGHT = 150; // NEW: Define image size

    public ProductPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(new Color(232, 233, 234)); 

        // --- Header (Unchanged) ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 102, 51)); 
        headerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("Go Cart");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        navPanel.setOpaque(false); 

        JButton homeButton = createNavButton("Home");
        JButton productsButton = createNavButton("Products");
        JButton ordersButton = createNavButton("My Orders"); 
        JButton aboutButton = createNavButton("About Us");
        JButton contactButton = createNavButton("Contact");
        JButton cartButton = createNavButton("View Cart"); 

        navPanel.add(homeButton);
        navPanel.add(productsButton);
        navPanel.add(ordersButton); 
        navPanel.add(aboutButton);
        navPanel.add(contactButton);
        navPanel.add(cartButton); 
        
        headerPanel.add(navPanel, BorderLayout.CENTER);
        
        welcomeLabel = new JLabel("Welcome!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBorder(new EmptyBorder(0,0,0,20)); 
        headerPanel.add(welcomeLabel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // --- Product Grid (Center) (Unchanged) ---
        JPanel productGridPanel = new JPanel(new GridLayout(0, 4, 15, 15)); 
        productGridPanel.setBackground(new Color(240, 248, 240)); 
        productGridPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        loadProducts(); // This is now updated
        
        for (Product product : productList) {
            productGridPanel.add(createProductItemPanel(product)); // This is now updated
        }

        JScrollPane scrollPane = new JScrollPane(productGridPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // --- Bottom Bar (Unchanged) ---
        JPanel southBar = new JPanel(new BorderLayout());
        JPanel goToCartPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        goToCartPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JButton goToCartButton = new JButton("Go to Shopping Cart");
        goToCartButton.setFont(new Font("Arial", Font.BOLD, 16));
        goToCartButton.setBackground(new Color(0, 102, 51));
        goToCartButton.setForeground(Color.WHITE);
        goToCartPanel.add(goToCartButton);
        southBar.add(goToCartPanel, BorderLayout.CENTER);
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(0, 102, 51));
        footerPanel.setPreferredSize(new Dimension(0, 40)); 
        southBar.add(footerPanel, BorderLayout.SOUTH);
        add(southBar, BorderLayout.SOUTH);

        // --- Action Listeners (Unchanged) ---
        cartButton.addActionListener(e -> goToCart());
        goToCartButton.addActionListener(e -> goToCart());
        homeButton.addActionListener(e -> mainFrame.switchToPanel("PRODUCTS"));
        productsButton.addActionListener(e -> mainFrame.switchToPanel("PRODUCTS"));
        ordersButton.addActionListener(e -> {
            mainFrame.orderHistoryPanel.refreshOrderList(mainFrame.currentLoggedInUser.email);
            mainFrame.switchToPanel("ORDER_HISTORY");
        });
        aboutButton.addActionListener(e -> JOptionPane.showMessageDialog(mainFrame, "About Us page not implemented."));
        contactButton.addActionListener(e -> JOptionPane.showMessageDialog(mainFrame, "Contact page not implemented."));
    }
    
    private void goToCart() {
        mainFrame.cartPanel.updateCartView(mainFrame.currentCart, mainFrame.isSharedCartSession); 
        mainFrame.switchToPanel("CART");
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // --- ### THIS IS THE MOST IMPORTANT CHANGE ### ---
    // Creates a single product item for the grid
    private JPanel createProductItemPanel(final Product product) { 
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        // --- NEW: Image Loading Logic ---
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(IMG_WIDTH, IMG_HEIGHT));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        String imagePath = "images/" + product.imageFileName;
        File imageFile = new File(imagePath);

        if (imageFile.exists()) {
            // Load the image
            ImageIcon icon = new ImageIcon(imagePath);
            // Scale the image
            Image img = icon.getImage();
            Image scaledImg = img.getScaledInstance(IMG_WIDTH, IMG_HEIGHT, Image.SCALE_SMOOTH);
            // Set it on the label
            imageLabel.setIcon(new ImageIcon(scaledImg));
        } else {
            // If image is missing, show placeholder text
            imageLabel.setText("Image Not Found");
            imageLabel.setOpaque(true);
            imageLabel.setBackground(Color.LIGHT_GRAY);
        }
        
        panel.add(imageLabel, BorderLayout.NORTH); // Add the image label to the top

        // Product details (Unchanged)
        JPanel detailsPanel = new JPanel(new GridLayout(2, 1));
        detailsPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        detailsPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(product.name);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        detailsPanel.add(nameLabel);

        JLabel priceLabel = new JLabel("₹" + String.format("%.2f", product.price));
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        detailsPanel.add(priceLabel);
        
        panel.add(detailsPanel, BorderLayout.CENTER);

        // Add to cart button (Unchanged)
        JButton addButton = new JButton("Add to Cart");
        addButton.setBackground(new Color(0, 102, 51));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.currentCart.put(product, mainFrame.currentCart.getOrDefault(product, 0) + 1);
                
                if (mainFrame.isSharedCartSession) {
                    mainFrame.saveAllData(); 
                }
                
                JOptionPane.showMessageDialog(mainFrame, 
                    product.name + " added to cart!", 
                    "Item Added", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        panel.add(addButton, BorderLayout.SOUTH);
        return panel;
    }

    public void setWelcomeMessage(String message) {
        welcomeLabel.setText(message);
    }

    // --- ### THIS METHOD IS ALSO UPDATED ### ---
    // Mock product data now includes image filenames
    private void loadProducts() {
        productList = new ArrayList<>();
        // Make sure these filenames match what you put in the 'images' folder!
        productList.add(new Product("Milk", 60.00, "37540.jpg"));
        productList.add(new Product("Peanut Butter", 250.00, "peanutbutter.jpg"));
        productList.add(new Product("Whole Wheat Flour", 180.00, "wheatflor.jpg"));
        productList.add(new Product("Brown Bread", 45.00, "brownbread.jpg"));
        productList.add(new Product("Tea Powder", 150.00, "teapowder.jpg"));
        productList.add(new Product("Coffee", 220.00, "coffepowder.jpg"));
        productList.add(new Product("Eggs (Dozen)", 80.00, "egg.jpg"));
        productList.add(new Product("White Rice (5kg)", 350.00, "whiterice.jpg"));
        productList.add(new Product("Lentils (Toor Dal)", 120.00, "lentils.jpg"));
        productList.add(new Product("Olive Oil", 450.00, "oliveoil.jpg"));
        productList.add(new Product("Cheddar Cheese", 300.00, "cheese.jpg"));
        productList.add(new Product("Apples (1kg)", 180.00, "apple.jpg"));
        productList.add(new Product("Bananas (Dozen)", 70.00, "banana.jpg"));
        productList.add(new Product("Yogurt", 50.00, "yogurt.jpg"));
        productList.add(new Product("Almonds (250g)", 280.00, "almond.jpg"));
        productList.add(new Product("Tomato Ketchup", 90.00, "tomatoketchep.jpg"));
        
        // If you don't have an image, you can use "default.png"
        // productList.add(new Product("Test Product", 10.00, "default.png"));
    }
}