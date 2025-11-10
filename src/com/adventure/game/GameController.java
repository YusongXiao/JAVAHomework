package com.adventure.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameController {

    private GameUI ui;
    private GameManager gameManager;

    public GameController(GameUI ui, GameManager gameManager) {
        this.ui = ui;
        this.gameManager = gameManager;

        this.ui.addSubmitListener(new SubmitListener());
    }

    public void startGame() {
        ui.appendOutput("Welcome to the Adventure Game!");
        ui.appendOutput(gameManager.getCurrentRoomDescription());
    }

    class SubmitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = ui.getInput();
            if (command.trim().isEmpty()) {
                return;
            }

            String result = gameManager.processCommand(command);
            ui.appendOutput("> " + command);
            ui.appendOutput(result);
            ui.clearInput();
        }
    }
}
