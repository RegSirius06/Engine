package net.regsirius06.engine.API;

import net.regsirius06.engine.plugins.loaders.ExecutablesPluginLoader;
import net.regsirius06.engine.plugins.loaders.LabyrinthPanelsPluginLoader;
import net.regsirius06.engine.plugins.loaders.PluginsLoader;
import net.regsirius06.engine.plugins.loaders.WallStatesPluginLoader;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the core of a plugin, providing access to various class loaders.
 *
 * <p>The {@code Core} interface extends {@link Loadable}, meaning it can be loaded into the system
 * as a plugin entry point. Implementations of this interface typically serve as the foundation of
 * a plugin, offering methods to retrieve loaders for different types of plugin components.</p>
 *
 * <p>Since plugins may not provide all types of components, these methods return {@code null} by default.
 * Plugin developers should override only the methods relevant to their plugin.</p>
 *
 * @see Loadable
 * @see net.regsirius06.engine.API.annotations.ModId
 * @see PluginsLoader
 */
public interface Core extends Loadable {

    /**
     * Returns the loader responsible for managing wall states in the plugin.
     *
     * <p>If the plugin does not provide custom wall states, this method returns {@code null}.</p>
     *
     * @return an instance of {@link WallStatesPluginLoader} or {@code null} if not implemented
     */
    default WallStatesPluginLoader getWallStates() {
        return null;
    }

    /**
     * Returns the loader responsible for managing executable components in the plugin.
     *
     * <p>If the plugin does not provide executable components, this method returns {@code null}.</p>
     *
     * @return an instance of {@link ExecutablesPluginLoader} or {@code null} if not implemented
     */
    default ExecutablesPluginLoader getExecutables() {
        return null;
    }

    /**
     * Returns the loader responsible for managing labyrinth panel components in the plugin.
     *
     * <p>If the plugin does not provide labyrinth panels, this method returns {@code null}.</p>
     *
     * @return an instance of {@link LabyrinthPanelsPluginLoader} or {@code null} if not implemented
     */
    default LabyrinthPanelsPluginLoader getLabyrinthPanels() {
        return null;
    }

    /**
     * Returns the name of the plugin.
     *
     * <p>The plugin name can be automatically extracted from the {@link net.regsirius06.engine.API.annotations.ModId}
     * annotation using {@link PluginsLoader#getPluginName(Core)}.</p>
     *
     * @return the plugin name as a {@code String}
     */
    default @NotNull String getName() {
        return PluginsLoader.getPluginName(this);
    }
}
