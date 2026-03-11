// --- SharedLoginPanel.java ---
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// This is a new panel for logging into a shared cart
public class SharedLoginPanel extends JPanel {

    private MainFrame mainFrame;
    private JTextField emailField; // Your *personal* email
    private JTextField cartNameField; // The shared cart's name
    private JPasswordField passwordField; // The shared cart's password

    public SharedLoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridBagLayout());
        setBackground(new Color(232, 233, 234));

        JPanel loginBox = new JPanel(new GridBagLayout());
        loginBox.setBackground(Color.WHITE);
        loginBox.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        
        JLabel titleLabel = new JLabel("Shared Cart Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        loginBox.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        
        gbc.gridy++;
        loginBox.add(new JLabel("Your Personal Email:"), gbc);
        gbc.gridy++;
        loginBox.add(new JLabel("Shared Cart Name:"), gbc);
        gbc.gridy++;
        loginBox.add(new JLabel("Shared Cart Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        emailField = new JTextField(20);
        loginBox.add(emailField, gbc);
        gbc.gridy++;
        cartNameField = new JTextField(20);
        loginBox.add(cartNameField, gbc);
        gbc.gridy++;
        passwordField = new JPasswordField(20);
        loginBox.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        JButton loginButton = new JButton("Login to Shared Cart");
        loginButton.setBackground(new Color(0, 102, 51));
        loginButton.setForeground(Color.WHITE);
        loginBox.add(loginButton, gbc);

        gbc.gridy++;
        JButton backButton = new JButton("Back to Main Login");
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        backButton.setContentAreaFilled(false);
        backButton.setForeground(Color.BLUE);
        loginBox.add(backButton, gbc);

        add(loginBox, new GridBagConstraints());

        // --- Action Listeners ---
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSharedLogin();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.switchToPanel("LOGIN");
            }
        });
    }

    private void handleSharedLogin() {
        String email = emailField.getText();
        String cartName = cartNameField.getText();
        String password = new String(passwordField.getPassword());

        // 1. Check if the personal user exists
        if (!mainFrame.userDatabase.containsKey(email)) {
            JOptionPane.showMessageDialog(this, "Personal email account not found.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Check if the shared cart exists
        SharedCart cart = mainFrame.sharedCartDatabase.get(cartName);
        if (cart == null) {
            JOptionPane.showMessageDialog(this, "Shared Cart not found.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. Check if the password matches
        if (!cart.password.equals(password)) {
            JOptionPane.showMessageDialog(this, "Invalid Shared Cart password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 4. Check if this user is a member of that cart
        if (!cart.memberEmails.contains(email)) {
            JOptionPane.showMessageDialog(this, "You are not a member of this shared cart.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // --- SUCCESS ---
        User user = mainFrame.userDatabase.get(email);
        mainFrame.sharedUserLoggedIn(user, cart); // Use the new login method
        
        // Clear fields
        emailField.setText("");
        cartNameField.setText("");
        passwordField.setText("");
        
        mainFrame.switchToPanel("PRODUCTS");
    }
}
