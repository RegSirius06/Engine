package net.regsirius06.engine.plugins.loaders;

import net.regsirius06.engine.API.Core;
import net.regsirius06.engine.API.Loadable;
import net.regsirius06.engine.API.annotations.AddTo;
import net.regsirius06.engine.API.annotations.ModId;
import net.regsirius06.engine.plugins.files.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;

/**
 * A default implementation of the {@link PluginLoader} interface that provides
 * basic plugin loading functionality for plugins of type {@link Loadable}.
 *
 * <p>This class loads plugins of a specific type using the {@link ServiceLoader} mechanism,
 * both from the system's service provider and from additional plugins specified via the
 * {@link AddTo} annotation. It also manages the list of loaded plugins and provides
 * access to them by their unique class name.</p>
 *
 * <p>The loader ensures that plugins are loaded for a specific core type,
 * which is provided as a parameter during initialization. This class also logs
 * relevant information during the plugin loading process using the SLF4J logging framework.</p>
 *
 * @param <T> the type of plugin that this loader manages, extending {@link Loadable}
 */
public abstract class DefaultPluginLoader<T extends Loadable> implements PluginLoader<T> {

    /**
     * Logger instance for logging plugin loading operations and errors.
     *
     * <p>This field uses the SLF4J logging framework to record important events during
     * the plugin loading process. It logs information about successfully loaded plugins
     * as well as error messages when something goes wrong, such as when no plugins are found
     * or a specific plugin cannot be retrieved.</p>
     *
     * <p>The log is used throughout the plugin loading lifecycle to provide visibility
     * into which plugins were successfully loaded and whether any issues occurred.</p>
     *
     * <p>The log messages can be accessed for debugging or monitoring purposes and can
     * be configured with different logging levels (e.g., INFO, ERROR, WARN) depending on
     * the situation.</p>
     */
    public static final Logger log = LoggerFactory.getLogger(DefaultPluginLoader.class);

    private final List<T> plugins = new ArrayList<>();
    private final Class<T> type;
    private final Class<? extends Core> coreType;

    /**
     * Constructs a new {@code DefaultPluginLoader} for the given plugin type and core type.
     *
     * <p>This constructor initializes the loader and automatically loads the plugins
     * using the {@link #loadPlugins()} method.</p>
     *
     * @param type the type of plugin to load
     * @param core the core type to associate with the loader
     */
    public DefaultPluginLoader(@NotNull Class<T> type, @NotNull Class<? extends Core> core) {
        this.type = type;
        this.coreType = core;
        this.loadPlugins();
    }

    /**
     * Loads all available plugins of type {@code T} using the {@link ServiceLoader} mechanism.
     *
     * <p>This method first clears any previously loaded plugins, then loads plugins from
     * the service provider configured for the core type. It also loads plugins that are
     * marked with the {@link AddTo} annotation and belong to the same core as the current plugin type.</p>
     *
     * <p>After loading, the method logs the names of the loaded plugin classes using SLF4J.</p>
     */
    @Override
    public void loadPlugins() {
        if (!plugins.isEmpty()) plugins.clear();

        // Load plugins via ServiceLoader for the core type
        ServiceLoader<T> loader = ServiceLoader.load(type, PluginManager.getSelfLoader(coreType));
        loader.forEach(this.plugins::add);

        // Load additional plugins from the PluginManager that are marked with AddTo
        ServiceLoader<T> addToLoader = ServiceLoader.load(type, PluginManager.getPluginLoader());
        addToLoader.forEach(plugin -> {
            AddTo addTo = plugin.getClass().getAnnotation(AddTo.class);
            if (addTo == null) return;
            if (Objects.equals(addTo.value(), coreType.getAnnotation(ModId.class).value())) {
                this.plugins.add(plugin);
            }
        });

        // Log loaded plugins
        for (T plugin : plugins) {
            log.info("Loaded plugin: {}", plugin.getClass().getName());
        }
    }

    /**
     * Returns the list of all currently loaded plugins.
     *
     * @return a list of plugins of type {@code T}
     */
    @Override
    public List<T> getPlugins() {
        return plugins;
    }

    /**
     * Retrieves a specific plugin by its unique class name.
     *
     * <p>The plugin is identified by its fully qualified class name, which is usually
     * obtained via {@link Class#getName()}.</p>
     *
     * @param id the fully qualified class name of the plugin to retrieve
     * @return the plugin associated with the given class name, or {@code null} if not found
     */
    @Override
    public @Nullable T getPlugin(String id) {
        if (this.plugins.isEmpty()) {
            log.error("No available plugins.");
            return null;
        }
        for (T plugin : this.plugins) {
            if (plugin.getClass().getName().equals(id)) {
                return plugin;
            }
        }
        log.error("Plugin \"{}\" not found.", id);
        return null;
    }
}
