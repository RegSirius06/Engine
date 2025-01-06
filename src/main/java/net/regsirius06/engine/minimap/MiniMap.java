package net.regsirius06.engine.minimap;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import net.regsirius06.engine.labyrinth.Labyrinth2D;
import net.regsirius06.engine.point.LightSource;
import net.regsirius06.engine.point.Player;
import net.regsirius06.engine.point.WallEnum;
import net.regsirius06.engine.point.WallType;
import net.regsirius06.engine.annotations.Drawable2D;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a 2D minimap of a labyrinth, displaying walls, light sources, and the player.
 * This class is responsible for rendering the minimap on a 2D Graphics context.
 */
public class MiniMap implements Drawable2D {
    private final Labyrinth2D labyrinth;
    private final List<LightSource> lightSources;
    private final int size;
    private final Player player;

    private final double scale;

    /**
     * Constructs a new {@link MiniMap} instance with the specified labyrinth, size, and player.
     *
     * @param labyrinth2D the {@link Labyrinth2D} object representing the labyrinth
     * @param size the size of the minimap in pixels
     * @param player the {@link Player} object to be rendered on the minimap (can be null)
     */
    public MiniMap(@NotNull Labyrinth2D labyrinth2D, int size, Player player) {
        this.labyrinth = labyrinth2D;
        this.lightSources = labyrinth2D.getLightSources();
        this.size = size;
        this.player = player;
        this.scale = (double) size / labyrinth2D.getDimension();
    }

    /**
     * Constructs a new {@link MiniMap} instance with the specified labyrinth and size.
     * This version does not render the player.
     *
     * @param labyrinth2D the {@link Labyrinth2D} object representing the labyrinth
     * @param size the size of the minimap in pixels
     */
    public MiniMap(@NotNull Labyrinth2D labyrinth2D, int size) {
        this(labyrinth2D, size, null);
    }

    /**
     * Draws the minimap onto the provided {@link Graphics2D} object.
     * This method renders the labyrinth, light sources, and player (if available),
     * as well as the border of the minimap.
     *
     * @param g2d the {@link Graphics2D} object to draw onto
     */
    @Override
    public final void draw(Graphics2D g2d) {
        this.renderMiniMap(g2d);
        if (player != null) this.renderPlayer(g2d, player.getX(), player.getY());
        this.renderBorder(g2d);
    }

    /**
     * Renders the labyrinth on the minimap.
     * This method iterates through the labyrinth and fills the respective tiles with appropriate colors.
     *
     * @param g2d the {@link Graphics2D} object to draw onto
     */
    protected void renderMiniMap(Graphics2D g2d) {
        int x = 0, y = 0;
        for (List<WallType> list: this.labyrinth) {
            for (WallType w: list) {
                if (w.notEmpty()) {
                    g2d.setColor(w.getColor());
                    if (w.getColor() == WallEnum.DEFAULT.color) {
                        g2d.setColor(Color.WHITE);
                    }
                    g2d.fillRect((int) (x * scale), (int) (y * scale), (int) scale, (int) scale);
                }
                y++;
            }
            y = 0;
            x++;
        }

        // Render light sources
        for (LightSource light : lightSources) {
            g2d.setColor(light.getColor());
            int lx = (int) (light.getX() * scale);
            int ly = (int) (light.getY() * scale);
            int step = (int) (scale / 4);
            g2d.fillOval(lx + step, ly + step, (int) scale - step, (int) scale - step);
        }
    }

    /**
     * Renders the player on the minimap at the specified coordinates.
     * If the player is not null, this method will draw the player as a red circle.
     *
     * @param g2d the {@link Graphics2D} object to draw onto
     * @param playerX the x-coordinate of the player
     * @param playerY the y-coordinate of the player
     */
    protected void renderPlayer(@NotNull Graphics2D g2d, double playerX, double playerY) {
        g2d.setColor(Color.RED);
        double px = (((int) playerX) * scale);
        double py = (((int) playerY) * scale);
        double step = (scale / 3);
        g2d.fillOval((int) (px + step), (int) (py + step), (int) (scale - step), (int) (scale - step));
    }

    /**
     * Renders the border of the minimap.
     * This method draws a white border around the minimap.
     *
     * @param g2d the {@link Graphics2D} object to draw onto
     */
    protected void renderBorder(@NotNull Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.drawRect(0, 0, size - 1, size - 1);
    }
}
