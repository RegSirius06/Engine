package net.regsirius06.engine.ui;

import net.regsirius06.engine.labyrinth.Labyrinth2D;
import net.regsirius06.engine.labyrinth.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.KeyEvent;

import static net.regsirius06.engine.labyrinth.LabyrinthGraphics2D.*;

public class KeyLogic {
    private final KeyDefinition definition;

    public KeyLogic(KeyDefinition definition) {
        this.definition = definition;
    }

    public void keyMoveAnalysis(@NotNull KeyEvent e, Player player, Labyrinth2D labyrinth) {
        double step = 0, angle = 0;
        if (e.getKeyCode() == definition.getKey(KeyDefinition.Action.FORWARD_MOVE)) {
            step = moveStep;
        } else if (e.getKeyCode() == definition.getKey(KeyDefinition.Action.BACK_MOVE)) {
            step = -moveStep;
        }
        if (e.getKeyCode() == definition.getKey(KeyDefinition.Action.LEFT_ROTATION)) {
            angle = -rotationStep;
        } else if (e.getKeyCode() == definition.getKey(KeyDefinition.Action.RIGHT_ROTATION)) {
            angle = rotationStep;
        }

        if (step != 0) {
            double newX = player.getX() + Math.cos(player.direction) * step;
            double newY = player.getY() + Math.sin(player.direction) * step;
            if (!labyrinth.isCollidingWithWall(newX, newY)) {
                player.add(step, angle);
            }
        } else if (angle != 0) {
            player.add(step, angle);
        }
    }

    public @Nullable Boolean keyMapAnalysis(@NotNull KeyEvent e, boolean currentState) {
        if (e.getKeyCode() == definition.getKey(KeyDefinition.Action.MINIMAP)) {
            return !currentState;
        } else {
            return null;
        }
    }
}
