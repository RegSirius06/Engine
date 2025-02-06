package net.regsirius06.engine.control;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.KeyEvent;
import java.io.*;
import java.util.EnumMap;

/**
 * This class represents a mapping between actions and key codes for keyboard inputs.
 * It allows defining, retrieving, and saving key bindings for various actions such as movement, rotation, and minimap toggling.
 * <p>
 * The class provides default key bindings for common actions like movement (W, A, S, D) and minimap (M).
 * It also allows saving and loading the key bindings to and from a file.
 * </p>
 *
 * @deprecated This class is deprecated. Consider using alternative key-binding management systems.
 */
@Deprecated
public class KeyDefinition implements Serializable {
    @Serial
    private static final long serialVersionUID = -1L;

    /**
     * Logger instance for logging errors and warnings related to key bindings.
     *
     * <p>This logger is used to report issues when saving or loading key bindings,
     * ensuring that errors are recorded without interrupting program execution.</p>
     */
    public static final Logger log = LoggerFactory.getLogger(KeyDefinition.class);

    /**
     * A default set of key bindings, including keys for movement (W, A, S, D) and minimap (M).
     * This is a pre-configured instance of KeyDefinition used when no custom key bindings are defined.
     */
    private static final KeyDefinition DEFAULT = new KeyDefinition() {{
        setKey(Action.FORWARD_MOVE, KeyEvent.VK_W);
        setKey(Action.BACK_MOVE, KeyEvent.VK_S);
        setKey(Action.LEFT_ROTATION, KeyEvent.VK_A);
        setKey(Action.RIGHT_ROTATION, KeyEvent.VK_D);
        setKey(Action.MINIMAP, KeyEvent.VK_M);
    }};

    /**
     * Enumeration of possible actions that can be mapped to keys.
     * This enum defines all the actions that can be triggered by specific key bindings.
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

    /**
     * A mapping between actions and their corresponding key codes.
     *
     * <p>This map stores key bindings for various actions, allowing customization
     * of keyboard controls. Keys are stored as integer values representing
     * {@link KeyEvent} codes.</p>
     */
    private final EnumMap<Action, Integer> keyBindings;

    /**
     * Constructs a new {@link KeyDefinition} instance with no initial key bindings.
     * This constructor initializes an empty set of key bindings that can later be configured by the user.
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

    /**
     * Saves the current key bindings to a file named "Settings.ser".
     *
     * <p>The key bindings are serialized and written to the file for persistent storage.
     * If an error occurs during the saving process, a log entry is created instead of
     * throwing an exception.</p>
     */
    public final void save() {
        try (FileOutputStream file = new FileOutputStream("Settings.ser")) {
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(this);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Loads the key bindings from a file named "Settings.ser".
     *
     * <p>If the file is found and successfully read, the stored key bindings are loaded.
     * If the file is missing, corrupted, or cannot be read, the default key bindings are returned.
     * Errors are logged but do not interrupt execution.</p>
     *
     * @return the loaded {@link KeyDefinition} instance with key bindings,
     *         or the default key bindings if loading fails
     */
    public static KeyDefinition load() {
        try (FileInputStream file = new FileInputStream("Settings.ser")) {
            ObjectInputStream in = new ObjectInputStream(file);
            return (KeyDefinition) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.warn(e.getMessage());
        }
        return DEFAULT;
    }
}
