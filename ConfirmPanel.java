// --- ConfirmPanel.java ---
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 

public class ConfirmPanel extends JPanel {

    private MainFrame mainFrame;
    private JLabel successLabel;
    private JLabel messageLabel; 
    private Order currentOrder; // NEW: Store the confirmed order

    public ConfirmPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridBagLayout());
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        successLabel = new JLabel("Order Confirmed!");
        successLabel.setFont(new Font("Arial", Font.BOLD, 28));
        successLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        messageLabel = new JLabel("Thank you for your purchase!");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton detailsButton = new JButton("View Order Details");
        detailsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton homeButton = new JButton("Back to Products");
        homeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(successLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(messageLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(detailsButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(homeButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(logoutButton); 

        add(panel);
        
        detailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentOrder != null) {
                    mainFrame.orderDetailsPanel.setOrderDetails(currentOrder);
                    mainFrame.switchToPanel("ORDER_DETAILS");
                }
            }
        });
        
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
    
    // NEW: Called from PaymentPanel to set details
    public void setConfirmationDetails(Order order) {
        this.currentOrder = order;
        successLabel.setText("Order #" + order.orderId + " Confirmed!");
        messageLabel.setText("Thank you, " + order.shippingName + "! Your order is " + order.orderStatus + ".");
    }
}