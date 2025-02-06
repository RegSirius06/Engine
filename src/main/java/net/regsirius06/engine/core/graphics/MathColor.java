package net.regsirius06.engine.core.graphics;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.function.Function;

/**
 * Represents a color using double precision floating-point values for the red, green, and blue components.
 * This class provides methods for manipulating colors, including multiplication with other colors, clamping,
 * and conversion to/from the standard Java Color format.
 * <p>
 * It is useful for precise color manipulation in graphics, particularly in scenarios where accurate representation
 * of color blending, fading, and inversion is needed, such as in rendering or visualization tasks.
 */
public final class MathColor {
    private final double r; // Red component (normalized to 0.0-1.0)
    private final double g; // Green component (normalized to 0.0-1.0)
    private final double b; // Blue component (normalized to 0.0-1.0)

    /**
     * Constructs a MathColor object from a java.awt.Color object.
     * The RGB values from the Color object are normalized to a range of 0.0 to 1.0.
     *
     * @param color The Color object to convert to MathColor.
     */
    public MathColor(@NotNull Color color) {
        this(color.getRed(), color.getGreen(), color.getBlue());
    }

    /**
     * Constructs a MathColor object from integer RGB values (0-255).
     * The values are normalized to the range 0.0 to 1.0.
     *
     * @param r The red component (0-255).
     * @param g The green component (0-255).
     * @param b The blue component (0-255).
     */
    public MathColor(int r, int g, int b) {
        this((double) r / 255, (double) g / 255, (double) b / 255);
    }

    /**
     * Private constructor that initializes the MathColor with normalized RGB values (0.0-1.0).
     *
     * @param r The red component (0.0-1.0).
     * @param g The green component (0.0-1.0).
     * @param b The blue component (0.0-1.0).
     */
    private MathColor(double r, double g, double b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    /**
     * Creates a new MathColor object from integer RGB values (0-255).
     * This method is a static factory for easier instantiation.
     *
     * @param r The red component (0-255).
     * @param g The green component (0-255).
     * @param b The blue component (0-255).
     * @return A new MathColor object representing the given RGB values.
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    public static @NotNull MathColor of(int r, int g, int b) {
        return new MathColor(r, g, b);
    }

    /**
     * Creates a new MathColor object from a java.awt.Color object.
     * This method is a static factory for easier instantiation.
     *
     * @param color The Color object to convert.
     * @return A new MathColor object representing the given Color.
     */
    @Contract("_ -> new")
    public static @NotNull MathColor of(@NotNull Color color) {
        return new MathColor(color);
    }

    /**
     * Multiplies the color components by a fading factor.
     * The fading factor is applied to each of the RGB components, adjusting the intensity of the color.
     *
     * @param factor The fading factor to determine how much the color fades.
     * @return A new MathColor object with the modified (faded) color components.
     */
    @Contract("_ -> new")
    public @NotNull MathColor multiply(double factor) {
        return new MathColor(
                this.r * fadingFactor(factor),
                this.g * fadingFactor(factor),
                this.b * fadingFactor(factor)
        );
    }

    /**
     * Multiplies this color with another MathColor and applies a fading factor.
     * The components of the current color and the other MathColor are multiplied and summed,
     * with the fading factor applied to both colors.
     *
     * @param other  The other MathColor to multiply with.
     * @param factor The fading factor to determine how much the colors fade.
     * @return A new MathColor object representing the result of multiplying the colors.
     */
    @Contract("_, _ -> new")
    public @NotNull MathColor multiply(@NotNull MathColor other, double factor) {
        return new MathColor(
                (this.r + other.r * fadingFactor(factor)),
                (this.g + other.g * fadingFactor(factor)),
                (this.b + other.b * fadingFactor(factor))
        );
    }

    /**
     * Multiplies this color with a java.awt.Color and applies a fading factor.
     * This method allows multiplication with standard Java Color objects.
     *
     * @param other  The java.awt.Color to multiply with.
     * @param factor The fading factor to determine how much the color fades.
     * @return A new MathColor object representing the result of multiplying the colors.
     */
    @Contract("_, _ -> new")
    public @NotNull MathColor multiply(@NotNull Color other, double factor) {
        return this.multiply(new MathColor(other), factor);
    }

    /**
     * Calculates the fading factor for a given value.
     * The fading factor is computed as the exponential decay of the input value.
     *
     * @param x The input value.
     * @return The calculated fading factor.
     */
    private double fadingFactor(double x) {
        return Math.exp(-x);
    }

    /**
     * Clamps the RGB values of the color to ensure they are between 0.0 and 1.0.
     * This method ensures that no color component exceeds valid bounds.
     *
     * @return A new MathColor object with clamped RGB values.
     */
    @Contract(" -> new")
    public @NotNull MathColor clamped() {
        return new MathColor(
                Math.min(1.0, Math.max(0.0, this.r)),
                Math.min(1.0, Math.max(0.0, this.g)),
                Math.min(1.0, Math.max(0.0, this.b))
        );
    }

    /**
     * Converts this MathColor object to a java.awt.Color object.
     * The MathColor is first clamped and normalized before converting to a Java Color.
     *
     * @return A new Color object representing the clamped MathColor.
     */
    @Contract(" -> new")
    public @NotNull Color getColor() {
        MathColor mathColor = this.clamped().apply(MathColor::normalize);
        return new Color(mathColor.getR(), mathColor.getG(), mathColor.getB());
    }

    /**
     * Gets the red component of the color as an integer value (0-255).
     *
     * @return The red component of the color.
     */
    public int getR() {
        return (int) (r * 255);
    }

    /**
     * Gets the green component of the color as an integer value (0-255).
     *
     * @return The green component of the color.
     */
    public int getG() {
        return (int) (g * 255);
    }

    /**
     * Gets the blue component of the color as an integer value (0-255).
     *
     * @return The blue component of the color.
     */
    public int getB() {
        return (int) (b * 255);
    }

    /**
     * Creates a new MathColor object that is the inverse of this color.
     * Inversion is achieved by subtracting each component from 1.0.
     *
     * @return A new MathColor object representing the inverse of the current color.
     */
    @Contract(value = " -> new", pure = true)
    public @NotNull MathColor inverse() {
        return new MathColor(1 - r, 1 - g, 1 - b);
    }

    /**
     * Normalizes a value using a logarithmic function.
     * The function maps values to a logarithmic scale.
     *
     * @param x The value to normalize.
     * @return The normalized value on a logarithmic scale.
     */
    public static double normalize(double x) {
        // Uncomment the next line if you want a soft sigmoid-like normalization:
        // return 1 / (1 + Math.exp(-x));
        return Math.log(1 + x) / Math.log(2);
    }

    /**
     * Applies a function to each color component (r, g, b) and returns a new MathColor object.
     *
     * @param function A function that takes a color component value and transforms it.
     * @return A new MathColor object with the transformed color components.
     */
    @Contract("_ -> new")
    public @NotNull MathColor apply(@NotNull Function<Double, Double> function) {
        return new MathColor(
                function.apply(this.r),
                function.apply(this.g),
                function.apply(this.b)
        );
    }
}