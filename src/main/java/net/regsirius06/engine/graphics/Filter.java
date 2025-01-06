package net.regsirius06.engine.graphics;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * A utility class that provides color filtering operations such as mixing and applying masks (musk).
 * This class contains static methods to combine, apply, or modify colors using various blending or masking techniques.
 * All methods in this class return a new MathColor object.
 */
public final class Filter {

    /**
     * Private constructor to prevent instantiation of this utility class.
     * Utility classes should not be instantiated.
     */
    private Filter() {
    }

    /**
     * Mixes two MathColor objects by averaging each of their color components (red, green, blue).
     * The resulting color is the component-wise average of the two input colors.
     *
     * @param color1 The first MathColor object to mix.
     * @param color2 The second MathColor object to mix.
     * @return A new MathColor object representing the result of mixing the two input colors.
     */
    @Contract("_, _ -> new")
    public static @NotNull MathColor mix(@NotNull MathColor color1, @NotNull MathColor color2) {
        return new MathColor(
                (color1.getR() + color2.getR()) / 2,
                (color1.getG() + color2.getG()) / 2,
                (color1.getB() + color2.getB()) / 2
        );
    }

    /**
     * Mixes two Color objects by first converting them to MathColor and then mixing the resulting MathColor objects.
     * This method allows mixing standard Java Color objects.
     *
     * @param color1 The first Color object to mix.
     * @param color2 The second Color object to mix.
     * @return A new MathColor object representing the result of mixing the two input colors.
     */
    @Contract("_, _ -> new")
    public static @NotNull MathColor mix(Color color1, Color color2) {
        return mix(new MathColor(color1), new MathColor(color2));
    }

    /**
     * Applies a musk (mask) to a MathColor object by taking the component-wise minimum value of the color and the musk.
     * The resulting color has each component (R, G, B) being the minimum of the two input values.
     *
     * @param color The MathColor object to apply the musk to.
     * @param musk  The MathColor object representing the musk (mask).
     * @return A new MathColor object representing the result of applying the musk to the color.
     */
    @Contract("_, _ -> new")
    public static @NotNull MathColor musk(@NotNull MathColor color, @NotNull MathColor musk) {
        return new MathColor(
                Math.min(color.getR(), musk.getR()),
                Math.min(color.getG(), musk.getG()),
                Math.min(color.getB(), musk.getB())
        );
    }

    /**
     * Applies a Color musk (mask) to a MathColor object by first converting the Color to MathColor.
     * This method allows applying a Color mask to a MathColor.
     *
     * @param color The MathColor object to apply the musk to.
     * @param musk  The Color object representing the musk (mask).
     * @return A new MathColor object representing the result of applying the musk to the color.
     */
    @Contract("_, _ -> new")
    public static @NotNull MathColor musk(@NotNull MathColor color, @NotNull Color musk) {
        return musk(color, new MathColor(musk));
    }

    /**
     * Applies a Color musk (mask) to another Color object by first converting both Color objects to MathColor.
     * This method allows applying a Color mask to a Color object.
     *
     * @param color The Color object to apply the musk to.
     * @param musk  The Color object representing the musk (mask).
     * @return A new MathColor object representing the result of applying the musk to the color.
     */
    @Contract("_, _ -> new")
    public static @NotNull MathColor musk(@NotNull Color color, @NotNull Color musk) {
        return musk(new MathColor(color), new MathColor(musk));
    }

    /**
     * Applies a MathColor musk (mask) to a Color object by first converting the Color object to MathColor.
     * This method allows applying a MathColor mask to a Color object.
     *
     * @param color The Color object to apply the musk to.
     * @param musk  The MathColor object representing the musk (mask).
     * @return A new MathColor object representing the result of applying the musk to the color.
     */
    @Contract("_, _ -> new")
    public static @NotNull MathColor musk(@NotNull Color color, @NotNull MathColor musk) {
        return musk(new MathColor(color), musk);
    }

    /**
     * Converts a Color object to a MathColor and applies a transformation to lighten the color.
     * The color is "multiplied" by a negative factor and transformed using a mathematical operation to brighten it.
     *
     * @param color The Color object to transform into a lighter version.
     * @return A new MathColor object representing the lightened version of the input color.
     */
    public static @NotNull MathColor asLight(@NotNull Color color) {
        return asLight(new MathColor(color));
    }

    /**
     * Applies a transformation to a MathColor to lighten it.
     * The lightening process involves multiplying the color by a negative factor and then applying a square root operation.
     *
     * @param color The MathColor object to lighten.
     * @return A new MathColor object representing the lightened version of the input color.
     */
    @Contract("_ -> new")
    public static @NotNull MathColor asLight(@NotNull MathColor color) {
        return color.multiply(-10)
                .apply(x -> Math.sqrt(3 * x * 0.25));
    }
}
