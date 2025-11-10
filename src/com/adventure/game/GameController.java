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

    this.ui.addDirectionListener(e -> showDirectionChooser());

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
        // No per-button refresh needed now
    }

    private void processAndDisplayCommand(String command) {
        ui.appendOutput("> " + command);
        String result = gameManager.processCommand(command);
        ui.appendOutput(result);
        if (command.startsWith("load") && !result.contains("失败")) {
            ui.appendOutput(gameManager.getCurrentRoomDescription());
        }
    // No direction button state refresh needed

        // Check for victory condition
        if (gameManager.isGameWon()) {
            ui.appendOutput("\n" + ConfigBasedInitializer.VICTORY_PROMPT);
            JOptionPane.showMessageDialog(ui, ConfigBasedInitializer.VICTORY_PROMPT, "Victory!", JOptionPane.INFORMATION_MESSAGE);
            // Inputs could be disabled here if needed
        }
    }

    private void showDirectionChooser() {
        Map<String, String> exits = gameManager.getAvailableDirections();
        if (exits.isEmpty()) {
            ui.appendOutput("当前没有可用的出口。");
            return;
        }
        Object[] options = exits.keySet().toArray();
        Object choice = JOptionPane.showInputDialog(
                null,
                "选择一个方向：",
                "Direction",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );
        if (choice != null) {
            processAndDisplayCommand("go " + choice.toString());
        }
    }
}
