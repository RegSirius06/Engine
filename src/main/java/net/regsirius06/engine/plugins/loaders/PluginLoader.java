package net.regsirius06.engine.plugins.loaders;

import net.regsirius06.engine.API.Loadable;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Defines the contract for plugin loaders that load and manage plugins of type {@link Loadable}.
 *
 * <p>Implementations of this interface are responsible for loading plugins into the system, managing
 * a collection of loaded plugins, and providing access to individual plugins by their identifiers.</p>
 *
 * <p>Each plugin loader is designed to work with a specific type of {@link Loadable} plugin, and
 * ensures that plugins of this type are properly loaded and accessible for the application.</p>
 *
 * @param <T> the type of plugin that this loader handles, which extends {@link Loadable}
 */
public interface PluginLoader<T extends Loadable> {

    /**
     * Loads all plugins of type {@code T} into the system.
     *
     * <p>This method is responsible for discovering, loading, and initializing all plugins that
     * are compatible with this loader.</p>
     */
    void loadPlugins();

    /**
     * Returns a list of all loaded plugins.
     *
     * <p>This method retrieves all plugins that have been successfully loaded by the loader.</p>
     *
     * @return a list of plugins of type {@code T}
     */
    List<T> getPlugins();

    /**
     * Retrieves a specific plugin by its unique identifier.
     *
     * <p>This method allows access to a single plugin by its ID, which is typically the fully qualified
     * class name obtained via {@link Object#getClass()}.{@link Class#getName()}. If the plugin with the specified
     * ID is not found, it returns {@code null}.</p>
     *
     * @param id the unique identifier for the plugin, usually the fully qualified class name
     * @return the plugin associated with the given ID, or {@code null} if not found
     */
    @Nullable T getPlugin(String id);
}
