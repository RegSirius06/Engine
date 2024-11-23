package net.regsirius06.engine.labyrinth;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class LightSource extends Point {
    public static final List<Color> COLORS = List.of(
            Color.GREEN, Color.BLUE, Color.MAGENTA,
            Color.ORANGE, Color.YELLOW, Color.CYAN,
            Color.RED, Color.PINK, new Color(128, 0, 255)
    );
    private final Color color;

    public LightSource(double x, double y, Color color) {
        super(x, y);
        this.color = color;
    }

    public LightSource(@NotNull Point point, Color color) {
        this(point.getX(), point.getY(), color);
    }

    public <P extends Point> boolean isPointInside(@NotNull P p) {
        return this.isPointInside(p.getX(), p.getY());
    }

    public boolean isPointInside(double x, double y) {
        int gridX = (int) x;
        int gridY = (int) y;
        return this.x == gridX && this.y == gridY;
    }

    public Color getColor() {
        return color;
    }
}
