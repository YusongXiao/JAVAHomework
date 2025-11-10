package com.adventure.game;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class GameUI extends JFrame {

    private final JTextArea gameOutput;
    private final JTextField commandInput;
    private final JButton submitButton;

    public GameUI() {
        setTitle("Adventure Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        gameOutput = new JTextArea();
        gameOutput.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(gameOutput);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        commandInput = new JTextField();
        submitButton = new JButton("Submit");

        inputPanel.add(commandInput, BorderLayout.CENTER);
        inputPanel.add(submitButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        // We will add action listeners later
    }

    public void appendOutput(String text) {
        gameOutput.append(text + "\n");
    }

    public String getInput() {
        return commandInput.getText();
    }

    public void clearInput() {
        commandInput.setText("");
    }

    public void addSubmitListener(ActionListener listener) {
        submitButton.addActionListener(listener);
        commandInput.addActionListener(listener); // Allow pressing Enter in the text field
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameUI ui = new GameUI();
            ui.setVisible(true);
            ui.appendOutput("Welcome to the Adventure Game!");
        });
    }
}
