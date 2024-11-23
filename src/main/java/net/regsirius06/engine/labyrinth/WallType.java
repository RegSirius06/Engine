package net.regsirius06.engine.labyrinth;

import java.awt.*;

public enum WallType {
    EMPTY(0, Color.WHITE),
    DEFAULT(1, Color.GRAY),
    END(2, Color.RED);

    public final int type;
    public final Color color;

    WallType(int type, Color color) {
        this.type = type;
        this.color = color;
    }

    public boolean notEmpty() {
        return this.type != EMPTY.type;
    }
}
