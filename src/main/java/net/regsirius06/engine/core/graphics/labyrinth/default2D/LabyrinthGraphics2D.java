package net.regsirius06.engine.core.graphics.labyrinth.default2D;

import net.regsirius06.engine.core.walls.Empty;
import net.regsirius06.engine.core.graphics.Filter;
import net.regsirius06.engine.core.graphics.MathColor;
import net.regsirius06.engine.core.graphics.rays.Line2D;
import net.regsirius06.engine.point.*;
import net.regsirius06.engine.utils.Drawable2D;
import net.regsirius06.engine.point.Point;
import net.regsirius06.engine.point.entities.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The LabyrinthGraphics2D class provides the logic for rendering a 2D labyrinth with ray casting effects,
 * simulating what the player sees, including light sources, reflections, and walls.
 * <p>
 * This class visualizes the labyrinth and displays the player's perspective on the surrounding environment,
 * taking into account factors such as lighting, intersections with objects, and reflections.
 * <p>
 * It implements the Drawable2D interface, which allows the labyrinth to be drawn on a 2D canvas.
 */
public class LabyrinthGraphics2D implements Drawable2D {

    /** Movement step for rays during raycasting. */
    public static final double moveStep = 0.1;

    /** Rotation step for rays (not used in current implementation). */
    public static final double rotationStep = 0.1;

    /** Field of view (FOV) for the player in degrees. */
    private static final int FIELD_OF_VIEW = 90;

    /** Canvas width for rendering. */
    private final int width;

    /** Canvas height for rendering. */
    private final int height;

    /** The labyrinth instance, representing the structure of the environment. */
    private final Labyrinth2D labyrinth;

    /** The player instance, representing the player's position and direction. */
    private final Player player;

    /** Set of visible light sources found by the player. */
    private Set<Wall> visibleLightSources;

    /** List of indices for the rays cast by the player. */
    private List<Integer> view;

    /** List of points with reflections, representing rays and intersections with objects. */
    private List<ColoredPoint> viewWithReflections;

    /**
     * Constructor that initializes the LabyrinthGraphics2D instance.
     *
     * @param width The width of the canvas for rendering.
     * @param height The height of the canvas for rendering.
     * @param labyrinth The labyrinth instance representing the environment.
     * @param player The player instance determining the position and direction.
     */
    public LabyrinthGraphics2D(int width, int height, Labyrinth2D labyrinth, Player player) {
        this.width = width;
        this.height = height;
        this.labyrinth = labyrinth;
        this.player = player;
    }

    /**
     * Returns the set of visible light sources.
     *
     * @return A set of light sources that are visible to the player.
     */
    public final Set<Wall> getVisibleLightSources() {
        return visibleLightSources;
    }

    /**
     * Draws the 2D visualization of the labyrinth and what the player sees.
     *
     * @param g2d The Graphics2D object used for drawing the scene.
     */
    @Override
    public final void draw(Graphics2D g2d) {
        this.visibleLightSources = new HashSet<>();
        this.viewWithReflections = new ArrayList<>();
        this.view = new ArrayList<>();
        this.renderRayCasting();
        this.visualize(g2d);
    }

    /**
     * Visualizes the labyrinth, including walls and light sources.
     *
     * @param g2d The Graphics2D object used for rendering the scene.
     */
    protected void visualize(Graphics2D g2d) {
        Color color = null;
        // If the player is inside a light source, the background is filled with a light color.
        for (Wall light : visibleLightSources) {
            if (light.isPointInside(this.player)) {
                color = light.getColor();
                break;
            }
        }

        if (color != null) {
            g2d.setColor(Filter.mix(color, Color.WHITE).getColor());
            g2d.fillRect(0, 0, width, height);
            return;
        }

        // Otherwise, render the walls with lighting and reflections.
        for (int rayIndex = 0; rayIndex < width; rayIndex++) {
            ColoredPoint point = viewWithReflections.get(view.get(rayIndex));
            double wallHeight = height / point.distanceTo(this.player);
            Color wallColor = point.getColor();

            g2d.setColor(wallColor);
            g2d.fillRect(rayIndex, height / 2 - (int) (wallHeight / 2), 1, (int) wallHeight);
        }
    }

    /**
     * Transforms a given index into an angle for raycasting.
     *
     * @param index The index of the ray.
     * @param fovRad The field of view in radians.
     * @param numRays The total number of rays cast.
     * @return The calculated angle for the ray.
     */
    private double transformToAngle(int index, double fovRad, int numRays) {
        return claimAngle(player.getDirection() + fovRad * ((index - (numRays / 2.0)) / numRays));
    }

    /**
     * Claims an angle by normalizing it to the range [0, 2π).
     *
     * @param angle The angle to normalize.
     * @return The normalized angle in radians.
     */
    protected static double claimAngle(double angle) {
        return angle % (2 * Math.PI);
    }

