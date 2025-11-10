package com.adventure.game;

import javax.swing.*;

public class GameApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameUI ui = new GameUI();
            GameManager gameManager = ConfigBasedInitializer.initGame();
            GameController controller = new GameController(ui, gameManager);
            controller.startGame();
            ui.setVisible(true);
        });
    }
}
