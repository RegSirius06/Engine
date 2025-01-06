package net.regsirius06.engine.point;

import java.awt.*;

/**
 * The {@code WallEnum} enumeration defines different types of walls, each with a specific type identifier,
 * color, and absorption coefficient. This enum provides utility for representing walls in a 2D space
 * with physical and visual properties.
 */
public enum WallEnum {
    /**
     * Represents an empty wall, with type 0, black color, and no absorption.
     */
    EMPTY(0, Color.BLACK, 0),

    /**
     * Represents a light-emitting wall, with type 0, white color, and no absorption.
     */
    LIGHT(0, Color.WHITE, 0),

    /**
     * Represents a default wall, with type 1, gray color, and an absorption coefficient of 1.
     */
    DEFAULT(1, Color.GRAY, 1),

    /**
     * Represents an end wall, with type 2, red color, and an absorption coefficient of 0.2.
     */
    END(2, Color.RED, 0.2);

    /**
     * The type identifier of the wall.
     */
    public final int type;

    /**
     * The color of the wall, represented as a {@code Color} object.
     */
    public final Color color;

    /**
     * The absorption coefficient of the wall, indicating how much light or energy the wall absorbs.
     */
    public final double absorption;

    /**
     * Constructs a {@code WallEnum} constant with the specified type, color, and absorption coefficient.
     *
     * @param type the type identifier of the wall
     * @param color the color of the wall
     * @param absorption the absorption coefficient of the wall
     */
    WallEnum(int type, Color color, double absorption) {
        this.type = type;
        this.color = color;
        this.absorption = absorption;
    }

    /**
     * Checks if this wall type is not empty.
     *
     * @return {@code true} if the wall type is not {@code EMPTY}, {@code false} otherwise
     */
    public boolean notEmpty() {
        return this.type != EMPTY.type;
    }
}
