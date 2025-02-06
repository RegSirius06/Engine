package net.regsirius06.engine.core.initializers;

import net.regsirius06.engine.API.Initializer;
import net.regsirius06.engine.API.annotations.ImplementationOf;
import net.regsirius06.engine.control.KeyLogic;
import net.regsirius06.engine.core.panels.LabyrinthPanel;
import org.jetbrains.annotations.NotNull;

/**
 * {@code LabyrinthPanelInit} is an implementation of the {@link Initializer} class for creating instances of
 * {@link LabyrinthPanel}. It is responsible for the creation of a labyrinth panel by initializing it with
 * specific parameters such as width, height, dimension, number of lights, seed, and key logic.
 *
 * <p>This initializer provides the mechanism to create a labyrinth panel with customizable properties. It is used
 * as part of a plugin or modular system to allow different configurations of labyrinths within the application.
 * It is designed to throw an {@link IllegalArgumentException} if the provided arguments are incorrect or insufficient
 * to create the {@link LabyrinthPanel}.</p>
 *
 * @see Initializer
 * @see LabyrinthPanel
 * @see KeyLogic
 */
@ImplementationOf(Initializer.class)
public class LabyrinthPanelInit extends Initializer<LabyrinthPanel> {

    /**
     * Returns the name of this initializer.
     *
     * <p>The name is used to identify the initializer in the system. In this case, the name "Default" represents
     * the standard initializer for a labyrinth panel.</p>
     *
     * @return the name of this initializer, "Default".
     */
    @Override
    public String getName() {
        return "Default";
    }

    /**
     * Creates a new instance of {@link LabyrinthPanel} with the provided parameters.
     *
     * <p>The {@code create} method takes in exactly six parameters: width, height, dimension, quantity of lights,
     * seed for randomization, and the key logic handler. These values are used to configure the labyrinth panel.
     * The method checks the number and types of arguments to ensure they are correct. If there is any discrepancy,
     * an {@link IllegalArgumentException} will be thrown.</p>
     *
     * @param objects an array of objects containing the necessary arguments:
     *                width, height, dimension, number of lights, seed, and key logic.
     *                The arguments must be passed in the exact order:
     *                (width, height, dimension, lights, seed, keyLogic).
     * @return a newly created {@link LabyrinthPanel} object configured with the provided arguments.
     * @throws IllegalArgumentException if the arguments are invalid, insufficient, or not of the correct types.
     */
    @Override
    public LabyrinthPanel create(Object @NotNull ... objects) throws IllegalArgumentException {
        // Ensure that exactly 6 arguments are passed
        if (objects.length != 6) {
            throw new IllegalArgumentException("6 arguments must be passed");
        }

        try {
            // Extract the arguments and cast them to the expected types
            int width = (int) objects[0];
            int height = (int) objects[1];
            int dimension = (int) objects[2];
            int lights = (int) objects[3];
            long seed = (long) objects[4];
            KeyLogic keyLogic = (KeyLogic) objects[5];

            // Return a new instance of LabyrinthPanel with the specified parameters
            return new LabyrinthPanel(width, height, dimension, lights, seed, keyLogic);

        } catch (ClassCastException | ArrayIndexOutOfBoundsException e) {
            // If there's a mismatch in argument types or quantity, throw an exception
            throw new IllegalArgumentException("Invalid argument types for LabyrinthPanel creation", e);
        }
    }
}
