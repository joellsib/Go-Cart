// --- LoginPanel.java ---
import javax.swing.*;
import javax.swing.border.EmptyBorder; // IMPORT
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {

    private MainFrame mainFrame;
    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        
        // 1. Set the main layout to BorderLayout
        setLayout(new BorderLayout());
        setBackground(new Color(232, 233, 234)); // Canvas background

        // --- 2. Add the Header (Copied from ProductPanel) ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 102, 51)); // Dark Green
        headerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("Go Cart");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // --- Navigation Buttons (for login page) ---
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        navPanel.setOpaque(false); // Make transparent

        JButton homeButton = createNavButton("Home");
        JButton productsButton = createNavButton("Products");
        JButton aboutButton = createNavButton("About Us");
        JButton contactButton = createNavButton("Contact");

        navPanel.add(homeButton);
        navPanel.add(productsButton);
        navPanel.add(aboutButton);
        navPanel.add(contactButton);
        
        headerPanel.add(navPanel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH); // Add header to the top

        // --- 3. Create the Center Content Area ---
        // This panel will have the gray background and center the login box
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(232, 233, 234)); // Canvas Background

        // This is the original white login box
        JPanel loginBox = new JPanel();
        loginBox.setLayout(new GridBagLayout());
        loginBox.setBackground(Color.WHITE);
        loginBox.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        
        JLabel loginTitleLabel = new JLabel("Personal User Login");
        loginTitleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        loginBox.add(loginTitleLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        loginBox.add(new JLabel("Email ID:"), gbc);

        gbc.gridy++;
        loginBox.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        emailField = new JTextField(20);
        loginBox.add(emailField, gbc);

        gbc.gridy++;
        passwordField = new JPasswordField(20);
        loginBox.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 102, 51));
        loginButton.setForeground(Color.WHITE);
        loginBox.add(loginButton, gbc);

        gbc.gridy++;
        JButton signUpButton = new JButton("Create New Personal Account");
        signUpButton.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        signUpButton.setContentAreaFilled(false);
        signUpButton.setForeground(Color.BLUE);
        loginBox.add(signUpButton, gbc);
        
        gbc.gridy++;
        JButton sharedCartLoginButton = new JButton("Login to Shared Cart");
        sharedCartLoginButton.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        sharedCartLoginButton.setContentAreaFilled(false);
        sharedCartLoginButton.setForeground(new Color(0, 102, 51));
        loginBox.add(sharedCartLoginButton, gbc);
        
        gbc.gridy++;
        JButton createSharedCartButton = new JButton("Create New Shared Cart");
        createSharedCartButton.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        createSharedCartButton.setContentAreaFilled(false);
        createSharedCartButton.setForeground(Color.BLUE);
        loginBox.add(createSharedCartButton, gbc);
        
        // Add the white loginBox to the gray centerPanel
        centerPanel.add(loginBox, new GridBagConstraints());
        
        // Add the centerPanel to the main layout
        add(centerPanel, BorderLayout.CENTER);

        // --- 4. Add the Footer (Copied from ProductPanel) ---
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(0, 102, 51));
        footerPanel.setPreferredSize(new Dimension(0, 40)); // Dark green footer
        add(footerPanel, BorderLayout.SOUTH); // Add footer to the bottom

        // --- Action Listeners ---
        
        // Login form button actions (unchanged)
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.switchToPanel("SIGNUP");
            }
        });
        
        sharedCartLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.switchToPanel("SHARED_LOGIN");
            }
        });
        
        createSharedCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.switchToPanel("SHARED_SIGNUP");
            }
        });
        
        // Header button actions (show a message, as user is not logged in)
        ActionListener notLoggedInListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainFrame, "Please log in to continue.");
            }
        };
        homeButton.addActionListener(notLoggedInListener);
        productsButton.addActionListener(notLoggedInListener);
        aboutButton.addActionListener(e -> JOptionPane.showMessageDialog(mainFrame, "About Go Cart..."));
        contactButton.addActionListener(e -> JOptionPane.showMessageDialog(mainFrame, "Contact us at support@gocart.com"));
    }

    // Helper to create styled navigation buttons (Copied from ProductPanel)
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

    private void handleLogin() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        User user = mainFrame.userDatabase.get(email);

        if (user != null && user.password.equals(password)) {
            // Success! Use the login method
            mainFrame.userLoggedIn(user); 
            mainFrame.switchToPanel("PRODUCTS");
            emailField.setText("");
            passwordField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}