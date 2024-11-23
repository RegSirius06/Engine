package net.regsirius06.engine;

import net.regsirius06.engine.gui.labyrinth.LabyrinthFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LabyrinthFrame().setVisible(true));
    }
}