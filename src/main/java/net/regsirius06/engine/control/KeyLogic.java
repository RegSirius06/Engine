package net.regsirius06.engine.control;

import net.regsirius06.engine.core.graphics.labyrinth.default2D.Labyrinth2D;
import net.regsirius06.engine.point.entities.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.KeyEvent;

import static net.regsirius06.engine.core.graphics.labyrinth.default2D.LabyrinthGraphics2D.moveStep;
import static net.regsirius06.engine.core.graphics.labyrinth.default2D.LabyrinthGraphics2D.rotationStep;

/**
 * Handles the logic for interpreting key inputs and performing actions based on key bindings.
 * This class is responsible for analyzing key events related to player movement and interaction with the labyrinth.
 * It processes key presses to move the player, rotate the player, and toggle the minimap display.
 * <p>
 * The class uses a {@link KeyDefinition} instance to map actions to specific key codes and interprets key events accordingly.
 * </p>
 *
 * @deprecated This class is deprecated. Consider using an alternative event handling system.
 */
@Deprecated
public class KeyLogic {
    private final KeyDefinition definition;

    /**
     * Constructs a new {@link KeyLogic} instance with the given key definition.
     * This constructor initializes the key logic with a set of key bindings that will be used to handle player movements and other actions.
     *
     * @param definition the {@link KeyDefinition} containing the key bindings for various actions, such as movement and minimap toggle
     */
    public KeyLogic(KeyDefinition definition) {
        this.definition = definition;
    }

    /**
     * Analyzes a key press event and processes the corresponding movement or rotation of the player.
     * Based on the key event, this method modifies the player's position and orientation within the labyrinth.
     * <p>
     * If the key press corresponds to a movement action, the player's position is updated accordingly.
     * If the key press corresponds to a rotation action, the player's orientation is updated.
     * The method also ensures that the player does not move through walls by checking for collisions within the labyrinth.
     *
     * @param e the {@link KeyEvent} representing the key press event
     * @param player the {@link Player} object whose position and direction are updated
     * @param labyrinth the {@link Labyrinth2D} object used to check for collisions with walls
     */
    public void keyMoveAnalysis(@NotNull KeyEvent e, Player player, Labyrinth2D labyrinth) {
        double step = 0, angle = 0;

        // Forward and backward movement
        if (e.getKeyCode() == definition.getKey(KeyDefinition.Action.FORWARD_MOVE)) {
            step = moveStep;
        } else if (e.getKeyCode() == definition.getKey(KeyDefinition.Action.BACK_MOVE)) {
            step = -moveStep;
        }

        // Left and right rotation
        if (e.getKeyCode() == definition.getKey(KeyDefinition.Action.LEFT_ROTATION)) {
            angle = -rotationStep;
        } else if (e.getKeyCode() == definition.getKey(KeyDefinition.Action.RIGHT_ROTATION)) {
            angle = rotationStep;
        }

        // Apply movement if no rotation, or rotation if no movement
        if (step != 0) {
            double newX = player.getX() + Math.cos(player.getDirection()) * step;
            double newY = player.getY() + Math.sin(player.getDirection()) * step;

            // Check for collision before applying movement
            if (!labyrinth.isCollidingWithWall(newX, newY)) {
                player.add(step, angle);
            }
        } else if (angle != 0) {
            player.add(step, angle);
        }
    }

    /**
     * Analyzes a key press event to toggle the minimap display.
     * This method checks if the key press corresponds to the minimap toggle action and returns the updated state.
     * The minimap state is toggled from visible to hidden or vice versa based on the key press.
     *
     * @param e the {@link KeyEvent} representing the key press event
     * @param currentState the current state of the minimap (true for visible, false for hidden)
     * @return the new state of the minimap (true if it should be shown, false if hidden),
     *         or {@code null} if the key press does not correspond to the minimap toggle
     */
    public @Nullable Boolean keyMapAnalysis(@NotNull KeyEvent e, boolean currentState) {
        if (e.getKeyCode() == definition.getKey(KeyDefinition.Action.MINIMAP)) {
            return !currentState;  // Toggle the minimap state
        } else {
            return null;  // No change in state
        }
    }
}
