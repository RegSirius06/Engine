package net.regsirius06.engine.core.walls;

import net.regsirius06.engine.API.annotations.ImplementationOf;
import net.regsirius06.engine.API.WallState;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Represents an empty space in the labyrinth.
 *
 * <p>The {@code Empty} class is an implementation of {@link WallState} that defines
 * a non-obstructive, passable area. It is the opposite of a solid wall, meaning
 * that it does not block movement or absorb energy.</p>
 *
 * <p>This class is useful for defining pathways, open spaces, or areas where
 * players and entities can move freely.</p>
 *
 * @see WallState
 */
@ImplementationOf(WallState.class)
public final class Empty implements WallState {

    /**
     * Indicates whether this state represents a light source.
     *
     * @return {@code false} as an empty space does not emit light by default
     */
    @Override
    public boolean isLight() {
        return false;
    }

    /**
     * Indicates whether this state represents an empty space.
     *
     * @return {@code true} since this class defines an empty, passable area
     */
    @Override
    public boolean isEmpty() {
        return true;
    }

    /**
     * Indicates whether this state can be dynamically generated.
     *
     * @return {@code true} since empty spaces can be generated in labyrinths
     */
    @Override
    public boolean isGenerable() {
        return true;
    }

    /**
     * Returns the absorption coefficient of this state.
     *
     * <p>Since this is an empty space, it does not absorb any energy.</p>
     *
     * @return {@code 0}, meaning no absorption
     */
    @Override
    public double getAbsorption() {
        return 0;
    }

    /**
     * Returns the color representation of this state.
     *
     * <p>The empty space is represented as black.</p>
     *
     * @return {@link Color#BLACK}, representing an empty void
     */
    @Override
    public @NotNull Color getColor() {
        return Color.BLACK;
    }
}
