// --- CartPanel.java ---
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 
import java.util.Map;

public class CartPanel extends JPanel {

    private MainFrame mainFrame;
    private JPanel itemsPanel;
    private JLabel totalLabel;
    private JLabel splitLabel; // This will just be hidden now
    private JButton checkoutButton; 
    
    private final Color HEADER_BG = new Color(0, 102, 51);
    private final Color CANVAS_BG = new Color(232, 233, 234);

    public CartPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(CANVAS_BG);

        // --- Header ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_BG);
        headerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        JLabel titleLabel = new JLabel("Your Shopping Cart");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // --- Main Content ---
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPanel.setOpaque(false);

        itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // --- Summary & Checkout Panel ---
        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBackground(Color.WHITE);
        summaryPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JPanel totalsPanel = new JPanel(); 
        totalsPanel.setLayout(new BoxLayout(totalsPanel, BoxLayout.Y_AXIS));
        totalsPanel.setOpaque(false);

        totalLabel = new JLabel("Total: ₹0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 20));
        totalsPanel.add(totalLabel);
        
        splitLabel = new JLabel(""); // Keep this, but we won't set text to it
        splitLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        splitLabel.setForeground(Color.BLUE);
        totalsPanel.add(splitLabel);
        
        summaryPanel.add(totalsPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        JButton backToProducts = new JButton("Continue Shopping");
        
        checkoutButton = new JButton("Proceed to Checkout");
        checkoutButton.setBackground(HEADER_BG);
        checkoutButton.setForeground(Color.WHITE);
        
        buttonPanel.add(backToProducts);
        buttonPanel.add(checkoutButton);
        summaryPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        contentPanel.add(summaryPanel, BorderLayout.SOUTH);
        add(contentPanel, BorderLayout.CENTER);
        
        // --- Action Listeners ---
        backToProducts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.switchToPanel("PRODUCTS");
            }
        });
        
        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mainFrame.currentCart.isEmpty()) {
                    JOptionPane.showMessageDialog(mainFrame, "Your cart is empty.", "Empty Cart", JOptionPane.WARNING_MESSAGE);
                } else {
                    mainFrame.switchToPanel("SHIPPING");
                }
            }
        });
    }

    // This method is now called with the correct cart
    public void updateCartView(Map<Product, Integer> cart, boolean isShared) {
        itemsPanel.removeAll(); 
        
        if (cart.isEmpty()) {
            itemsPanel.add(new JLabel("  Your cart is empty."));
            totalLabel.setText("Total: ₹0.00");
            splitLabel.setText("");
            checkoutButton.setEnabled(false); // Disable checkout if empty
            itemsPanel.revalidate();
            itemsPanel.repaint();
            return;
        }

        double total = 0.0;
        itemsPanel.add(createCartItemHeader());

        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            total += product.price * quantity;
            itemsPanel.add(createCartItemPanel(product, quantity));
        }
        
        totalLabel.setText(String.format("Total: ₹%.2f", total));
        
        // --- MODIFIED LOGIC ---
        // We no longer show split details.
        splitLabel.setText(""); // Always hide split label
        checkoutButton.setText("Proceed to Checkout");
        checkoutButton.setEnabled(true); // Always enable if cart is not empty
        
        itemsPanel.revalidate();
        itemsPanel.repaint();
    }
    
    private JPanel createCartItemHeader() {
        JPanel header = new JPanel(new BorderLayout(10, 10));
        header.setBorder(new EmptyBorder(5, 10, 5, 10));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        header.setBackground(Color.LIGHT_GRAY);
        
        header.add(new JLabel("Product"), BorderLayout.WEST);
        header.add(new JLabel("Quantity"), BorderLayout.CENTER);
        header.add(new JLabel("Subtotal"), BorderLayout.EAST);
        return header;
    }

    private JPanel createCartItemPanel(final Product product, final int quantity) { 
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60)); 
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel nameLabel = new JLabel(product.name + " (₹" + product.price + ")");
        panel.add(nameLabel, BorderLayout.WEST);
        
        double subtotal = product.price * quantity;
        JLabel subtotalLabel = new JLabel(String.format("₹%.2f", subtotal));
        panel.add(subtotalLabel, BorderLayout.EAST);

        JPanel updatePanel = new JPanel();
        updatePanel.setOpaque(false);
        
        JButton minusButton = new JButton("-");
        minusButton.setMargin(new Insets(2, 5, 2, 5));
        
        JLabel qtyLabel = new JLabel(String.valueOf(quantity));
        qtyLabel.setBorder(new EmptyBorder(0, 10, 0, 10));
        
        JButton plusButton = new JButton("+");
        plusButton.setMargin(new Insets(2, 5, 2, 5));

        JButton removeButton = new JButton("Remove");
        removeButton.setForeground(Color.RED);
        removeButton.setOpaque(false);
        removeButton.setContentAreaFilled(false);
        removeButton.setBorder(new EmptyBorder(0, 20, 0, 0));

        updatePanel.add(minusButton);
        updatePanel.add(qtyLabel);
        updatePanel.add(plusButton);
        updatePanel.add(removeButton);
        
        panel.add(updatePanel, BorderLayout.CENTER);
        
        plusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.currentCart.put(product, quantity + 1);
                if (mainFrame.isSharedCartSession) mainFrame.saveAllData(); 
                updateCartView(mainFrame.currentCart, mainFrame.isSharedCartSession); 
            }
        });
        
        minusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (quantity > 1) {
                    mainFrame.currentCart.put(product, quantity - 1);
                } else {
                    mainFrame.currentCart.remove(product); 
                }
                if (mainFrame.isSharedCartSession) mainFrame.saveAllData(); 
                updateCartView(mainFrame.currentCart, mainFrame.isSharedCartSession);
            }
        });
        
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.currentCart.remove(product);
                if (mainFrame.isSharedCartSession) mainFrame.saveAllData(); 
                updateCartView(mainFrame.currentCart, mainFrame.isSharedCartSession);
            }
        });
        
        return panel;
    }
}