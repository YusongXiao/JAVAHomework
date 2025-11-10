package com.adventure.game;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class LoginUI extends JFrame {

    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton loginButton;
    private final JButton registerButton;
    private final JLabel messageLabel;

    public LoginUI() {
        setTitle("Login or Register");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(null); // Center the window

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        usernameField = new JTextField(20);
        add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        passwordField = new JPasswordField(20);
        add(passwordField, gbc);

        // Buttons
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        loginButton = new JButton("Login");
        add(loginButton, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        registerButton = new JButton("Register");
        add(registerButton, gbc);

        // Message Label
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setForeground(Color.RED);
        add(messageLabel, gbc);
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public void addLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public void addRegisterListener(ActionListener listener) {
        registerButton.addActionListener(listener);
    }

    public void showMessage(String message) {
        messageLabel.setText(message);
    }
}
