package net.regsirius06.engine.core.walls;

import net.regsirius06.engine.API.annotations.ImplementationOf;
import net.regsirius06.engine.API.WallState;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.Random;

/**
 * Represents a light-emitting source in the labyrinth.
 *
 * <p>The {@code Light} class is an implementation of {@link WallState} that defines
 * a space that emits light and has no physical barriers.</p>
 *
 * <p>Light sources are visually represented with randomized colors chosen from a predefined set.</p>
 *
 * @see WallState
 */
@ImplementationOf(WallState.class)
public final class Light implements WallState {

    /**
     * A list of predefined colors for light sources.
     * These colors represent different possible light source colors.
     */
    public static final List<Color> COLORS = List.of(
            Color.GREEN, Color.BLUE, Color.MAGENTA,
            Color.ORANGE, Color.YELLOW, Color.CYAN,
            Color.PINK, new Color(128, 0, 255)
    );

    private static final Random colorRandom = new Random();
    private final Color color;

    /**
     * Constructs a new {@code Light} instance with a randomly chosen color.
     */
    public Light() {
        this.color = getRandomColor();
    }

    /**
     * Indicates whether this wall state emits light.
     *
     * @return {@code true} since this class represents a light-emitting source
     */
    @Override
    public boolean isLight() {
        return true;
    }

    /**
     * Indicates whether this wall state represents an empty space.
     *
     * @return {@code true} since light sources do not obstruct movement
     */
    @Override
    public boolean isEmpty() {
        return true;
    }

    /**
     * Indicates whether this wall state can be dynamically generated.
     *
     * @return {@code true} since light sources can be placed procedurally
     */
    @Override
    public boolean isGenerable() {
        return true;
    }

    /**
     * Returns the absorption coefficient of this state.
     *
     * <p>A negative absorption value indicates that the light source actively emits energy
     * instead of absorbing it.</p>
     *
     * @return {@code -10}, representing a strong light-emitting property
     */
    @Override
    public double getAbsorption() {
        return -10;
    }

    /**
     * Returns the color representation of this light source.
     *
     * @return a randomly selected color from {@link #COLORS}
     */
    @Override
    public @NotNull Color getColor() {
        return this.color;
    }

    /**
     * Returns a random color from the predefined list of light source colors.
     *
     * @return a randomly chosen {@link Color}
     */
    public static @NotNull Color getRandomColor() {
        return COLORS.get(colorRandom.nextInt(COLORS.size()));
    }
}
