package net.regsirius06.engine.core.walls;

import net.regsirius06.engine.API.annotations.ImplementationOf;
import net.regsirius06.engine.API.WallState;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Represents the boundary wall of the labyrinth.
 *
 * <p>The {@code End} class is an implementation of {@link WallState} that defines
 * an impassable barrier at the edge of the labyrinth. It serves as a world boundary,
 * preventing movement beyond its limits.</p>
 *
 * <p>This type of wall is often used to mark the outermost structure of the labyrinth
 * or to designate a restricted area.</p>
 *
 * @see WallState
 */
@ImplementationOf(WallState.class)
public final class End implements WallState {

    /**
     * Indicates whether this wall state emits light.
     *
     * @return {@code false} since the boundary wall does not emit light
     */
    @Override
    public boolean isLight() {
        return false;
    }

    /**
     * Indicates whether this wall state represents an empty space.
     *
     * @return {@code false} since this class represents a solid boundary
     */
    @Override
    public boolean isEmpty() {
        return false;
    }

    /**
     * Indicates whether this wall state can be dynamically generated.
     *
     * @return {@code true} since boundary walls can be placed procedurally
     */
    @Override
    public boolean isGenerable() {
        return true;
    }

    /**
     * Returns the absorption coefficient of this state.
     *
     * <p>This boundary wall absorbs some energy, making it slightly reflective.</p>
     *
     * @return {@code 0.2}, representing a moderate absorption level
     */
    @Override
    public double getAbsorption() {
        return 0.2;
    }

    /**
     * Returns the color representation of this wall.
     *
     * <p>The boundary wall is visually represented as red.</p>
     *
     * @return {@link Color#RED}, indicating a world boundary
     */
    @Override
    public @NotNull Color getColor() {
        return Color.RED;
    }
}
