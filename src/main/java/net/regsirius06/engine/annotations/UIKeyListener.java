package net.regsirius06.engine.annotations;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * An interface for handling key events in a user interface (UI).
 * This interface extends KeyListener and provides default implementations
 * for the {@link #keyTyped(KeyEvent)} and {@link #keyReleased(KeyEvent)} methods,
 * allowing implementers to focus on the {@link #keyPressed(KeyEvent)} method.
 * <p>
 * Implementers of this interface only need to override the keyPressed method
 * to handle key press events, as the other methods are provided with default no-op behavior.
 */
public interface UIKeyListener extends KeyListener {

    /**
     * Invoked when a key has been typed.
     * This method is empty by default, meaning no action is performed.
     * Implementers can override this method if they want to handle key typed events.
     *
     * @param e the KeyEvent containing details about the key event (such as the key code and the key character)
     */
    @Override
    default void keyTyped(KeyEvent e) {}

    /**
     * Invoked when a key has been released.
     * This method is empty by default, meaning no action is performed.
     * Implementers can override this method if they want to handle key released events.
     *
     * @param e the KeyEvent containing details about the key event (such as the key code and the key state)
     */
    @Override
    default void keyReleased(KeyEvent e) {}
}
