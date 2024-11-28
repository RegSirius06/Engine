package net.regsirius06.engine.labyrinth;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class Player extends Point {
    public double direction;

    public Player(double x, double y, double direction) {
        super(x, y);
        this.direction = direction;
    }

    public Player(@NotNull Point point, double direction) {
        this(point.getX(), point.getY(), direction);
    }

    public Player(Point spawnPoint) {
        this(spawnPoint, (new Random()).nextDouble(-Math.PI, Math.PI));
    }

    public void add(double step, double angle) {
        this.direction += angle;
        if (-2 * Math.PI > this.direction) {
            this.direction += 2 * Math.PI;
        } else if (this.direction > 2 * Math.PI) {
            this.direction -= 2 * Math.PI;
        }
        this.x += step * Math.cos(direction);
        this.y += step * Math.sin(direction);
    }
}
