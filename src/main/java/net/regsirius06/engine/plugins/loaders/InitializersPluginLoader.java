package net.regsirius06.engine.plugins.loaders;

import net.regsirius06.engine.API.Core;
import net.regsirius06.engine.API.Initializer;
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
 * A plugin loader implementation for loading {@link Initializer} plugins of a specific type {@code T}.
 *
 * <p>This class uses the {@link ServiceLoader} mechanism to load plugins of type {@link Initializer}
 * for a specified type {@code T}, both from the system's service provider and from additional plugins
 * specified via the {@link AddTo} annotation. It ensures that plugins are loaded for a specific core type
 * provided during initialization.</p>
 *
 * <p>The loader is capable of filtering plugins that are compatible with the type {@code T} and ensures
 * that plugins with the matching type are loaded into the system. The loader also logs information about
 * the loaded plugins using the SLF4J logging framework.</p>
 *
 * @param <T> the type of objects that this initializer will create or manage
 */
public abstract class InitializersPluginLoader<T> implements PluginLoader<Initializer<T>> {

    /**
     * Logger instance for logging plugin loading operations, errors, and status updates.
     * <p>
     * This logger is used to record key events in the plugin loading lifecycle, including:
     * <ul>
     *   <li>The successful loading of {@link Initializer} plugins, with their class names being logged as informational messages.</li>
     *   <li>Errors related to plugin loading, such as when no plugins are available or when a specific plugin cannot be found by its class name.</li>
     * </ul>
     * <p>
     * The logger uses the SLF4J logging framework, allowing for flexible logging configurations, such as different logging levels (INFO, ERROR, etc.), output destinations, and formats. This makes it suitable for both development and production environments.
     * </p>
     * <p>
     * Example log outputs:
     * <ul>
     *   <li>Informational log when a plugin is successfully loaded: "Loaded class: {@code plugin-class-name}"</li>
     *   <li>Error log when no plugins are available: "No available plugins."</li>
     *   <li>Error log when a plugin is not found by its class name: "Plugin \"{@code plugin-class-name}\" not found."</li>
     * </ul>
     * <p>
     * Using this log, developers can track which plugins are loaded, debug issues with missing plugins, and ensure
     * that the plugin system operates as expected during runtime.
     * </p>
     */
    public static final Logger log = LoggerFactory.getLogger(InitializersPluginLoader.class);

    private final List<Initializer<T>> plugins = new ArrayList<>();
    private final Class<T> type;
    private final Class<? extends Core> coreType;

    /**
     * Constructs a new {@code InitializersPluginLoader} for the given initializer type and core type.
     *
     * <p>This constructor initializes the loader and automatically loads the plugins
     * using the {@link #loadPlugins()} method.</p>
     *
     * @param initializerType the type of initializer to load
     * @param coreType the core type to associate with the loader
     */
    public InitializersPluginLoader(@NotNull Class<T> initializerType, @NotNull Class<? extends Core> coreType) {
        this.coreType = coreType;
        this.type = initializerType;
        this.loadPlugins();
    }

    /**
     * Loads all available {@link Initializer} plugins of type {@code T} using the {@link ServiceLoader} mechanism.
     *
     * <p>This method first clears any previously loaded plugins, then loads plugins from the service provider
     * configured for the core type. It also loads plugins that are marked with the {@link AddTo} annotation and
     * belong to the same core as the current plugin type.</p>
     *
     * <p>After loading, the method logs the names of the loaded plugin classes using SLF4J.</p>
     */
    @SuppressWarnings(value = "unchecked")
    @Override
    public void loadPlugins() {
        if (!plugins.isEmpty()) plugins.clear();

        // Load initializers via ServiceLoader for the core type
        ServiceLoader<Initializer> loader = ServiceLoader.load(Initializer.class, PluginManager.getSelfLoader(coreType));
        loader.forEach(plugin -> {
            if (type.isAssignableFrom(plugin.getType())) {
                plugins.add((Initializer<T>) plugin);
            }
        });

        // Load additional initializers from the PluginManager
        ServiceLoader<Initializer> addToLoader = ServiceLoader.load(Initializer.class, PluginManager.getPluginLoader());
        addToLoader.forEach(plugin -> {
            AddTo addTo = plugin.getClass().getAnnotation(AddTo.class);
            if (addTo == null) return;
            if (Objects.equals(addTo.value(), coreType.getAnnotation(ModId.class).value())) {
                if (type == plugin.getType()) {
                    plugins.add((Initializer<T>) plugin);
                }
            }
        });

        // Log loaded initializers
        for (Initializer<T> plugin : plugins) {
            log.info("Loaded class: {}", plugin.getClass().getName());
        }
    }

    /**
     * Returns the list of all currently loaded {@link Initializer} plugins.
     *
     * @return a list of plugins of type {@code Initializer<T>}
     */
    @Override
    public List<Initializer<T>> getPlugins() {
        return plugins;
    }

    /**
     * Retrieves a specific initializer plugin by its unique class name.
     *
     * <p>The plugin is identified by its fully qualified class name, which is usually
     * obtained via {@link Class#getName()}.</p>
     *
     * @param id the fully qualified class name of the initializer plugin to retrieve
     * @return the initializer plugin associated with the given class name, or {@code null} if not found
     */
    @Override
    public @Nullable Initializer<T> getPlugin(String id) {
        if (this.plugins.isEmpty()) {
            log.error("No available plugins.");
            return null;
        }
        for (Initializer<T> t : this.plugins) {
            if (t.getClass().getName().equals(id)) {
                return t;
            }
        }
        log.error("Plugin \"{}\" not found.", id);
        return null;
    }
}
