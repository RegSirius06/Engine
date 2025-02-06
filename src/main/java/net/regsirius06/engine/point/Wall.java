package net.regsirius06.engine.point;

import net.regsirius06.engine.API.WallState;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * The {@link Wall} class extends the {@link ColoredPoint} class and adds a {@link WallState} attribute
 * to represent the state of a wall (such as whether it's passable, blocked, or any other custom state).
 * This class is particularly useful for representing walls in a maze or grid-based environment,
 * where each wall can have a position, color, and state.
 * <p>
 * The state of the wall is represented by the {@link WallState} enum, which provides different states
 * that can be associated with a wall (e.g., passable, blocked, etc.).
 * <p>
 * This class is most commonly used to represent a maze cell's wall in a maze generation or maze-solving algorithm.
 */
public class Wall extends ColoredPoint {
    private final WallState state;

    /**
     * Constructs a new {@code Wall} with the specified {@code x} and {@code y} coordinates,
     * and a given {@code WallState} to represent the wall's current state. The color of the wall is
     * determined by the {@code WallState}.
     *
     * @param x     the {@code x} coordinate of the wall
     * @param y     the {@code y} coordinate of the wall
     * @param state the {@code WallState} representing the state of the wall
     */
    public Wall(double x, double y, @NotNull WallState state) {
        super(x, y, state.getColor());
        this.state = state;
    }

    /**
     * Constructs a new {@code Wall} by copying the coordinates of an existing {@code Point}
     * and setting the {@code WallState} to the specified state. The color of the wall is determined by the state.
     *
     * @param point the {@code Point} to copy coordinates from
     * @param state the {@code WallState} representing the state of the wall
     */
    public Wall(@NotNull Point point, @NotNull WallState state) {
        this(point.getX(), point.getY(), state);
    }

    /**
     * Returns the {@code WallState} of this wall.
     *
     * @return the {@code WallState} representing the current state of the wall
     */
    public WallState getState() {
        return state;
    }
}
