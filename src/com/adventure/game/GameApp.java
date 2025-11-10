package com.adventure.game;

import javax.swing.*;

public class GameApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginUI loginUI = new LoginUI();
            UserAuthenticator authenticator = new UserAuthenticator();
            LoginController loginController = new LoginController(loginUI, authenticator);
            loginController.showLoginScreen();
        });
    }
}
