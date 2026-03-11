// --- SharedSignUpPanel.java ---
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class SharedSignUpPanel extends JPanel {

    private MainFrame mainFrame;
    private JTextField cartNameField;
    private JPasswordField passwordField;
    private JTextArea emailsArea; 
    private JTextField phone1Field;
    private JTextField phone2Field;

    public SharedSignUpPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridBagLayout());
        setBackground(new Color(232, 233, 234));

        JPanel signUpBox = new JPanel(new GridBagLayout());
        signUpBox.setBackground(Color.WHITE);
        signUpBox.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        
        JLabel titleLabel = new JLabel("Create Shared Cart");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        signUpBox.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        
        gbc.gridy++;
        signUpBox.add(new JLabel("Cart Name (for login):"), gbc);
        gbc.gridy++;
        signUpBox.add(new JLabel("Cart Password:"), gbc);
        gbc.gridy++;
        signUpBox.add(new JLabel("Phone 1:"), gbc);
        gbc.gridy++;
        signUpBox.add(new JLabel("Phone 2:"), gbc);
        gbc.gridy++;
        signUpBox.add(new JLabel("Member Emails (one per line):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cartNameField = new JTextField(20);
        signUpBox.add(cartNameField, gbc);
        gbc.gridy++;
        passwordField = new JPasswordField(20);
        signUpBox.add(passwordField, gbc);
        gbc.gridy++;
        phone1Field = new JTextField(20);
        signUpBox.add(phone1Field, gbc);
        gbc.gridy++;
        phone2Field = new JTextField(20);
        signUpBox.add(phone2Field, gbc);
        
        gbc.gridy++;
        gbc.fill = GridBagConstraints.BOTH; 
        emailsArea = new JTextArea(5, 20);
        JScrollPane scrollPane = new JScrollPane(emailsArea);
        signUpBox.add(scrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        JButton createButton = new JButton("Create Shared Cart");
        createButton.setBackground(new Color(0, 102, 51));
        createButton.setForeground(Color.WHITE);
        signUpBox.add(createButton, gbc);

        gbc.gridy++;
        JButton backButton = new JButton("Back to Login");
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        backButton.setContentAreaFilled(false);
        backButton.setForeground(Color.BLUE);
        signUpBox.add(backButton, gbc);

        add(signUpBox, new GridBagConstraints());

        // --- Action Listeners ---
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSharedSignUp();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.switchToPanel("LOGIN");
            }
        });
    }

    private void handleSharedSignUp() {
        String cartName = cartNameField.getText();
        String password = new String(passwordField.getPassword());
        String phone1 = phone1Field.getText();
        String phone2 = phone2Field.getText();
        String[] emailLines = emailsArea.getText().split("\\n");
        List<String> memberEmails = new ArrayList<>();
        
        // Clean up email list
        for (String email : emailLines) {
            if (email != null && !email.trim().isEmpty()) {
                memberEmails.add(email.trim());
            }
        }
        
        if (cartName.isEmpty() || password.isEmpty() || phone1.isEmpty() || phone2.isEmpty() || memberEmails.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (mainFrame.sharedCartDatabase.containsKey(cartName)) {
            JOptionPane.showMessageDialog(this, "This Cart Name is already taken.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (String email : memberEmails) {
            if (!mainFrame.userDatabase.containsKey(email)) {
                JOptionPane.showMessageDialog(this, 
                    "Error: User with email '" + email + "' does not have a personal account.\nAll members must create a personal account first.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // All users are valid, create the cart
        SharedCart newCart = new SharedCart(cartName, password, memberEmails, phone1, phone2);
        mainFrame.sharedCartDatabase.put(cartName, newCart);
        
        // --- NEW: Save data to file ---
        mainFrame.saveAllData();
        // --- End of new code ---

        JOptionPane.showMessageDialog(this, "Shared Cart created successfully! You can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
        
        // Clear fields
        cartNameField.setText("");
        passwordField.setText("");
        phone1Field.setText("");
        phone2Field.setText("");
        emailsArea.setText("");

        mainFrame.switchToPanel("LOGIN");
    }
}