// --- OrderDetailsPanel.java ---
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 
import java.util.Map;

public class OrderDetailsPanel extends JPanel {

    private MainFrame mainFrame;
    private JTextArea detailsArea;

    public OrderDetailsPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Order Details");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(detailsArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton homeButton = new JButton("Back to Products");
        JButton logoutButton = new JButton("Logout"); 
        
        buttonPanel.add(homeButton);
        buttonPanel.add(logoutButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.switchToPanel("PRODUCTS");
            }
        });
        
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.logout();
            }
        });
    }

    // MODIFIED: This now takes a full Order object
    public void setOrderDetails(Order order) {
        if (order == null) {
            detailsArea.setText("No order details found.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Order ID: ").append(order.orderId).append("\n");
        sb.append("Order Status: ").append(order.orderStatus).append("\n");
        sb.append("Paid via: ").append(order.paymentMethod).append("\n");
        
        if (order.cartName != null) {
            sb.append("Fulfilled from Shared Cart: ").append(order.cartName).append("\n");
        }
        sb.append("\n--- Shipping Details ---\n");
        sb.append("Name:    ").append(order.shippingName).append("\n");
        sb.append("Address: ").append(order.shippingAddress).append("\n");
        sb.append("         ").append(order.shippingCity).append(", ").append(order.shippingPin).append("\n");

        sb.append("\n--- Items ---\n");
        sb.append("--------------------------------------------\n");
        sb.append(String.format("%-20s %-10s %-10s\n", "Product", "Quantity", "Subtotal"));
        sb.append("--------------------------------------------\n");
        
        for (Map.Entry<Product, Integer> entry : order.items.entrySet()) {
            Product p = entry.getKey();
            int qty = entry.getValue();
            double subtotal = p.price * qty;
            sb.append(String.format("%-20s %-10d ₹%-10.2f\n", p.name, qty, subtotal));
        }
        
        sb.append("--------------------------------------------\n");
        sb.append(String.format("%-31s ₹%-10.2f\n", "TOTAL:", order.totalAmount));
        
        detailsArea.setText(sb.toString());
    }
}