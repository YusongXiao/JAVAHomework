package com.adventure.game;

import javax.swing.SwingUtilities;

public class LoginController {

    private final LoginUI loginUI;
    private final UserAuthenticator authenticator;
    private String currentUsername;

    public LoginController(LoginUI loginUI, UserAuthenticator authenticator) {
        this.loginUI = loginUI;
        this.authenticator = authenticator;

        this.loginUI.addLoginListener(e -> handleLogin());
        this.loginUI.addRegisterListener(e -> handleRegister());
    }

    public void showLoginScreen() {
        loginUI.setVisible(true);
    }

    private void handleLogin() {
        String username = loginUI.getUsername();
        String password = loginUI.getPassword();

        if (username.isEmpty()) {
            loginUI.showMessage("Username cannot be empty.");
            return;
        }

        if (!authenticator.isUsernameExists(username)) {
            loginUI.showMessage("User does not exist.");
            return;
        }

        if (authenticator.verifyPassword(username, password)) {
            loginUI.showMessage("Login successful!");
            currentUsername = username;
            startGame();
        } else {
            loginUI.showMessage("Incorrect password.");
        }
    }

    private void handleRegister() {
        String username = loginUI.getUsername();
        String password = loginUI.getPassword();

        if (username.isEmpty() || !username.matches("^[a-zA-Z0-9_]+$")) {
            loginUI.showMessage("Invalid username format.");
            return;
        }
        if (password.length() > 0 && password.length() < 3) {
            loginUI.showMessage("Password must be at least 3 characters long.");
            return;
        }

        if (authenticator.registerUser(username, password)) {
            loginUI.showMessage("Registration successful!");
            currentUsername = username;
            startGame();
        } else {
            loginUI.showMessage("Username already exists or registration failed.");
        }
    }

    private void startGame() {
        // Close login window
        loginUI.dispose();

        // Start the main game
        SwingUtilities.invokeLater(() -> {
            // You can choose which game data to load here.
            // For now, we use the default from ConfigBasedInitializer.
            GameManager gameManager = ConfigBasedInitializer.initGame();
            // You might want to associate the player with the logged-in user
            gameManager.getPlayer().setName(currentUsername);

            GameUI ui = new GameUI();
            GameController controller = new GameController(ui, gameManager);
            controller.startGame();
            ui.setVisible(true);
        });
    }
}
