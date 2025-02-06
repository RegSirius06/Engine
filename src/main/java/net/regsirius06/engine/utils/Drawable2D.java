package net.regsirius06.engine.utils;

import java.awt.*;

/**
 * This interface represents an object that can be drawn on a 2D graphics context.
 * Any class implementing this interface should provide the logic for how it is drawn.
 * <p>
 * The {@link #draw(Graphics2D)} method is responsible for rendering the object on the screen using a provided
 * {@link Graphics2D} context, allowing for custom rendering logic for various 2D shapes or entities.
 * </p>
 */
public interface Drawable2D {

    /**
     * Draws the object on the specified {@link Graphics2D} context.
     * This method should contain the logic for rendering the object on the screen.
     *
     * @param g2d The {@link Graphics2D} object that is used to render the object.
     *            It provides various drawing methods for rendering shapes, images, etc.
     */
    void draw(Graphics2D g2d);
}
