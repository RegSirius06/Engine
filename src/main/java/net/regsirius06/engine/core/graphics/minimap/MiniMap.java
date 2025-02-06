package net.regsirius06.engine.core.graphics.minimap;

import java.awt.Color;
import java.awt.Graphics2D;
import net.regsirius06.engine.core.graphics.labyrinth.default2D.Labyrinth2D;
import net.regsirius06.engine.core.walls.Default;
import net.regsirius06.engine.point.entities.Player;
import net.regsirius06.engine.point.Wall;
import net.regsirius06.engine.utils.Drawable2D;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a 2D minimap of a labyrinth, displaying walls, light sources, and the player.
 * This class is responsible for rendering the minimap on a 2D Graphics context.
 * <p>
 * The minimap displays a small-scale view of the labyrinth, including walls, light sources, and the player's
 * position. The rendering is scaled to fit within a specified size and is updated dynamically.
 * </p>
 *
 * <p>
 * The minimap is displayed within a square area, with a scale factor based on the provided size. The player's
 * position is marked with a red circle, and the walls are rendered using their respective colors. If light sources
 * are present in the labyrinth, they are rendered as small circular highlights.
 * </p>
 */
public class MiniMap implements Drawable2D {

    private final Labyrinth2D labyrinth;
    private final int size;
    private final Player player;
    private final double scale;
    private final int radius = 10;

    /**
     * Constructs a new {@link MiniMap} instance with the specified labyrinth, size, and player.
     *
     * @param labyrinth2D the {@link Labyrinth2D} object representing the labyrinth
     * @param size the size of the minimap in pixels
     * @param player the {@link Player} object to be rendered on the minimap (can be null)
     */
    public MiniMap(@NotNull Labyrinth2D labyrinth2D, int size, Player player) {
        this.labyrinth = labyrinth2D;
        this.size = size;
        this.player = player;
        this.scale = (double) size / (2 * radius + 1);
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
     * <p>
     * It handles rendering the walls based on their state and color, and it highlights light sources using
     * circular indicators.
     * </p>
     *
     * @param g2d the {@link Graphics2D} object to draw onto
     */
    protected void renderMiniMap(Graphics2D g2d) {
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                int x = (int) player.getX() + dx;
                int y = (int) player.getY() + dy;

                if (labyrinth.checkAddress(x, y)) {
                    Wall w = labyrinth.get(x, y);
                    if (!w.getState().isEmpty()) {
                        g2d.setColor(w.getState().thisEquals(Default.class) ? Color.WHITE : w.getColor());
                        g2d.fillRect((int) ((dx + radius) * scale), (int) ((dy + radius) * scale), (int) scale, (int) scale);
                    }
                    if (w.getState().isLight()) {
                        g2d.setColor(w.getColor());
                        int lx = (int) ((dx + radius) * scale);
                        int ly = (int) ((dy + radius) * scale);
                        int step = (int) (scale / 4);
                        g2d.fillOval(lx + step, ly + step, (int) scale - step, (int) scale - step);
                    }
                }
            }
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
        double px = (radius * scale);
        double py = (radius * scale);
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
