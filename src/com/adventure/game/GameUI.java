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
    private final JButton goNorthButton;
    private final JButton goSouthButton;
    private final JButton goEastButton;
    private final JButton goWestButton;

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

        JPanel directionPanel = new JPanel(new GridLayout(2, 3, 8, 8));
        goNorthButton = new JButton("North (北)");
        goSouthButton = new JButton("South (南)");
        goEastButton = new JButton("East (东)");
        goWestButton = new JButton("West (西)");

        goNorthButton.setEnabled(false);
        goSouthButton.setEnabled(false);
        goEastButton.setEnabled(false);
        goWestButton.setEnabled(false);

        directionPanel.add(new JLabel());
        directionPanel.add(goNorthButton);
        directionPanel.add(new JLabel());
        directionPanel.add(goWestButton);
        directionPanel.add(goSouthButton);
        directionPanel.add(goEastButton);

        controlPanel.add(directionPanel, BorderLayout.NORTH);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        lookButton = new JButton("Look");
        takeButton = new JButton("Take...");
        useButton = new JButton("Use...");
        inventoryButton = new JButton("Inventory");
        saveButton = new JButton("Save");
        loadButton = new JButton("Load...");
        exitButton = new JButton("Exit");

        actionPanel.add(lookButton);
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

    public void addNorthListener(ActionListener listener) {
        goNorthButton.addActionListener(listener);
    }

    public void addSouthListener(ActionListener listener) {
        goSouthButton.addActionListener(listener);
    }

    public void addEastListener(ActionListener listener) {
        goEastButton.addActionListener(listener);
    }

    public void addWestListener(ActionListener listener) {
        goWestButton.addActionListener(listener);
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

    public void updateDirectionButtons(boolean north, boolean south, boolean east, boolean west) {
        goNorthButton.setEnabled(north);
        goSouthButton.setEnabled(south);
        goEastButton.setEnabled(east);
        goWestButton.setEnabled(west);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameUI ui = new GameUI();
            ui.setVisible(true);
            ui.appendOutput("Welcome to the Adventure Game!");
        });
    }
}
