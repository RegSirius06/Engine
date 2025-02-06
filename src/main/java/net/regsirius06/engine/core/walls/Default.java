package net.regsirius06.engine.core.walls;

import net.regsirius06.engine.API.annotations.ImplementationOf;
import net.regsirius06.engine.API.WallState;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Represents the default wall state in the labyrinth.
 *
 * <p>This class implements the {@link WallState} interface and defines a standard,
 * non-light, non-empty wall with a moderate absorption coefficient.
 * It serves as the default implementation for walls in the labyrinth system.</p>
 *
 * <p>The default wall is generable, meaning it can be dynamically created within the labyrinth.</p>
 *
 * @see WallState
 */
@ImplementationOf(WallState.class)
public final class Default implements WallState {
    /**
     * Determines if the wall allows light to pass through.
     *
     * @return {@code false} since the default wall is opaque.
     */
    @Override
    public boolean isLight() {
        return false;
    }

    /**
     * Determines if the wall is empty (i.e., non-existent).
     *
     * @return {@code false} since this represents a solid wall.
     */
    @Override
    public boolean isEmpty() {
        return false;
    }

    /**
     * Determines if the wall can be generated dynamically.
     *
     * @return {@code true} since this wall type can be created in the labyrinth.
     */
    @Override
    public boolean isGenerable() {
        return true;
    }

    /**
     * Returns the absorption coefficient of the wall.
     *
     * <p>This value represents how much energy the wall absorbs when interacting
     * with external forces (e.g., sound, light, or other mechanics).</p>
     *
     * @return the absorption coefficient, which is {@code 0.2} for the default wall.
     */
    @Override
    public double getAbsorption() {
        return 0.2;
    }

    /**
     * Returns the color of the wall.
     *
     * @return {@link Color#GRAY}, representing the default wall color.
     */
    @Override
    public @NotNull Color getColor() {
        return Color.GRAY;
    }
}
