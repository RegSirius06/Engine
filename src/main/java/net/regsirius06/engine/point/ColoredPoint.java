package net.regsirius06.engine.point;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * The {@code ColoredPoint} class extends the {@code Point} class and adds a color attribute
 * to the point. This class allows the point to have a visual representation through a {@code Color} object.
 * It provides methods to access the color and create a new colored point with the same coordinates and color.
 */
public class ColoredPoint extends Point {
    protected final Color color;

    /**
     * Constructs a new {@code ColoredPoint} with the specified {@code x} and {@code y} coordinates and a given {@code Color}.
     *
     * @param x the {@code x} coordinate of the colored point
     * @param y the {@code y} coordinate of the colored point
     * @param color the color of the point, represented as a {@code Color} object
     */
    public ColoredPoint(double x, double y, Color color) {
        super(x, y);
        this.color = color;
    }

    /**
     * Constructs a new {@code ColoredPoint} by copying the coordinates of an existing {@code Point}
     * and setting the color to the specified {@code Color}.
     *
     * @param point the {@code Point} to copy coordinates from
     * @param color the color of the point, represented as a {@code Color} object
     */
    public ColoredPoint(@NotNull Point point, Color color) {
        this(point.getX(), point.getY(), color);
    }

    /**
     * Returns the {@code Color} of this colored point.
     *
     * @return the {@code Color} of the point
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns a new {@code ColoredPoint} with the same coordinates and color as this point.
     *
     * @return a new {@code ColoredPoint} with the same {@code x}, {@code y}, and color as this point
     */
    public ColoredPoint getColoredPoint() {
        return new ColoredPoint(this, this.color);
    }
}
