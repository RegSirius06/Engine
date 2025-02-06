package net.regsirius06.engine.point;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * The {@code ColoredPoint} class extends the {@code Point} class by adding a {@code Color} attribute
 * to represent the point's color. This allows the point to have a visual representation, making it suitable
 * for applications that require both positional and visual data, such as graphical applications or UI components.
 * <p>
 * This class provides methods to access the color of the point, as well as to create a new {@code ColoredPoint}
 * with the same coordinates and color as the current point.
 */
public class ColoredPoint extends Point {
    private final Color color;

    /**
     * Constructs a new {@code ColoredPoint} with the specified {@code x} and {@code y} coordinates and a given {@code Color}.
     *
     * @param x the {@code x} coordinate of the colored point
     * @param y the {@code y} coordinate of the colored point
     * @param color the {@code Color} of the point, represented as a {@code Color} object
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
     * @param color the {@code Color} of the point
     */
    public ColoredPoint(@NotNull Point point, Color color) {
        this(point.getX(), point.getY(), color);
    }

    /**
     * Returns the {@code Color} of this {@code ColoredPoint}.
     *
     * <p>
     * The color is used for visual representation and can be applied in graphical contexts such as rendering
     * the point in a UI component or graphical interface.
     * </p>
     *
     * @return the {@code Color} representing the visual appearance of the point
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns a new {@code ColoredPoint} with the same coordinates and color as this point.
     * This method allows for creating a copy of the current colored point.
     *
     * @return a new {@code ColoredPoint} with the same {@code x}, {@code y}, and color
     */
    public ColoredPoint getColoredPoint() {
        return new ColoredPoint(this, this.color);
    }
}
