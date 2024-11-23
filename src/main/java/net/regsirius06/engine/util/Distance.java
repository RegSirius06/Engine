package net.regsirius06.engine.util;

import net.regsirius06.engine.labyrinth.WallType;

import java.awt.*;

public record Distance(double distance, double rayLength, Color color, WallType wallType) {
}