    /**
     * Casts rays to detect walls and light sources.
     * The method performs the raycasting and calculates intersections with walls, light sources,
     * and reflections in the environment.
     */
    private void renderRayCasting() {
        double fovRad = Math.toRadians(FIELD_OF_VIEW);
        int numRays = width;

        for (int rayIndex = 0; rayIndex < numRays; rayIndex++) {
            double rayAngle = transformToAngle(rayIndex, fovRad, numRays);
            castRay(player.getX(), player.getY(), rayAngle, fovRad, numRays);
        }
    }

    /**
     * Casts a ray from the player's position in a specified direction.
     *
     * @param startX The starting X-coordinate of the ray.
     * @param startY The starting Y-coordinate of the ray.
     * @param rayAngle The angle at which the ray is cast.
     * @param fovRad The field of view in radians.
     * @param numRays The total number of rays to cast.
     */
    private void castRay(double startX, double startY, double rayAngle, double fovRad, int numRays) {
        double dx = Math.cos(rayAngle);
        double dy = Math.sin(rayAngle);

        double distance = 0;
        double step = moveStep;
        double full_length = 0;
        int reflections = 0;
        Line2D line = new Line2D(startX, startY, rayAngle);

        // Special case: Check if the player is at a light source
        if (this.labyrinth.get((int) startX, (int) startY).getState().isLight() &&
                this.labyrinth.get((int) startX, (int) startY).isPointInside(this.player)) {
            this.viewWithReflections.add(new ColoredPoint(
                    startX, startY,
                    Filter.asLight(
                            MathColor.of(this.labyrinth.get((int) startX, (int) startY).getColor())
                                    .multiply(this.labyrinth.get((int) startX, (int) startY).getState().getAbsorption())
                    ).getColor()
            ));
            this.view.add(this.viewWithReflections.size() - 1);
            this.visibleLightSources.add(this.labyrinth.get((int) startX, (int) startY));
            return;
        }

        // Raycasting loop
        while (full_length < 10_000 && reflections < 8) {
            int x = (int) (startX + dx * distance);
            int y = (int) (startY + dy * distance);

            if (!this.labyrinth.checkAddress(x, y)) {
                break;
            }

            // Check for light source intersections and reflections
            if (this.labyrinth.get(x, y).getState().isLight()) {
                if (reflections == 0) {
                    visibleLightSources.add(this.labyrinth.get(x, y));
                    view.add(this.viewWithReflections.size());
                }
                this.viewWithReflections.add(new ColoredPoint(
                        startX + dx * distance, startY + dy * distance,
                        Filter.asLight(
                                MathColor.of(this.labyrinth.get(x, y).getColor()).multiply(distance)
                                        .multiply(this.labyrinth.get(x, y).getState().getAbsorption())
                        ).getColor()
                ));
                for (int i = view.get(view.size() - 1); i < viewWithReflections.size(); i++) {
                    ColoredPoint buffer = viewWithReflections.get(i);
                    viewWithReflections.set(i, new ColoredPoint(
                            buffer.getPoint(),
                            Filter.mix(
                                    buffer.getColor(),
                                    viewWithReflections.get(viewWithReflections.size() - 1).getColor()
                            ).getColor()
                    ));
                    int finalI = i;
                    viewWithReflections = viewWithReflections.stream().map(
                            e -> e == buffer ? viewWithReflections.get(finalI) : e
                    ).collect(Collectors.toList());
                }
                return;
            }

            // If a wall is encountered, process it and continue the raycasting
            if (!this.labyrinth.get(x, y).getState().isEmpty()) {
                this.viewWithReflections.add(new ColoredPoint(
                        startX + dx * distance, startY + dy * distance,
                        MathColor.of(this.labyrinth.get(x, y).getColor()).multiply(distance)
                                .multiply(this.labyrinth.get(x, y).getState().getAbsorption()).getColor()
                ));
                if (reflections == 0) {
                    view.add(this.viewWithReflections.size() - 1);
                }

                rayAngle = getAngle(line, startX, x, fovRad);
                reflections++;

                full_length += distance;
                startX += distance * dx;
                startY += distance * dy;
                dx = Math.cos(rayAngle);
                dy = Math.sin(rayAngle);
                distance = 0;
                line = new Line2D(startX, startY, rayAngle);
            }
            distance += step;
        }
    }

