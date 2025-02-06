package net.regsirius06.engine.core;

import net.regsirius06.engine.API.Core;
import net.regsirius06.engine.API.annotations.ImplementationOf;
import net.regsirius06.engine.API.annotations.ModId;
import net.regsirius06.engine.plugins.loaders.ExecutablesPluginLoader;
import net.regsirius06.engine.plugins.loaders.LabyrinthPanelsPluginLoader;
import net.regsirius06.engine.plugins.loaders.WallStatesPluginLoader;

/**
 * This class represents the core of the game plugin and serves as the foundation of the plugin system.
 * It implements the {@link Core} interface, providing access to various plugin loaders.
 *
 * <p>The {@code Base} class initializes the core components for managing wall states, executable components,
 * and labyrinth panels by creating instances of respective plugin loaders.</p>
 *
 * <p>The class is annotated with {@link ModId} to specify its unique identifier and {@link ImplementationOf}
 * to mark it as an implementation of the {@link Core} interface.</p>
 *
 * <p>By implementing the {@link Core} interface, the {@code Base} class offers the following loaders:</p>
 * <ul>
 *     <li>{@link WallStatesPluginLoader} - Manages the wall state components of the plugin.</li>
 *     <li>{@link ExecutablesPluginLoader} - Manages the executable components of the plugin.</li>
 *     <li>{@link LabyrinthPanelsPluginLoader} - Manages the labyrinth panels components of the plugin.</li>
 * </ul>
 *
 * @see Core
 * @see ModId
 * @see ImplementationOf
 */
@ModId("Base")
@ImplementationOf(Core.class)
public class Base implements Core {

    private final WallStatesPluginLoader wallStates;
    private final ExecutablesPluginLoader executables;
    private final LabyrinthPanelsPluginLoader labyrinthPanels;

    /**
     * Constructs the core game engine by initializing the plugin loaders for wall states,
     * executables, and labyrinth panels.
     */
    public Base() {
        wallStates = new WallStatesPluginLoader(this.getClass());
        executables = new ExecutablesPluginLoader(this.getClass());
        labyrinthPanels = new LabyrinthPanelsPluginLoader(this.getClass());
    }

    /**
     * Returns the loader responsible for managing wall states in the plugin.
     *
     * @return an instance of {@link WallStatesPluginLoader} or {@code null} if not implemented
     */
    @Override
    public WallStatesPluginLoader getWallStates() {
        return wallStates;
    }

    /**
     * Returns the loader responsible for managing executable components in the plugin.
     *
     * @return an instance of {@link ExecutablesPluginLoader} or {@code null} if not implemented
     */
    @Override
    public ExecutablesPluginLoader getExecutables() {
        return executables;
    }

    /**
     * Returns the loader responsible for managing labyrinth panel components in the plugin.
     *
     * @return an instance of {@link LabyrinthPanelsPluginLoader} or {@code null} if not implemented
     */
    @Override
    public LabyrinthPanelsPluginLoader getLabyrinthPanels() {
        return labyrinthPanels;
    }
}