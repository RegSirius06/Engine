package net.regsirius06.engine.point;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

/**
 * The {@code LightSource} class represents a light-emitting source in the 2D space.
 * It extends {@code WallType} and is specifically associated with the {@code WallEnum.LIGHT} type.
 * This class allows the creation of light sources with different colors, which can be used in simulations or visual representations.
 */
public class LightSource extends WallType {
    /**
     * A list of predefined colors for light sources.
     * These colors represent different possible light source colors.
     */
    public static final List<Color> COLORS = List.of(
            Color.GREEN, Color.BLUE, Color.MAGENTA,
            Color.ORANGE, Color.YELLOW, Color.CYAN,
            Color.PINK, new Color(128, 0, 255)
    );

    /**
     * Constructs a new {@code LightSource} at the specified {@code x} and {@code y} coordinates with the given color.
     * The type is automatically set to {@code WallEnum.LIGHT}.
     *
     * @param x the {@code x} coordinate of the light source
     * @param y the {@code y} coordinate of the light source
     * @param color the color of the light source
     */
    public LightSource(double x, double y, Color color) {
        super(x, y, WallEnum.LIGHT, color);
    }

    /**
     * Constructs a new {@code LightSource} at the specified {@code Point} location with the given color.
     * The type is automatically set to {@code WallEnum.LIGHT}.
     *
     * @param point the {@code Point} to copy coordinates from
     * @param color the color of the light source
     */
    public LightSource(@NotNull Point point, Color color) {
        this(point.getX(), point.getY(), color);
    }
}
