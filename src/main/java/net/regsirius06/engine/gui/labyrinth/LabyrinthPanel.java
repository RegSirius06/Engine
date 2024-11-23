package net.regsirius06.engine.gui.labyrinth;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

import net.regsirius06.engine.labyrinth.LightSource;
import net.regsirius06.engine.labyrinth.Player;
import net.regsirius06.engine.labyrinth.WallType;
import net.regsirius06.engine.minimap.MiniMap;
import net.regsirius06.engine.labyrinth.Labyrinth2D;
import net.regsirius06.engine.util.DirectionalLightSource;
import net.regsirius06.engine.util.Distance;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class LabyrinthPanel extends JPanel implements KeyListener {
    public static final int WIDTH = 960, HEIGHT = 540, FIELD_OF_VIEW = 90;

    private static final double moveStep = 0.1, rotationStep = 0.1;

    private final Labyrinth2D labyrinth;
    private final Player player;
    private final boolean debug;
    private Set<DirectionalLightSource> visibleLightSources;

    private static final int MINI_MAP_SIZE = 200;
    private final MiniMap miniMap;

    public LabyrinthPanel(int dimension, int quantityOfLights, boolean debug) {
        this.debug = debug;
        this.labyrinth = new Labyrinth2D(dimension, quantityOfLights);
        this.player = new Player(labyrinth.getSpawnPoint(), Math.PI / 2);

        this.addKeyListener(this);
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);

        this.setBackground(Color.BLACK);
        this.miniMap = new MiniMap(labyrinth, MINI_MAP_SIZE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.visibleLightSources = new HashSet<>();
        Graphics2D g2d = (Graphics2D) g;

        renderRayCasting(g2d);
        renderWarning(g2d);
        if (debug) renderMiniMap(g2d);
    }

    private void renderWarning(@NotNull Graphics2D g2d) {
        g2d.setColor(new Color(10, 10, 10));
        if (visibleLightSources.stream().anyMatch(e -> e.lightSource().isPointInside(this.player))) {
            g2d.drawString("You in light!", WIDTH / 2, HEIGHT / 2);
        }
    }

    private void renderMiniMap(Graphics2D g2d) {
        miniMap.render(g2d);
        miniMap.renderPlayer(g2d, player.getX(), player.getY());
        miniMap.renderBorder(g2d);
    }

    private void renderRayCasting(Graphics2D g2d) {
        double fovRad = Math.toRadians(FIELD_OF_VIEW);
        int numRays = WIDTH;

        for (int rayIndex = 0; rayIndex < numRays; rayIndex++) {
            double rayAngle = player.direction + fovRad * ((rayIndex - (numRays / 2.0)) / numRays);
            Distance distance = castRay(player.getX(), player.getY(), rayAngle);
            double wallHeight = HEIGHT / distance.distance();

            Color wallColor = getWallColor(distance);

            g2d.setColor(wallColor);
            g2d.fillRect(rayIndex, HEIGHT / 2 - (int) (wallHeight / 2), 1, (int) wallHeight);
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
        double step = 0.1;
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

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(@NotNull KeyEvent e) {
        double step = 0, angle = 0;
        if (e.getKeyCode() == KeyEvent.VK_W) {
            step = moveStep;
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            step = -moveStep;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            angle = -rotationStep;
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            angle = rotationStep;
        }

        if (step != 0) {
            double newX = player.getX() + Math.cos(player.direction) * step;
            double newY = player.getY() + Math.sin(player.direction) * step;
            if (!isCollidingWithWall(newX, newY)) {
                player.add(step, angle);
            }
        } else if (angle != 0) {
            player.add(step, angle);
        }

        this.repaint();
    }

    private boolean isCollidingWithWall(double x, double y) {
        int gridX = (int) x;
        int gridY = (int) y;

        if (gridX < 0 || gridX >= labyrinth.getDimension() || gridY < 0 || gridY >= labyrinth.getDimension()) {
            return true;
        }

        WallType wallType = labyrinth.get(gridX, gridY);
        return wallType.notEmpty();
    }


    @Override
    public void keyReleased(KeyEvent e) {}
}
