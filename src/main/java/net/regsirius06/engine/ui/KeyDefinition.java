package net.regsirius06.engine.ui;

import org.jetbrains.annotations.Nullable;

import java.awt.event.KeyEvent;
import java.util.EnumMap;

/**
 * Represents a mapping between actions and key codes for keyboard inputs.
 * This class allows defining and retrieving key bindings for various actions such as movement and interaction.
 *
 * It provides default key bindings for common actions like movement and minimap toggling.
 */
public class KeyDefinition {

    /**
     * A default set of key bindings, including keys for movement (W, A, S, D) and minimap (M).
     */
    public static final KeyDefinition DEFAULT = new KeyDefinition() {{
        setKey(Action.FORWARD_MOVE, KeyEvent.VK_W);
        setKey(Action.BACK_MOVE, KeyEvent.VK_S);
        setKey(Action.LEFT_ROTATION, KeyEvent.VK_A);
        setKey(Action.RIGHT_ROTATION, KeyEvent.VK_D);
        setKey(Action.MINIMAP, KeyEvent.VK_M);
    }};

    /**
     * Enumeration of possible actions that can be mapped to keys.
     */
    public enum Action {
        /**
         * Action for moving forward.
         */
        FORWARD_MOVE,

        /**
         * Action for moving backward.
         */
        BACK_MOVE,

        /**
         * Action for rotating left.
         */
        LEFT_ROTATION,

        /**
         * Action for rotating right.
         */
        RIGHT_ROTATION,

        /**
         * Action for toggling the minimap.
         */
        MINIMAP,

        /**
         * Action for getting light.
         */
        GET_LIGHT,

        /**
         * Action for setting light.
         */
        SET_LIGHT,

        /**
         * Action for making a forward choice.
         */
        CHOICE_FORWARD,

        /**
         * Action for making a backward choice.
         */
        CHOICE_BACK
    }

    private final EnumMap<Action, Integer> keyBindings;

    /**
     * Constructs a new {@link KeyDefinition} instance with no initial key bindings.
     */
    public KeyDefinition() {
        keyBindings = new EnumMap<>(Action.class);
    }

    /**
     * Retrieves the key code associated with a specific action.
     *
     * @param action the action whose key code is to be retrieved
     * @return the key code for the given action, or {@code null} if no key is assigned
     */
    public final @Nullable Integer getKey(Action action) {
        return keyBindings.get(action);
    }

    /**
     * Sets a key code for a specific action.
     *
     * @param action the action to associate with the key code
     * @param keyCode the key code to bind to the action (e.g., {@link KeyEvent#VK_W} for 'W' key)
     */
    public final void setKey(Action action, int keyCode) {
        keyBindings.put(action, keyCode);
    }
}
