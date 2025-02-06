package net.regsirius06.engine.world;

import net.regsirius06.engine.API.Initializer;
import net.regsirius06.engine.control.KeyDefinition;
import net.regsirius06.engine.control.KeyLogic;
import net.regsirius06.engine.utils.panels.AbstractLabyrinthPanel;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code World} class represents a game world, including a labyrinth and various attributes such as
 * the world size, lighting, and world-specific settings. It allows for the management of the world,
 * including saving and loading worlds from files.
 * <p>
 * A world is defined by the following attributes:
 * <ul>
 *     <li>{@code name} - the name of the world</li>
 *     <li>{@code width} - the width of the world</li>
 *     <li>{@code height} - the height of the world</li>
 *     <li>{@code dimension} - the dimensionality of the world (typically 2D or 3D)</li>
 *     <li>{@code quantityOfLight} - the amount of light in the world</li>
 *     <li>{@code seed} - the random seed used for generating the world</li>
 *     <li>{@code keyDefinition} - the key bindings for interacting with the world</li>
 *     <li>{@code labyrinth} - the labyrinth that represents the core of the world</li>
 * </ul>
 * <p>
 * The {@code World} class also provides functionality for saving and loading the world to/from a file,
 * and retrieving a list of all saved worlds. It provides methods to create a labyrinth with the given settings.
 */
public final class World implements Serializable {
    /**
     * The serial version UID for serialization.
     */
    @Serial
    private static final long serialVersionUID = -1L;

    /**
     * The logger for the {@code World} class, used to log messages related to world operations.
     * <p>
     * This {@link Logger} instance is responsible for logging events and errors related to the operations
     * performed on the {@code World} object, such as saving, loading, and other internal processes.
     * The logger is configured using the SLF4J logging framework, which allows for flexible logging output,
     * including console output, file logging, or integration with external logging systems.
     * <p>
     * Example usage:
     * <pre>
     * log.info("World has been successfully loaded.");
     * log.error("Failed to save the world: {}", e.getMessage());
     * </pre>
     * <p>
     * The logging level can be adjusted in the SLF4J configuration to control the verbosity of the log messages.
     * Common log levels include {@code DEBUG}, {@code INFO}, {@code WARN}, and {@code ERROR}.
     * By default, the log messages are output to the console, but this can be configured as needed.
     */
    public static final Logger log = LoggerFactory.getLogger(World.class);

    /**
     * The name of the world.
     * <p>
     * This is the unique identifier for the world, typically used for saving, loading, and referencing the world.
     */
    private final String name;

    /**
     * The width of the world.
     * <p>
     * This parameter defines how many units there are horizontally in the world. It is typically used to set
     * the width of the world’s grid or maze.
     */
    private final int width;

    /**
     * The height of the world.
     * <p>
     * This parameter defines how many units there are vertically in the world. It is typically used to set
     * the height of the world’s grid or maze.
     */
    private final int height;

    /**
     * The dimension of the world.
     * <p>
     * Defines the dimensionality of the world. Typically 2 for a 2D world and 3 for a 3D world, this
     * parameter can determine how the world is represented (e.g., grid-based, spatial arrangement, etc.).
     */
    private final int dimension;

    /**
     * The quantity of light sources in the world.
     * <p>
     * Defines how many light sources are present in the world, affecting visibility and lighting effects in the world.
     */
    private final int quantityOfLight;

    /**
     * The seed used for generating the world.
     * <p>
     * A random seed value used to generate the world content (such as the labyrinth) in a reproducible way.
     * Worlds generated with the same seed will be identical.
     */
    private final long seed;

    /**
     * The key definition used for interacting with the world.
     * <p>
     * <b>Deprecated:</b> This field is deprecated and may be removed in future versions. Please consider
     * using a more modern approach for input handling. This object contains the configuration of key bindings
     * for user interaction with the world, such as movement or actions within the world.
     */
    @Deprecated
    private final KeyDefinition keyDefinition;

    /**
     * The labyrinth initializer associated with the world.
     * <p>
     * This object is responsible for initializing and generating the labyrinth within the world using the
     * given world parameters such as width, height, dimension, light sources, and seed.
     */
    private final Initializer<? extends AbstractLabyrinthPanel> labyrinth;

    /**
     * Constructs a new {@code World} with the given parameters.
     *
     * @param name the name of the world
     * @param width the width of the world
     * @param height the height of the world
     * @param dimension the dimension of the world (e.g., 2D or 3D)
     * @param quantityOfLights the number of light sources in the world
     * @param seed the random seed for generating the world
     * @param keyDefinition the key bindings for interacting with the world
     * @param labyrinth the labyrinth initializer that is used to create the labyrinth in the world
     */
    public World(String name, int width, int height, int dimension,
                 int quantityOfLights, long seed, KeyDefinition keyDefinition,
                 @NotNull Initializer<? extends AbstractLabyrinthPanel> labyrinth) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.dimension = dimension;
        this.quantityOfLight = quantityOfLights;
        this.seed = seed;
        this.keyDefinition = keyDefinition;
        this.labyrinth = labyrinth;
    }

    /**
     * Returns the labyrinth panel associated with this world, initialized with the world-specific settings.
     *
     * @return an instance of the labyrinth panel
     */
    public AbstractLabyrinthPanel getLabyrinth() {
        return labyrinth.create(width, height, dimension, quantityOfLight, seed, new KeyLogic(keyDefinition));
    }

    /**
     * Returns the name of this world.
     *
     * @return the name of the world
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the width of the world.
     *
     * @return the width of the world
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the world.
     *
     * @return the height of the world
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the path to the file where this world is saved, based on the world name.
     *
     * @param name the name of the world
     * @return the file path where the world is saved
     */
    @Contract(pure = true)
    public static @NotNull String getPath(String name) {
        return "./worlds/" + name + ".ser";
    }

    /**
     * Returns the path to the file where this world is saved.
     *
     * @return the file path where this world is saved
     */
    @Contract(pure = true)
    public @NotNull String getPath() {
        return getPath(name);
    }

    /**
     * Saves this world to a file at the specified path.
     */
    public void save() {
        File folder = new File("./worlds/");
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                log.error("Failed to create folder: {}", folder.getAbsolutePath());
                return;
            }
        }

        try (FileOutputStream file = new FileOutputStream(this.getPath())) {
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(this);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Loads a world from the file system using the given world name.
     *
     * @param name the name of the world to load
     * @return the loaded {@code World} instance, or {@code null} if the world could not be loaded
     */
    public static @Nullable World load(@NotNull String name) {
        try (FileInputStream file = new FileInputStream(getPath(name))) {
            ObjectInputStream in = new ObjectInputStream(file);
            return (World) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * Returns a list of names of all saved worlds.
     *
     * @return a list of saved world names
     */
    public static @NotNull List<String> getSavedWorlds() {
        File folder = new File("./worlds/");
        List<String> worldNames = new ArrayList<>();

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles((dir, name) -> name.endsWith(".ser"));
            if (files != null) {
                for (File file : files) {
                    String worldName = file.getName().replace(".ser", "");
                    worldNames.add(worldName);
                }
            }
        }
        return worldNames.stream().toList();
    }
}
