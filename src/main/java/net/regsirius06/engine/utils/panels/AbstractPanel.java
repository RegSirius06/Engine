package net.regsirius06.engine.utils.panels;

import net.regsirius06.engine.utils.Drawable2D;

import javax.swing.*;
import java.awt.*;

/**
 * An abstract panel that provides a foundation for custom JPanel implementations.
 * This panel implements the {@link Drawable2D} interface, allowing custom rendering
 * of graphical content via the {@link #draw(Graphics2D)} method.
 * <p>
 * The {@link #paintComponent(Graphics)} method is overridden to call the {@link #draw(Graphics2D)}
 * method for custom rendering on the panel.
 * </p>
 */
public abstract class AbstractPanel extends JPanel implements Drawable2D {

    /**
     * Paints the component by invoking the {@link #draw(Graphics2D)} method.
     * This method is called whenever the panel needs to be re-rendered.
     *
     * @param g the Graphics object used for rendering; it is cast to {@link Graphics2D} for more advanced drawing
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.draw((Graphics2D) g);
    }
}
