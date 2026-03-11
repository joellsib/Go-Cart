// --- OrderHistoryPanel.java ---
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

// NEW CLASS: This panel displays a list of past orders
public class OrderHistoryPanel extends JPanel {

    private MainFrame mainFrame;
    private JPanel orderListPanel; // A panel to list all the orders
    private final Color HEADER_BG = new Color(0, 102, 51);

    public OrderHistoryPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        
        // --- Header ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(HEADER_BG);
        headerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        JLabel titleLabel = new JLabel("Your Order History");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);
        
        // --- Order List ---
        orderListPanel = new JPanel();
        orderListPanel.setLayout(new BoxLayout(orderListPanel, BoxLayout.Y_AXIS));
        orderListPanel.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(orderListPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(scrollPane, BorderLayout.CENTER);
        
        // --- Footer / Nav ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backButton = new JButton("Back to Products");
        footerPanel.add(backButton);
        add(footerPanel, BorderLayout.SOUTH);
        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.switchToPanel("PRODUCTS");
            }
        });
    }
    
    // This is called from ProductPanel before switching here
    public void refreshOrderList(String userEmail) {
        orderListPanel.removeAll(); // Clear old list
        
        List<Order> userOrders = mainFrame.getOrdersForUser(userEmail);
        
        if (userOrders.isEmpty()) {
            orderListPanel.add(new JLabel("You have no previous orders."));
        } else {
            // Show newest orders first
            Collections.reverse(userOrders);
            for (final Order order : userOrders) { // 'final' for use in listener
                orderListPanel.add(createOrderEntryPanel(order));
                orderListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        
        orderListPanel.revalidate();
        orderListPanel.repaint();
    }
    
    // Creates a single row for an order
    private JPanel createOrderEntryPanel(final Order order) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panel.setBackground(Color.WHITE);
        
        JLabel summaryLabel = new JLabel(order.getOrderSummary());
        summaryLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(summaryLabel, BorderLayout.CENTER);
        
        JButton detailsButton = new JButton("View Details");
        detailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Now we use the OrderDetailsPanel to show this specific order
                mainFrame.orderDetailsPanel.setOrderDetails(order);
                mainFrame.switchToPanel("ORDER_DETAILS");
            }
        });
        panel.add(detailsButton, BorderLayout.EAST);
        
        return panel;
    }
}
