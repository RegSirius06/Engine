package net.regsirius06.engine.utils.panels;

import net.regsirius06.engine.control.KeyLogic;
import net.regsirius06.engine.control.UIKeyListener;

import java.awt.event.KeyEvent;

/**
 * An abstract class that extends {@link AbstractPanel} and implements the {@link UIKeyListener} interface.
 * This class serves as a foundation for panels that represent labyrinths and handle keyboard events.
 * <p>
 * It includes functionality for key press events, inherited from {@link UIKeyListener}, as well as providing
 * a seed value for randomization. The {@link #keyPressed(KeyEvent)} method must be implemented by subclasses
 * to handle key events specific to the labyrinth panel.
 * </p>
 */
public abstract class AbstractLabyrinthPanel extends AbstractPanel implements UIKeyListener {

    /**
     * Constructs an {@link AbstractLabyrinthPanel} with the specified parameters.
     *
     * @param width the width of the panel
     * @param height the height of the panel
     * @param dimension the dimension of the labyrinth
     * @param quantityOfLights the number of lights in the labyrinth
     * @param seed the seed used for randomization
     * @param keyLogic the {@link KeyLogic} instance to handle key events
     */
    public AbstractLabyrinthPanel(int width, int height, int dimension,
                                  int quantityOfLights, long seed, KeyLogic keyLogic) {
    }
}
