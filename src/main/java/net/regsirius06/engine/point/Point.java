package net.regsirius06.engine.point;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * The {@code Point} class represents a point in a 2D Cartesian coordinate system with {@code x} and {@code y} coordinates.
 * This class provides various methods for geometric operations such as calculating the Euclidean distance, angle between
 * two points, moving a point by certain offsets, and comparing points for equality.
 * It is particularly useful for applications like pathfinding, maze generation, or any other tasks that require 2D point manipulation.
 */
public class Point {
    /**
     * Represents the x-coordinate of this point in a 2D Cartesian coordinate system.
     * This value is immutable once the point is created.
     */
    protected double x;

    /**
     * Represents the y-coordinate of this point in a 2D Cartesian coordinate system.
     * This value is immutable once the point is created.
     */
    protected double y;

    /**
     * Constructs a new {@code Point} with the specified {@code x} and {@code y} coordinates.
     *
     * @param x the {@code x} coordinate of the point
     * @param y the {@code y} coordinate of the point
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a new {@code Point} by copying the coordinates of another {@code Point}.
     *
     * @param point the {@code Point} to copy coordinates from
     */
    public Point(@NotNull Point point) {
        this(point.x, point.y);
    }

    /**
     * Returns the {@code x} coordinate of this {@code Point}.
     *
     * @return the {@code x} coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the {@code y} coordinate of this {@code Point}.
     *
     * @return the {@code y} coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Returns a new {@code Point} object that has the same {@code x} and {@code y} coordinates as this point.
     *
     * @return a new {@code Point} with identical coordinates to this point
     */
    public Point getPoint() {
        return new Point(x, y);
    }

    /**
     * Checks if the given point {@code p} has the same coordinates as this {@code Point}.
     * This method compares the {@code x} and {@code y} coordinates of both points.
     *
     * @param p the point to compare with
     * @param <P> the type of the point, which must extend {@code Point}
     * @return {@code true} if the coordinates of {@code p} are the same as this point's coordinates, {@code false} otherwise
     */
    public <P extends Point> boolean isPointInside(@NotNull P p) {
        return isPointInside(p.getX(), p.getY());
    }

    /**
     * Checks if the given coordinates {@code x} and {@code y} are equal to this {@code Point}'s coordinates.
     *
     * @param x the {@code x} coordinate of the point to compare
     * @param y the {@code y} coordinate of the point to compare
     * @return {@code true} if the coordinates match, {@code false} otherwise
     */
    public boolean isPointInside(double x, double y) {
        return (int) this.x == (int) x && (int) this.y == (int) y;
    }

    /**
     * Calculates the Euclidean distance from this {@code Point} to another point specified by the {@code x} and {@code y} coordinates.
     * The distance is calculated as the square root of the sum of the squared differences between the respective coordinates.
     *
     * @param x the {@code x} coordinate of the other point
     * @param y the {@code y} coordinate of the other point
     * @return the Euclidean distance between this point and the given coordinates
     */
    public double distanceTo(double x, double y) {
        return Math.sqrt((this.x - x) * (this.x - x) + (this.y - y) * (this.y - y));
    }

    /**
     * Calculates the Euclidean distance from this {@code Point} to another point.
     * The distance is calculated as the square root of the sum of the squared differences between the respective coordinates.
     *
     * @param p the other point to compare with
     * @param <P> the type of the point, which must extend {@code Point}
     * @return the Euclidean distance between this point and the given {@code Point}
     */
    public <P extends Point> double distanceTo(@NotNull P p) {
        return distanceTo(p.getX(), p.getY());
    }

    /**
     * Calculates the angle in radians between this {@code Point} and another point specified by the {@code x} and {@code y} coordinates.
     * The angle is measured from the positive {@code x}-axis. Positive angles indicate counterclockwise rotation, while negative angles
     * indicate clockwise rotation.
     *
     * @param x the {@code x} coordinate of the target point
     * @param y the {@code y} coordinate of the target point
     * @return the angle in radians from this point to the specified point
     */
    public double angleTo(double x, double y) {
        return -Math.atan((this.y - y) / (this.x - x));
    }

    /**
     * Calculates the angle in radians between this {@code Point} and another point.
     * The angle is measured from the positive {@code x}-axis. Positive angles indicate counterclockwise rotation, while negative angles
     * indicate clockwise rotation.
     *
     * @param p the target point
     * @param <P> the type of the point, which must extend {@code Point}
     * @return the angle in radians from this point to the specified point
     */
    public <P extends Point> double angleTo(@NotNull P p) {
        return angleTo(p.getX(), p.getY());
    }

    /**
     * Moves this {@code Point} by the given coordinates of another {@code Point}.
     * This method returns a new {@code Point} object with the new location.
     *
     * @param point the point whose coordinates will be used to move this point
     * @return a new {@code Point} located at the new position after the move
     */
    public Point movePoint(@NotNull Point point) {
        return movePoint(point.x, point.y);
    }

    /**
     * Moves this {@code Point} by the given {@code x} and {@code y} offsets.
     * This method returns a new {@code Point} object with the new location.
     *
     * @param x the {@code x} offset to move by
     * @param y the {@code y} offset to move by
     * @return a new {@code Point} located at the new position after the move
     */
    public Point movePoint(double x, double y) {
        return new Point(this.x + x, this.y + y);
    }

    /**
     * Checks if this {@code Point} is equal to another object.
     * Two points are considered equal if both their {@code x} and {@code y} coordinates are the same.
     *
     * @param o the object to compare this point with
     * @return {@code true} if the object is a {@code Point} with the same coordinates, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Double.compare(x, point.x) == 0 && Double.compare(y, point.y) == 0;
    }

    /**
     * Returns a hash code for this {@code Point}.
     * The hash code is based on the {@code x} and {@code y} coordinates of the point.
     *
     * @return the hash code for this point
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
