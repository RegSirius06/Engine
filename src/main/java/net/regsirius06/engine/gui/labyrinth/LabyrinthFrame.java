package net.regsirius06.engine.gui.labyrinth;

import net.regsirius06.engine.ui.KeyDefinition;

import javax.swing.*;

public class LabyrinthFrame extends JFrame {
    private final LabyrinthPanel labyrinth;

    public LabyrinthFrame(int width, int height) {
        super("Labyrinth");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);
        labyrinth = new LabyrinthPanel(width, height, 15, 5, false, KeyDefinition.DEFAULT);

        this.getContentPane().add(labyrinth);
        setResizable(false);
    }
}