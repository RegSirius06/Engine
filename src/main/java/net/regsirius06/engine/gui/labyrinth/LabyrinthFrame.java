package net.regsirius06.engine.gui.labyrinth;

import net.regsirius06.engine.ui.KeyDefinition;
import net.regsirius06.engine.ui.KeyLogic;

import javax.swing.*;

/**
 * Represents a frame for displaying a labyrinth game.
 */
public class LabyrinthFrame extends JFrame {
    private final LabyrinthPanel labyrinth;

    /**
     * Constructs a new LabyrinthFrame with the specified dimensions.
     * This constructor initializes the frame, sets up the labyrinth panel,
     * and configures basic window properties.
     *
     * @param width  The width of the frame in pixels.
     * @param height The height of the frame in pixels.
     */
    public LabyrinthFrame(int width, int height) {
        super("Labyrinth");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);
        labyrinth = new LabyrinthPanel(width, height, 15, 5, false, new KeyLogic(KeyDefinition.DEFAULT));

        this.getContentPane().add(labyrinth);
        setResizable(false);
    }
}