package com.adventure.game;

import java.util.Map;
import javax.swing.JOptionPane;

public class GameController {

    private final GameUI ui;
    private final GameManager gameManager;

    public GameController(GameUI ui, GameManager gameManager) {
        this.ui = ui;
        this.gameManager = gameManager;

        // Attach listeners to the UI buttons
        this.ui.addLookListener(e -> processAndDisplayCommand("look"));
        this.ui.addInventoryListener(e -> processAndDisplayCommand("inventory"));
    this.ui.addSaveListener(e -> processAndDisplayCommand("save " + gameManager.getPlayer().getName()));
        this.ui.addLoadListener(e -> {
            String defaultName = gameManager.getPlayer().getName();
            String saveName = (String) JOptionPane.showInputDialog(
                    ui,
                    "Load which save?",
                    "Load",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    defaultName
            );
            if (saveName != null && !saveName.trim().isEmpty()) {
                processAndDisplayCommand("load " + saveName.trim());
            }
        });
        this.ui.addExitListener(e -> System.exit(0));

        this.ui.addNorthListener(e -> processAndDisplayCommand("go 北"));
        this.ui.addSouthListener(e -> processAndDisplayCommand("go 南"));
        this.ui.addEastListener(e -> processAndDisplayCommand("go 东"));
        this.ui.addWestListener(e -> processAndDisplayCommand("go 西"));

        this.ui.addTakeListener(e -> {
            String item = JOptionPane.showInputDialog(ui, "What do you want to take?", "Take", JOptionPane.PLAIN_MESSAGE);
            if (item != null && !item.trim().isEmpty()) {
                processAndDisplayCommand("take " + item.trim());
            }
        });

        this.ui.addUseListener(e -> {
            String item = JOptionPane.showInputDialog(ui, "What do you want to use?", "Use", JOptionPane.PLAIN_MESSAGE);
            if (item != null && !item.trim().isEmpty()) {
                processAndDisplayCommand("use " + item.trim());
            }
        });
    }

    public void startGame() {
        ui.appendOutput("Welcome, " + gameManager.getPlayer().getName() + "!");
        ui.appendOutput(gameManager.getStoryLine());
        ui.appendOutput("====================================");
        ui.appendOutput(gameManager.getCurrentRoomDescription());
        refreshDirectionButtons();
    }

    private void processAndDisplayCommand(String command) {
        ui.appendOutput("> " + command);
        String result = gameManager.processCommand(command);
        ui.appendOutput(result);
        if (command.startsWith("load") && !result.contains("失败")) {
            ui.appendOutput(gameManager.getCurrentRoomDescription());
        }
        refreshDirectionButtons();

        // Check for victory condition
        if (gameManager.isGameWon()) {
            ui.appendOutput("\n" + ConfigBasedInitializer.VICTORY_PROMPT);
            JOptionPane.showMessageDialog(ui, ConfigBasedInitializer.VICTORY_PROMPT, "Victory!", JOptionPane.INFORMATION_MESSAGE);
            ui.updateDirectionButtons(false, false, false, false);
        }
    }

    private void refreshDirectionButtons() {
        Map<String, String> exits = gameManager.getAvailableDirections();
        ui.updateDirectionButtons(
                exits.containsKey("北"),
                exits.containsKey("南"),
                exits.containsKey("东"),
                exits.containsKey("西")
        );
    }
}
