package net.regsirius06.engine.point;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * The {@code Point} class represents a point in a 2D space with {@code x} and {@code y} coordinates.
 * This class provides methods to perform geometric operations such as calculating distance and angle
 * between two points, as well as copying and comparing points.
 */
public class Point {
    protected double x, y;

    /**
     * Constructs a new point with the given {@code x} and {@code y} coordinates.
     *
     * @param x the {@code x} coordinate of the point
     * @param y the {@code y} coordinate of the point
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a new point by copying the coordinates of an existing point.
     *
     * @param point the point to copy from
     */
    public Point(@NotNull Point point) {
        this(point.x, point.y);
    }

    /**
     * Returns the {@code x} coordinate of this point.
     *
     * @return the {@code x} coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the {@code y} coordinate of this point.
     *
     * @return the {@code y} coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Returns a new {@code Point} object with the same coordinates as this point.
     *
     * @return a new {@code Point} with the same {@code x} and {@code y} coordinates
     */
    public Point getPoint() {
        return new Point(x, y);
    }

    /**
     * Checks if a given point {@code p} is inside this point's location by comparing its coordinates.
     *
     * @param p the point to compare
     * @param <P> the type of the point, which must extend {@code Point}
     * @return {@code true} if the coordinates of {@code p} are equal to this point, {@code false} otherwise
     */
    public <P extends Point> boolean isPointInside(@NotNull P p) {
        return isPointInside(p.getX(), p.getY());
    }

    /**
     * Checks if the given coordinates ({@code x}, {@code y}) are the same as this point's coordinates.
     *
     * @param x the {@code x} coordinate of the point to compare
     * @param y the {@code y} coordinate of the point to compare
     * @return {@code true} if the coordinates are the same, {@code false} otherwise
     */
    public boolean isPointInside(double x, double y) {
        return (int) this.x == (int) x && (int) this.y == (int) y;
    }

    /**
     * Calculates the Euclidean distance from this point to another point with the given coordinates.
     *
     * @param x the {@code x} coordinate of the other point
     * @param y the {@code y} coordinate of the other point
     * @return the distance between this point and the given coordinates
     */
    public double distanceTo(double x, double y) {
        return Math.sqrt((this.x - x) * (this.x - x) + (this.y - y) * (this.y - y));
    }

    /**
     * Calculates the Euclidean distance from this point to another point.
     *
     * @param p the other point
     * @param <P> the type of the point, which must extend {@code Point}
     * @return the distance between this point and the given point
     */
    public <P extends Point> double distanceTo(@NotNull P p) {
        return distanceTo(p.getX(), p.getY());
    }

    /**
     * Calculates the angle from this point to another point specified by {@code x} and {@code y} coordinates.
     * The angle is measured in radians, with positive angles indicating counterclockwise rotation
     * and negative angles indicating clockwise rotation from the positive x-axis.
     *
     * @param x the {@code x} coordinate of the target point
     * @param y the {@code y} coordinate of the target point
     * @return the angle in radians from this point to the specified point
     */
    public double angleTo(double x, double y) {
        return -Math.atan((this.y - y) / (this.x - x));
    }

    /**
     * Calculates the angle from this point to another point.
     * The angle is measured in radians, with positive angles indicating counterclockwise rotation
     * and negative angles indicating clockwise rotation from the positive x-axis.
     *
     * @param p the target point
     * @param <P> the type of the point, which must extend {@code Point}
     * @return the angle in radians from this point to the specified point
     */
    public <P extends Point> double angleTo(@NotNull P p) {
        return angleTo(p.getX(), p.getY());
    }

    /**
     * Moves the current point by the coordinates of another point.
     * Returns a new point with the new location.
     *
     * @param point the point to move by
     * @return a new {@code Point} located at the current point moved by the given point's coordinates
     */
    public Point movePoint(@NotNull Point point) {
        return movePoint(point.x, point.y);
    }

    /**
     * Moves the current point by the given {@code x} and {@code y} offsets.
     * Returns a new point with the new location.
     *
     * @param x the {@code x} offset to move by
     * @param y the {@code y} offset to move by
     * @return a new {@code Point} located at the current point moved by the given offsets
     */
    public Point movePoint(double x, double y) {
        return new Point(this.x + x, this.y + y);
    }

    /**
     * Checks if this point is equal to another object.
     * Two points are considered equal if their {@code x} and {@code y} coordinates are the same.
     *
     * @param o the object to compare this point to
     * @return {@code true} if the object is a point with the same coordinates as this point,
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Double.compare(x, point.x) == 0 && Double.compare(y, point.y) == 0;
    }

    /**
     * Returns a hash code for this point. The hash code is based on the {@code x} and {@code y} coordinates.
     *
     * @return a hash code for this point
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