    /**
     * Calculates the angle of a ray based on its interaction with the walls in the environment.
     *
     * @param currentRay The current ray being cast.
     * @param startX The starting X-coordinate of the ray.
     * @param wallX The X-coordinate where the ray intersects with a wall.
     * @param deltaAngle The difference in angle between rays.
     * @return The calculated angle of the ray.
     */
    private double getAngle(@NotNull Line2D currentRay, double startX, double wallX, double deltaAngle) {
        // Calculation logic omitted for brevity.
        Line2D leftRay = currentRay.rotate(startX, deltaAngle);
        Line2D rightRay = currentRay.rotate(startX, -deltaAngle);
        Point start = new Point(startX, currentRay.getY(startX));
        double wallY = currentRay.getY(wallX);
        Wall wall;
        try {
            wall = new Wall(wallX, wallY, this.labyrinth.get((int) wallX, (int) wallY).getState());
        } catch (IndexOutOfBoundsException e) {
            wall = new Wall(wallX, wallY, new Empty());
        }
        boolean isLeft = start.getX() > wall.getX();
        boolean isUp = start.getY() < wall.getY();
        double leftX = leftRay.getX(wallY);
        double leftY = leftRay.getY(wallX);
        double rightX = rightRay.getX(wallY);
        double rightY = rightRay.getY(wallX);
        Wall constYLeft, constYRight, constXLeft, constXRight;
        try {
            constYLeft = new Wall(leftX, wallY, this.labyrinth.get((int) leftX, (int) wallY).getState());
        } catch (IndexOutOfBoundsException e) {
            constYLeft = new Wall(leftX, wallY, new Empty());
        }
        try {
            constYRight = new Wall(rightX, wallY, this.labyrinth.get((int) rightX, (int) wallY).getState());
        } catch (IndexOutOfBoundsException e) {
            constYRight = new Wall(rightX, wallY, new Empty());
        }
        try {
            constXLeft = new Wall(wallX, leftY, this.labyrinth.get((int) wallX, (int) leftY).getState());
        } catch (IndexOutOfBoundsException e) {
            constXLeft = new Wall(wallX, leftY, new Empty());
        }
        try {
            constXRight = new Wall(wallX, rightY, this.labyrinth.get((int) wallX, (int) rightY).getState());
        } catch (IndexOutOfBoundsException e) {
            constXRight = new Wall(wallX, rightY, new Empty());
        }

        Wall left, right;
        double alpha;
        if (!constYLeft.getState().isEmpty() && !constXLeft.getState().isEmpty()) {
            left = (start.distanceTo(constYLeft) < start.distanceTo(constXLeft) ? constYLeft : constXLeft);
        } else if (constYLeft.getState().isEmpty() && !constXLeft.getState().isEmpty()) {
            left = constXLeft;
        } else if (!constYLeft.getState().isEmpty() && constXLeft.getState().isEmpty()) {
            left = constYLeft;
        } else {
            if (isLeft && isUp) {
                alpha = 3 * Math.PI / 2;
            } else if (isLeft) {
                alpha = 0;
            } else if (isUp) {
                alpha = Math.PI;
            } else {
                alpha = Math.PI / 2;
            }
            double rt = 2 * alpha - currentRay.getAngle();
            return claimAngle(rt);
        }
        if (!constYRight.getState().isEmpty() && !constXRight.getState().isEmpty()) {
            right = (start.distanceTo(constYRight) < start.distanceTo(constXRight) ? constYRight : constXRight);
        } else if (constYRight.getState().isEmpty() && !constXRight.getState().isEmpty()) {
            right = constXRight;
        } else if (!constYRight.getState().isEmpty() && constXRight.getState().isEmpty()) {
            right = constYRight;
        } else {
            if (isLeft && isUp) {
                alpha = 0;
            } else if (isLeft) {
                alpha = Math.PI / 2;
            } else if (isUp) {
                alpha = 3 * Math.PI / 2;
            } else {
                alpha = Math.PI;
            }
            double rt = 2 * alpha - currentRay.getAngle();
            return claimAngle(rt);
        }

        if (left.getX() == wall.getX()) {
            if (right.getY() == wall.getY()) {
                if (isLeft && isUp) {
                    alpha = 7 * Math.PI / 4;
                } else if (isLeft) {
                    alpha = Math.PI / 4;
                } else if (isUp) {
                    alpha = 5 * Math.PI / 4;
                } else {
                    alpha = 3 * Math.PI / 4;
                }
            } else {
                alpha = (isUp ? 3 * Math.PI / 2 : Math.PI / 2);
            }
        } else if (left.getY() == wall.getY()) {
            if (right.getX() == wall.getX()) {
                if (isLeft && isUp) {
                    alpha = 5 * Math.PI / 4;
                } else if (isLeft) {
                    alpha = 7 * Math.PI / 4;
                } else if (isUp) {
                    alpha = 3 * Math.PI / 4;
                } else {
                    alpha = Math.PI / 4;
                }
            } else {
                alpha = (isLeft ? 0 : Math.PI);
            }
        } else {
            if (right.getX() == wall.getX()) {
                alpha = (isUp ? 3 * Math.PI / 2 : Math.PI / 2);
            } else if (right.getY() == wall.getY()) {
                alpha = (isLeft ? 0 : Math.PI);
            } else {
                // I don't know when it's possible...
                // Come back!
                alpha = currentRay.getAngle();
            }
        }
        double rt = 2 * alpha - currentRay.getAngle();
        return claimAngle(rt);
    }
}
