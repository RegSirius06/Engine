package net.regsirius06.engine.point;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * The {@code WallType} class extends the {@code ColoredPoint} class and adds the concept of a wall type
 * represented by the {@code WallEnum}. Each wall can have a type, color, and additional properties based
 * on its type, such as absorption. This class provides methods to access the wall's type and check whether
 * the wall is empty.
 */
public class WallType extends ColoredPoint {
    /**
     * The {@code WallEnum} type associated with this wall.
     */
    protected final WallEnum wall;

    /**
     * Constructs a new {@code WallType} with the specified {@code x} and {@code y} coordinates, a {@code WallEnum} type,
     * and a color for the wall.
     *
     * @param x the {@code x} coordinate of the wall
     * @param y the {@code y} coordinate of the wall
     * @param wallEnum the wall type represented as a {@code WallEnum}
     * @param color the color of the wall
     */
    protected WallType(double x, double y, @NotNull WallEnum wallEnum, Color color) {
        super(x, y, color);
        this.wall = wallEnum;
    }

    /**
     * Constructs a new {@code WallType} with the specified {@code x} and {@code y} coordinates and a {@code WallEnum} type.
     * The color is automatically set based on the {@code WallEnum}.
     *
     * @param x the {@code x} coordinate of the wall
     * @param y the {@code y} coordinate of the wall
     * @param wallEnum the wall type represented as a {@code WallEnum}
     */
    public WallType(double x, double y, @NotNull WallEnum wallEnum) {
        super(x, y, wallEnum.color);
        this.wall = wallEnum;
    }

    /**
     * Constructs a new {@code WallType} by copying the coordinates of an existing {@code Point} and setting the wall type
     * and color based on the {@code WallEnum}.
     *
     * @param point the {@code Point} to copy coordinates from
     * @param wallEnum the wall type represented as a {@code WallEnum}
     */
    public WallType(@NotNull Point point, @NotNull WallEnum wallEnum) {
        super(point, wallEnum.color);
        this.wall = wallEnum;
    }

    /**
     * Returns the {@code WallEnum} type associated with this wall.
     *
     * @return the {@code WallEnum} type of the wall
     */
    public WallEnum getWall() {
        return wall;
    }

    /**
     * Checks if the wall is not empty by calling the {@code notEmpty} method on the associated {@code WallEnum}.
     *
     * @return {@code true} if the wall is not empty (i.e., its type is not {@code EMPTY}), {@code false} otherwise
     */
    public boolean notEmpty() {
        return this.wall.notEmpty();
    }
}
