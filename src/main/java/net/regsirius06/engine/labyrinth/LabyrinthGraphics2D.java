package net.regsirius06.engine.labyrinth;

import net.regsirius06.engine.util.DirectionalLightSource;
import net.regsirius06.engine.util.Distance;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class LabyrinthGraphics2D {
    public static final double moveStep = 0.1, rotationStep = 0.1;
    private static final int FIELD_OF_VIEW = 90;
    private final int width, height;

    private final Labyrinth2D labyrinth;
    private final Player player;
    private Set<DirectionalLightSource> visibleLightSources;

    public LabyrinthGraphics2D(int width, int height, Labyrinth2D labyrinth, Player player) {
        this.width = width;
        this.height = height;
        this.labyrinth = labyrinth;
        this.player = player;
    }

    public Set<DirectionalLightSource> getVisibleLightSources() {
        return visibleLightSources;
    }

    public void draw(Graphics2D g2d) {
        this.visibleLightSources = new HashSet<>();
        renderRayCasting(g2d);
    }

    private void renderRayCasting(Graphics2D g2d) {
        double fovRad = Math.toRadians(FIELD_OF_VIEW);
        int numRays = width;

        for (int rayIndex = 0; rayIndex < numRays; rayIndex++) {
            double rayAngle = player.direction + fovRad * ((rayIndex - (numRays / 2.0)) / numRays);
            Distance distance = castRay(player.getX(), player.getY(), rayAngle);
            double wallHeight = height / distance.distance();

            Color wallColor = getWallColor(distance);

            g2d.setColor(wallColor);
            g2d.fillRect(rayIndex, height / 2 - (int) (wallHeight / 2), 1, (int) wallHeight);
        }
    }

    private @NotNull Color getWallColor(@NotNull Distance distance) {
        Color finalColor = distance.wallType().color;
        double brightness = normalizeDistance(distance);

        return blendColors(finalColor, distance.color(), brightness);
    }

    private static @NotNull Color blendColors(@NotNull Color color1, @NotNull Color color2, double brightnessOfColor2) {
        int r = (color1.getRed() > 0) ?
                (int) (color1.getRed() * (1 - brightnessOfColor2) + color2.getRed() * brightnessOfColor2) : 0;
        int g = (color1.getGreen() > 0) ?
                (int) (color1.getGreen() * (1 - brightnessOfColor2) + color2.getGreen() * brightnessOfColor2) : 0;
        int b = (color1.getBlue() > 0) ?
                (int) (color1.getBlue() * (1 - brightnessOfColor2) + color2.getBlue() * brightnessOfColor2) : 0;

        return new Color(Math.min(r, 255), Math.min(g, 255), Math.min(b, 255));
    }

    private static double normalizeDistance(@NotNull Distance distance) {
        //return distance.distance() / distance.rayLength();
        //return 1 / (1 + Math.exp(-distance.distance() / distance.rayLength()));
        return Math.log(1 + distance.distance() / distance.rayLength()) / Math.log(2);
    }

    @Contract("_, _, _ -> new")
    private @NotNull Distance castRay(double playerX, double playerY, double rayAngle) {
        double dx = Math.cos(rayAngle);
        double dy = Math.sin(rayAngle);

        double distance = 0;
        double step = moveStep;
        boolean hitLightSource = false;
        double length = 1000;
        double full_length = 0;
        boolean buf = true;
        int reflections = 0;
        LightSource light = null;
        WallType wallType = WallType.EMPTY;

        while (distance < 1000 && reflections < 8) {
            int x = (int) (playerX + dx * distance);
            int y = (int) (playerY + dy * distance);

            if (x < 0 || x >= labyrinth.getDimension() || y < 0 || y >= labyrinth.getDimension()) {
                break;
            }

            if (light == null) {
                light = labyrinth.getLightSource(x, y);
            }
            if (light != null) {
                hitLightSource = true;
                visibleLightSources.add(new DirectionalLightSource(light, light.distanceTo(this.player)));
                if (buf) {
                    length = distance;
                    buf = false;
                }
                if (light.isPointInside(this.player)) {
                    return new Distance(step, step, blendColors(Color.WHITE, light.getColor(), 0.1), wallType);
                }
            }

            if (labyrinth.get(x, y).notEmpty()) {
                if (buf) {
                    wallType = labyrinth.get(x, y);
                    length = distance;
                    buf = false;
                }
                double wallAngle = Math.atan2(y - playerY, x - playerX);
                rayAngle = 2 * wallAngle - rayAngle;

                reflections++;

                dx = Math.cos(rayAngle);
                dy = Math.sin(rayAngle);
                full_length += distance;
                distance = 0;
                playerX = x;
                playerY = y;
            }
            distance += step;
        }

        return new Distance(length, full_length, hitLightSource ? light.getColor() : Color.BLACK, wallType);
    }
}
