package net.regsirius06.engine.API;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Represents the state of a wall in a labyrinth.
 *
 * <p>The {@code WallState} interface defines properties and behaviors of walls in a labyrinth,
 * such as whether the wall is light, empty, or generable. It also provides methods to retrieve
 * the absorption value and the color associated with the wall.</p>
 *
 * <p>As the labyrinth system evolves, additional parameters for wall states may be added.
 * Implementations of this interface should provide the specific behavior for each of the
 * defined methods based on the type of wall they represent.</p>
 *
 * <p>The default method {@link #thisEquals(Object)} checks if the current wall state is equal
 * to another object, either by class type or class comparison.</p>
 *
 * @see Loadable
 */
public interface WallState extends Loadable {

    /**
     * Returns whether the wall is light (e.g., transparent or lit).
     *
     * @return {@code true} if the wall is light, {@code false} otherwise
     */
    boolean isLight();

    /**
     * Returns whether the wall is empty (i.e., there is no wall).
     *
     * @return {@code true} if the wall is empty, {@code false} otherwise
     */
    boolean isEmpty();

    /**
     * Returns whether the wall is generable, meaning it can be dynamically created or altered.
     *
     * @return {@code true} if the wall is generable, {@code false} otherwise
     */
    boolean isGenerable();

    /**
     * Returns the absorption coefficient of the wall, which can be used to model properties
     * like energy absorption or blocking power of the wall.
     *
     * @return the absorption value of the wall
     */
    double getAbsorption();

    /**
     * Returns the color of the wall.
     *
     * @return the color of the wall
     */
    @NotNull Color getColor();

    /**
     * Checks if this wall state is equal to another object.
     *
     * <p>The method compares the class of the current object to the class of the provided
     * object to determine if they are the same type.</p>
     *
     * @param o the object to compare to
     * @return {@code true} if the objects are of the same class, {@code false} otherwise
     */
    default boolean thisEquals(Object o) {
        if (o == null) return false;
        if (o instanceof Class<?>) {
            return this.getClass() == o;
        }
        return o.getClass() == this.getClass();
    }
}
