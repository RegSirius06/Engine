package net.regsirius06.engine.utils;

import net.regsirius06.engine.point.Point;
import net.regsirius06.engine.point.Wall;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for various implementations of 2D labyrinths.
 * This interface defines methods for retrieving and setting wall types at specific coordinates in the labyrinth.
 * It also includes methods for checking collisions and retrieving key features such as the spawn point and labyrinth dimensions.
 * <p>
 * Implementations of this interface are expected to handle the specifics of storing and manipulating the labyrinth structure,
 * including managing the wall types and verifying if a point is within the labyrinth's bounds.
 * </p>
 */
@Deprecated
public interface ILabyrinth2D {
    /**
     * Returns the wall type at the specified coordinates (x, y) in the labyrinth.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the {@link Wall} at the given coordinates
     */
    Wall get(int x, int y);

    /**
     * Returns the wall type at the location of a given {@link Point}.
     *
     * @param p the point to get the wall type for
     * @param <P> the type of point (subtype of {@link Point})
     * @return the {@link Wall} at the given point
     */
    default <P extends Point> Wall get(@NotNull P p) {
        return this.get((int) p.getX(), (int) p.getY());
    }

    /**
     * Sets the wall type at the specified coordinates (x, y) in the labyrinth.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param wall the wall type to set
     */
    void set(int x, int y, Wall wall);

    /**
     * Sets the wall type at the location of a given {@link Point}.
     *
     * @param p the point to set the wall type for
     * @param <P> the type of point (subtype of {@link Point})
     * @param wall the wall type to set
     */
    default <P extends Point> void set(@NotNull P p, Wall wall) {
        this.set((int) p.getX(), (int) p.getY(), wall);
    }

    /**
     * Sets the wall type in its location.
     *
     * @param wall the wall type to set
     */
    default void set(Wall wall) {
        this.set((int) wall.getX(), (int) wall.getY(), wall);
    }

    /**
     * Checks if the specified coordinates are within the bounds of the labyrinth.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return true if the coordinates are within bounds, false otherwise
     */
    boolean checkAddress(int x, int y);

    /**
     * Checks if the specified position (x, y) is colliding with a wall in the labyrinth.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return true if there is a collision with a wall, false otherwise
     */
    boolean isCollidingWithWall(double x, double y);

    /**
     * Returns the spawn point of the labyrinth.
     *
     * @return the {@link Point} representing the spawn point
     */
    @NotNull Point getSpawnPoint();

    /**
     * Returns the dimension of the labyrinth (i.e., the width/height of the square grid).
     *
     * @return the dimension of the labyrinth
     */
    int getDimension();
}
