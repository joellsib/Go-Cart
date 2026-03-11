// --- SignUpPanel.java ---
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignUpPanel extends JPanel {
    private MainFrame mainFrame;
    private JTextField usernameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JPasswordField passwordField;

    public SignUpPanel(MainFrame mainFrame) {
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
        
        JLabel titleLabel = new JLabel("Create Personal Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        signUpBox.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        
        gbc.gridy++;
        signUpBox.add(new JLabel("Username:"), gbc);
        gbc.gridy++;
        signUpBox.add(new JLabel("Email ID:"), gbc);
        gbc.gridy++;
        signUpBox.add(new JLabel("Phone No:"), gbc);
        gbc.gridy++;
        signUpBox.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        usernameField = new JTextField(20);
        signUpBox.add(usernameField, gbc);
        gbc.gridy++;
        emailField = new JTextField(20);
        signUpBox.add(emailField, gbc);
        gbc.gridy++;
        phoneField = new JTextField(20);
        signUpBox.add(phoneField, gbc);
        gbc.gridy++;
        passwordField = new JPasswordField(20);
        signUpBox.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        JButton createButton = new JButton("Create Account");
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
                handleSignUp();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.switchToPanel("LOGIN");
            }
        });
    }

    private void handleSignUp() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Sign Up Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (mainFrame.userDatabase.containsKey(email)) {
            JOptionPane.showMessageDialog(this, "This email is already registered.", "Sign Up Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Add new user to mock database
        User newUser = new User(username, email, phone, password);
        mainFrame.userDatabase.put(email, newUser);
        
        // --- NEW: Save data to file ---
        mainFrame.saveAllData();
        // --- End of new code ---

        JOptionPane.showMessageDialog(this, "Account created successfully! Please log in.", "Sign Up Success", JOptionPane.INFORMATION_MESSAGE);
        
        // Clear fields
        usernameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        passwordField.setText("");

        // Go back to login
        mainFrame.switchToPanel("LOGIN");
    }
}