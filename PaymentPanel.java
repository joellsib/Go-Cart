// --- PaymentPanel.java ---
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 
import java.util.HashMap;
import java.util.Map;

public class PaymentPanel extends JPanel {

    private MainFrame mainFrame;
    private JPanel formPanel; // Main panel
    
    // --- Panels for different payment modes ---
    private JPanel personalPaymentPanel; // Holds 3 options
    private JPanel creditCardPanel; // Holds credit card fields
    private JPanel upiPanel;
    private JPanel codPanel;
    private JPanel paymentMethodsCardPanel; 
    private CardLayout paymentMethodsCardLayout;
    
    // --- The sharedPayPanel and sharedPayLabel have been REMOVED ---
    
    private JButton confirmButton;
    private ButtonGroup paymentOptionGroup;
    private JRadioButton ccRadio, upiRadio, codRadio;

    public PaymentPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridBagLayout());
        
        formPanel = new JPanel(new BorderLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Payment Details"));

        // --- Panel for PERSONAL cart payment (NOW USED FOR ALL PAYMENTS) ---
        personalPaymentPanel = new JPanel(new BorderLayout(10, 10));
        
        // 1. Radio buttons for options
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ccRadio = new JRadioButton("Credit/Debit Card");
        ccRadio.setSelected(true);
        upiRadio = new JRadioButton("UPI");
        codRadio = new JRadioButton("Cash on Delivery");
        
        paymentOptionGroup = new ButtonGroup();
        paymentOptionGroup.add(ccRadio);
        paymentOptionGroup.add(upiRadio);
        paymentOptionGroup.add(codRadio);
        
        radioPanel.add(ccRadio);
        radioPanel.add(upiRadio);
        radioPanel.add(codRadio);
        personalPaymentPanel.add(radioPanel, BorderLayout.NORTH);

        // 2. CardLayout panel for payment details
        paymentMethodsCardLayout = new CardLayout();
        paymentMethodsCardPanel = new JPanel(paymentMethodsCardLayout);

        // -- Card 1: Credit Card --
        creditCardPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0; gbc.gridy = 0;
        creditCardPanel.add(new JLabel("Card Number:"), gbc);
        gbc.gridx = 1;
        creditCardPanel.add(new JTextField(20), gbc);
        gbc.gridx = 0; gbc.gridy++;
        creditCardPanel.add(new JLabel("Expiry (MM/YY):"), gbc);
        gbc.gridx = 1;
        creditCardPanel.add(new JTextField(5), gbc);
        gbc.gridx = 0; gbc.gridy++;
        creditCardPanel.add(new JLabel("CVV:"), gbc);
        gbc.gridx = 1;
        creditCardPanel.add(new JPasswordField(3), gbc);
        
        // -- Card 2: UPI --
        upiPanel = new JPanel(new GridBagLayout());
        gbc.gridx = 0; gbc.gridy = 0;
        upiPanel.add(new JLabel("Enter UPI ID:"), gbc);
        gbc.gridx = 1;
        upiPanel.add(new JTextField(20), gbc);
        
        // -- Card 3: COD --
        codPanel = new JPanel();
        codPanel.add(new JLabel("You will pay the full amount upon delivery."));

        paymentMethodsCardPanel.add(creditCardPanel, "CC");
        paymentMethodsCardPanel.add(upiPanel, "UPI");
        paymentMethodsCardPanel.add(codPanel, "COD");
        personalPaymentPanel.add(paymentMethodsCardPanel, BorderLayout.CENTER);

        // --- Add main panel to form ---
        formPanel.add(personalPaymentPanel, BorderLayout.NORTH);
        // The sharedPayPanel is no longer added

        // --- Confirmation Button ---
        JPanel buttonPanel = new JPanel();
        confirmButton = new JButton("Confirm Order");
        confirmButton.setBackground(new Color(0, 102, 51));
        confirmButton.setForeground(Color.WHITE);
        buttonPanel.add(confirmButton);
        formPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(formPanel);
        
        // --- ACTION LISTENERS ---
        
        // Listener for Radio Buttons to switch cards
        ActionListener radioListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ccRadio.isSelected()) {
                    paymentMethodsCardLayout.show(paymentMethodsCardPanel, "CC");
                } else if (upiRadio.isSelected()) {
                    paymentMethodsCardLayout.show(paymentMethodsCardPanel, "UPI");
                } else if (codRadio.isSelected()) {
                    paymentMethodsCardLayout.show(paymentMethodsCardPanel, "COD");
                }
            }
        };
        ccRadio.addActionListener(radioListener);
        upiRadio.addActionListener(radioListener);
        codRadio.addActionListener(radioListener);
        
        // --- MODIFIED: Listener for the main Confirm Button ---
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // We no longer check if it's shared. We just handle the payment.
                // The confirmOrder() method (called inside)
                // already knows if it's a shared cart or not.
                handlePersonalPayment();
            }
        });
    }
    
    // --- MODIFIED: Called from ShippingPanel ---
    // We no longer need the 'isShared' parameter
    public void preparePaymentView() {
        // This method is now much simpler.
        // It *always* shows the personal payment panel.
        personalPaymentPanel.setVisible(true);
        confirmButton.setText("Confirm Order");
        confirmButton.setEnabled(true);
    }

    private void handlePersonalPayment() {
        String paymentMethod;
        String orderStatus;
        if (ccRadio.isSelected()) {
            paymentMethod = "Credit Card";
            orderStatus = "Processing"; // Assume payment is instant
        } else if (upiRadio.isSelected()) {
            paymentMethod = "UPI";
            orderStatus = "Processing"; // Assume payment is instant
        } else {
            paymentMethod = "Cash on Delivery";
            orderStatus = "Placed"; // Payment not yet received
        }
        
        confirmOrder(paymentMethod, orderStatus);
    }
    
    // --- The handleSharedPayment() method has been DELETED ---

    // --- This method is UNCHANGED and already works for both cases ---
    private void confirmOrder(String paymentMethod, String orderStatus) {
        String orderId = String.valueOf(System.currentTimeMillis());
        String userEmail = mainFrame.currentLoggedInUser.email; // This is the user who is paying
        String cartName = mainFrame.isSharedCartSession ? mainFrame.currentSharedCart.cartName : null;
        
        Map<Product, Integer> itemsToOrder = new HashMap<>(mainFrame.currentCart);
        double total = calculateTotal(itemsToOrder);

        // Create the new Order object
        Order newOrder = new Order(
            orderId, userEmail, cartName, itemsToOrder, total,
            mainFrame.tempShippingName, mainFrame.tempShippingAddress,
            mainFrame.tempShippingCity, mainFrame.tempShippingPin,
            paymentMethod, orderStatus
        );
        
        // Save the new order to files
        mainFrame.addNewOrder(newOrder);
        
        // Clear the active cart
        mainFrame.currentCart.clear();
        
        // If it was a shared cart, we must save its now-empty state to the file
        if (mainFrame.isSharedCartSession) {
            mainFrame.saveAllData();
        }
        
        // Pass the new Order object to the confirm panel
        mainFrame.confirmPanel.setConfirmationDetails(newOrder);
        mainFrame.switchToPanel("CONFIRM");
    }
    
    // Helper to calculate total (unchanged)
    private double calculateTotal(Map<Product, Integer> cart) {
        double total = 0.0;
        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            total += entry.getKey().price * entry.getValue();
        }
        return total;
    }
}