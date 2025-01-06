package net.regsirius06.engine.ui;

import net.regsirius06.engine.labyrinth.Labyrinth2D;
import net.regsirius06.engine.point.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.KeyEvent;

import static net.regsirius06.engine.graphics.LabyrinthGraphics2D.*;

/**
 * Handles the logic for interpreting key inputs and performing actions based on key bindings.
 * This class is responsible for analyzing key events related to player movement and interaction with the labyrinth.
 */
public class KeyLogic {
    private final KeyDefinition definition;

    /**
     * Constructs a new {@link KeyLogic} instance with the given key definition.
     *
     * @param definition the {@link KeyDefinition} containing the key bindings for various actions
     */
    public KeyLogic(KeyDefinition definition) {
        this.definition = definition;
    }

    /**
     * Analyzes a key press event and processes the corresponding movement or rotation of the player.
     * Based on the key event, this method modifies the player's position and orientation within the labyrinth.
     *
     * @param e the {@link KeyEvent} representing the key press event
     * @param player the {@link Player} object whose position is updated
     * @param labyrinth the {@link Labyrinth2D} object used to check for collisions
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
     * This method returns the updated state of the minimap, toggling it based on the key press.
     *
     * @param e the {@link KeyEvent} representing the key press event
     * @param currentState the current state of the minimap (true for visible, false for hidden)
     * @return the new state of the minimap (true if it should be shown, false if hidden)
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
