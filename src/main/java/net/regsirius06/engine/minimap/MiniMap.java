package net.regsirius06.engine.minimap;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import net.regsirius06.engine.labyrinth.Labyrinth2D;
import net.regsirius06.engine.labyrinth.LightSource;
import net.regsirius06.engine.labyrinth.WallType;
import org.jetbrains.annotations.NotNull;

public class MiniMap {
    private final Labyrinth2D labyrinth;
    private final List<LightSource> lightSources;
    private final int size;

    private final double scale;

    public MiniMap(@NotNull Labyrinth2D labyrinth2D, int size) {
        this.labyrinth = labyrinth2D;
        this.lightSources = labyrinth2D.getLightSources();
        this.size = size;

        this.scale = (double) size / labyrinth2D.getDimension();
    }

    public void render(Graphics2D g2d) {
        int x = 0, y = 0;
        for (List<WallType> list: this.labyrinth) {
            for (WallType w: list) {
                if (w.notEmpty()) {
                    g2d.setColor(w.color);
                    if (w.color == WallType.DEFAULT.color) {
                        g2d.setColor(Color.WHITE);
                    }
                    g2d.fillRect((int) (x * scale), (int) (y * scale), (int) scale, (int) scale);
                }
                y++;
            }
            y = 0;
            x++;
        }

        for (LightSource light : lightSources) {
            g2d.setColor(light.getColor());
            int lx = (int) (light.getX() * scale);
            int ly = (int) (light.getY() * scale);
            int step = (int) (scale / 4);
            g2d.fillOval(lx + step, ly + step, (int) scale - step, (int) scale - step);
        }
    }

    public void renderPlayer(@NotNull Graphics2D g2d, double playerX, double playerY) {
        g2d.setColor(Color.RED);
        double px = (((int) playerX) * scale);
        double py = (((int) playerY) * scale);
        double step = (scale / 3);
        g2d.fillOval((int) (px + step), (int) (py + step), (int) (scale - step), (int) (scale - step));
    }

    public void renderBorder(@NotNull Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.drawRect(0, 0, size - 1, size - 1);
    }
}

