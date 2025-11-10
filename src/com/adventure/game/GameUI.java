package com.adventure.game;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class GameUI extends JFrame {

    private final JTextArea gameOutput;
    private final JButton lookButton;
    private final JButton takeButton;
    private final JButton useButton;
    private final JButton inventoryButton;
    private final JButton saveButton;
    private final JButton loadButton;
    private final JButton exitButton;
    private final JButton directionButton;

    public GameUI() {
        setTitle("Adventure Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        gameOutput = new JTextArea();
        gameOutput.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(gameOutput);
        add(scrollPane, BorderLayout.CENTER);

    JPanel controlPanel = new JPanel(new BorderLayout());

    JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        lookButton = new JButton("Look");
    directionButton = new JButton("Direction...");
        takeButton = new JButton("Take...");
        useButton = new JButton("Use...");
        inventoryButton = new JButton("Inventory");
        saveButton = new JButton("Save");
        loadButton = new JButton("Load...");
        exitButton = new JButton("Exit");

        actionPanel.add(lookButton);
    actionPanel.add(directionButton);
        actionPanel.add(takeButton);
        actionPanel.add(useButton);
        actionPanel.add(inventoryButton);
        actionPanel.add(saveButton);
        actionPanel.add(loadButton);
        actionPanel.add(exitButton);

        controlPanel.add(actionPanel, BorderLayout.CENTER);

        add(controlPanel, BorderLayout.SOUTH);
    }

    public void appendOutput(String text) {
        gameOutput.append(text + "\n");
    }

    public void addLookListener(ActionListener listener) {
        lookButton.addActionListener(listener);
    }

    public void addDirectionListener(ActionListener listener) {
        directionButton.addActionListener(listener);
    }

    public void addTakeListener(ActionListener listener) {
        takeButton.addActionListener(listener);
    }

    public void addUseListener(ActionListener listener) {
        useButton.addActionListener(listener);
    }

    public void addInventoryListener(ActionListener listener) {
        inventoryButton.addActionListener(listener);
    }

    public void addSaveListener(ActionListener listener) {
        saveButton.addActionListener(listener);
    }

    public void addLoadListener(ActionListener listener) {
        loadButton.addActionListener(listener);
    }

    public void addExitListener(ActionListener listener) {
        exitButton.addActionListener(listener);
    }

    // No dynamic enable/disable needed with Direction chooser

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameUI ui = new GameUI();
            ui.setVisible(true);
            ui.appendOutput("Welcome to the Adventure Game!");
        });
    }
}
