package net.regsirius06.engine.gui;

import net.regsirius06.engine.utils.panels.AbstractLabyrinthPanel;
import net.regsirius06.engine.world.World;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a frame for displaying a labyrinth game.
 * This class creates a window that contains a labyrinth panel,
 * displaying a specific labyrinth from the given world.
 */
public class LabyrinthFrame extends MainMenu.Child {

    /**
     * Constructs a new {@code LabyrinthFrame} with the specified dimensions
     * and initializes the frame to display a labyrinth panel.
     *
     * @param world The {@link World} object representing the labyrinth's world,
     *              including the labyrinth to be displayed.
     */
    public LabyrinthFrame(@NotNull World world) {
        super();
        setSize(world.getWidth(), world.getHeight());
        AbstractLabyrinthPanel labyrinth = world.getLabyrinth();

        // Set the window name to the name of the world
        this.setName(world.getName());

        // Add the labyrinth panel to the window's content
        this.getContentPane().add(labyrinth);

        // Set window properties
        setResizable(false);
        setVisible(true);
    }
}
