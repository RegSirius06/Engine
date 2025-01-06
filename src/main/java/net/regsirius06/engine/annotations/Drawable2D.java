package net.regsirius06.engine.annotations;

import java.awt.*;

/**
 * This interface represents an object that can be drawn on a 2D graphics context.
 * Any class implementing this interface should provide the logic for how it is drawn.
 */
public interface Drawable2D {

    /**
     * Draws the object on the specified Graphics2D context.
     * This method should contain the logic for rendering the object on the screen.
     *
     * @param g2d The Graphics2D object that is used to render the object.
     *            It provides various drawing methods for rendering shapes, images, etc.
     */
    void draw(Graphics2D g2d);
}
