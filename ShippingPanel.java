// --- ShippingPanel.java ---
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 
public class ShippingPanel extends JPanel {
    private MainFrame mainFrame;
    private JTextField nameField;
    private JTextField address1Field;
    private JTextField address2Field;
    private JTextField cityField;
    private JTextField pinField;
    public ShippingPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridBagLayout());
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Enter Shipping Details"));  
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(30);
        formPanel.add(nameField, gbc);     
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Address Line 1:"), gbc);
        gbc.gridx = 1;
        address1Field = new JTextField(30);
        formPanel.add(address1Field, gbc);
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Address Line 2:"), gbc);
        gbc.gridx = 1;
        address2Field = new JTextField(30);
        formPanel.add(address2Field, gbc);
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("City:"), gbc);
        gbc.gridx = 1;
        cityField = new JTextField(30);
        formPanel.add(cityField, gbc);     
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("PIN Code:"), gbc);
        gbc.gridx = 1;
        pinField = new JTextField(10);
        formPanel.add(pinField, gbc);        
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton paymentButton = new JButton("Proceed to Payment");
        paymentButton.setBackground(new Color(0, 102, 51));
        paymentButton.setForeground(Color.WHITE);
        formPanel.add(paymentButton, gbc);        
        add(formPanel);       
        paymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Save shipping details to temp fields in MainFrame
                mainFrame.tempShippingName = nameField.getText();
                mainFrame.tempShippingAddress = address1Field.getText() + " " + address2Field.getText();
                mainFrame.tempShippingCity = cityField.getText();
                mainFrame.tempShippingPin = pinField.getText();
                
                // Also save to shared cart object if in a shared session
                if (mainFrame.isSharedCartSession && mainFrame.currentSharedCart != null) {
                    SharedCart cart = mainFrame.currentSharedCart;
                    cart.shippingName = mainFrame.tempShippingName;
                    cart.shippingAddress = mainFrame.tempShippingAddress;
                    cart.shippingCity = mainFrame.tempShippingCity;
                    cart.shippingPin = mainFrame.tempShippingPin;
                }
                
                // --- MODIFIED CALL ---
                // We no longer need to tell the payment panel if it's a shared cart
                mainFrame.paymentPanel.preparePaymentView();
                mainFrame.switchToPanel("PAYMENT");
            }
        });
    }
}