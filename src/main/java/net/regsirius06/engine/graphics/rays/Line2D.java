package net.regsirius06.engine.graphics.rays;

/**
 * Represents a 2D line in the Cartesian plane.
 * A Line2D object can be constructed using the slope and y-intercept,
 * two points, or an angle and a point.
 */
public class Line2D {
    private final double k; // Slope of the line
    private final double b; // Y-intercept of the line

    /**
     * Constructs a Line2D object using the slope (k) and the y-intercept (b).
     *
     * @param k The slope of the line. It represents the change in y for a given change in x.
     * @param b The y-intercept of the line. It is the point where the line crosses the y-axis.
     */
    public Line2D(double k, double b) {
        this.k = k;
        this.b = b;
    }

    /**
     * Constructs a Line2D object that passes through a specified point (x0, y0) with a given angle.
     * The angle is specified in radians, where 0 radians represents a horizontal line.
     *
     * @param x0    The x-coordinate of a point through which the line passes.
     * @param y0    The y-coordinate of the point.
     * @param angle The angle of the line in radians with respect to the x-axis.
     */
    public Line2D(double x0, double y0, double angle) {
        this.k = Math.tan(angle);  // Slope from the angle (tan of the angle)
        this.b = y0 - this.k * x0; // Y-intercept calculated from the point (x0, y0)
    }

    /**
     * Constructs a Line2D object passing through two points (x0, y0) and (x1, y1).
     * The slope is calculated as the difference in y-coordinates divided by the difference in x-coordinates.
     *
     * @param x0 The x-coordinate of the first point.
     * @param y0 The y-coordinate of the first point.
     * @param x1 The x-coordinate of the second point.
     * @param y1 The y-coordinate of the second point.
     */
    public Line2D(double x0, double y0, double x1, double y1) {
        this.k = (y1 - y0) / (x1 - x0); // Slope calculation using the two points
        this.b = y0 - this.k * x0;      // Y-intercept calculation using the first point
    }

    /**
     * Calculates the y-coordinate for a given x-coordinate based on the line equation (y = kx + b).
     *
     * @param x The x-coordinate for which to calculate the corresponding y-coordinate.
     * @return The y-coordinate corresponding to the given x-coordinate on this line.
     */
    public double getY(double x) {
        return k * x + b;
    }

    /**
     * Calculates the x-coordinate for a given y-coordinate based on the line equation (x = (y - b) / k).
     *
     * @param y The y-coordinate for which to calculate the corresponding x-coordinate.
     * @return The x-coordinate corresponding to the given y-coordinate on this line.
     */
    public double getX(double y) {
        return (y - b) / k;
    }

    /**
     * Calculates the angle of the line with respect to the x-axis.
     * The angle is calculated using the arctangent of the slope (atan(k)).
     *
     * @return The angle of the line in radians with respect to the positive x-axis.
     */
    public double getAngle() {
        return Math.atan(k); // Angle calculation from the slope
    }

    /**
     * Rotates the line around a given point (x0, y0) by a specified angle in radians.
     * The line will be rotated while keeping the point (x0, y0) fixed.
     *
     * @param x0    The x-coordinate of the rotation center.
     * @param angle The angle of rotation in radians.
     * @return A new Line2D object representing the rotated line.
     */
    public Line2D rotate(double x0, double angle) {
        // The line is rotated by adjusting the angle of the line, with the fixed point (x0, getY(x0))
        return new Line2D(x0, getY(x0), this.getAngle() + angle);
    }

    /**
     * Calculates and returns a new Line2D object that is perpendicular (normal) to this line.
     * The normal line has a slope of -1/k and passes through the same y-intercept.
     *
     * @return A new Line2D object representing the normal line to this line.
     */
    public Line2D normal() {
        return new Line2D(-1 / k, b); // Perpendicular slope to the original line
    }
}
