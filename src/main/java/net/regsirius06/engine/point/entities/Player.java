package net.regsirius06.engine.point.entities;

import net.regsirius06.engine.point.Point;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * The {@code Player} class represents a player in a 2D space, extending the {@code Point} class.
 * It includes a direction in which the player is facing, in addition to the {@code x} and {@code y} coordinates
 * inherited from the {@code Point} class.
 * This class allows the player to move in a specified direction and adjust their direction based on steps and angles.
 */
public class Player extends Point {
    private double direction;

    /**
     * Constructs a new {@code Player} at the specified {@code x}, {@code y} coordinates, and facing a given direction.
     *
     * @param x the {@code x} coordinate of the player's position
     * @param y the {@code y} coordinate of the player's position
     * @param direction the direction the player is facing, measured in radians
     */
    public Player(double x, double y, double direction) {
        super(x, y);
        this.direction = direction;
    }

    /**
     * Constructs a new {@code Player} at the specified {@code Point} spawn location and facing a given direction.
     *
     * @param spawnPoint the {@code Point} that represents the player's initial position
     * @param direction the direction the player is facing, measured in radians
     */
    public Player(@NotNull Point spawnPoint, double direction) {
        this(spawnPoint.getX(), spawnPoint.getY(), direction);
    }

    /**
     * Constructs a new {@code Player} at the specified {@code Point} spawn location with a random direction.
     * The direction is chosen randomly within the range of {@code -Math.PI} to {@code Math.PI}.
     *
     * @param spawnPoint the {@code Point} that represents the player's initial position
     */
    public Player(Point spawnPoint) {
        this(spawnPoint, (new Random()).nextDouble(-Math.PI, Math.PI));
    }

    /**
     * Returns the direction the player is facing, measured in radians.
     * A direction of {@code 0} represents the positive {@code x}-axis, and the angle increases counterclockwise.
     *
     * @return the direction the player is facing, in radians
     */
    public final double getDirection() {
        return direction;
    }

    /**
     * Moves the player by a specified step in the direction the player is facing.
     * The direction is adjusted by the given {@code angle} (in radians), and the player's position is updated accordingly.
     * The direction is kept within the range {@code [-2 * Math.PI, 2 * Math.PI]} by normalizing it.
     *
     * @param step the distance to move the player
     * @param angle the angle (in radians) by which to adjust the player's facing direction
     */
    public final void add(double step, double angle) {
        this.direction += angle;
        // Normalize the direction to keep it within [-2π, 2π]
        if (-2 * Math.PI > this.direction) {
            this.direction += 2 * Math.PI;
        } else if (this.direction > 2 * Math.PI) {
            this.direction -= 2 * Math.PI;
        }
        // Move the player based on the current direction and step
        this.x += step * Math.cos(direction);
        this.y += step * Math.sin(direction);
    }
}
