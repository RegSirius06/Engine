package net.regsirius06.engine.gui.labyrinth;

import javax.swing.*;

public class LabyrinthFrame extends JFrame {
    private final LabyrinthPanel labyrinth;

    public LabyrinthFrame() {
        super("Labyrinth");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(LabyrinthPanel.WIDTH, LabyrinthPanel.HEIGHT);
        labyrinth = new LabyrinthPanel(15, 5, false);

        this.getContentPane().add(labyrinth);
        setResizable(false);
    }
}