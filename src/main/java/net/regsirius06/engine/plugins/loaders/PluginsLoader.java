package net.regsirius06.engine.plugins.loaders;

import net.regsirius06.engine.API.Core;
import net.regsirius06.engine.API.annotations.ModId;
import net.regsirius06.engine.core.Base;
import net.regsirius06.engine.plugins.files.PluginManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * A concrete implementation of the {@link PluginLoader} interface that loads {@link Core} plugins.
 *
 * <p>This class is responsible for loading all available plugins of type {@link Core}, including ensuring
 * that plugins are uniquely identified by the {@link ModId} annotation. The loader also provides
 * convenient methods to retrieve plugins by their {@code ModId}, validate the loaded plugins for
 * uniqueness, and fetch a specific base plugin.</p>
 *
 * <p>The loader can be accessed through the static instance {@link #PLUGINS}, which is the recommended
 * way to interact with the plugins system.</p>
 */
public final class PluginsLoader implements PluginLoader<Core> {

    /**
     * Logger instance for logging plugin management operations, including loading plugins, validating them,
     * and handling errors such as missing or duplicate {@link ModId} annotations.
     * <p>
     * This logger is used to record key events during the plugin loading process, such as:
     * <ul>
     *   <li>Start and completion of plugin loading.</li>
     *   <li>Successful loading of individual plugins.</li>
     *   <li>Warnings about missing or duplicate {@link ModId} annotations.</li>
     *   <li>Errors encountered during the plugin validation or loading process.</li>
     * </ul>
     * <p>
     * The logger uses the SLF4J framework, allowing flexibility in configuring log levels (e.g., INFO, WARN, ERROR)
     * and directing output to different destinations (console, log files, etc.). This ensures that developers and administrators
     * can track the state of plugin loading and identify issues with the plugins at runtime.</p>
     * <p>
     * Example log outputs:
     * <ul>
     *   <li>Informational log when a plugin is successfully loaded: "Loaded plugin: {@code plugin-class-name}"</li>
     *   <li>Warning log when a plugin with a specified {@link ModId} is not found: "Plugin \"{@code id}\" not found."</li>
     *   <li>Error log when a plugin fails the validation check for missing or duplicate {@link ModId}: "Duplicate MOD_ID: {@code modId}"</li>
     * </ul>
     * <p>
     * By using this logger, developers and administrators can gain insight into the plugin loading process,
     * making it easier to debug issues related to missing or incorrectly annotated plugins.</p>
     */
    public static final Logger log = LoggerFactory.getLogger(PluginsLoader.class);

    /**
     * A static instance of {@code PluginsLoader} for easy access to the loaded plugins.
     * This is the recommended object to interact with for plugin management.
     */
    public static PluginsLoader PLUGINS = new PluginsLoader();

    private final List<Core> plugins = new ArrayList<>();

    /**
     * Private constructor to initialize the {@code PluginsLoader} and load plugins immediately.
     * This constructor is invoked when the {@link #PLUGINS} instance is created.
     */
    private PluginsLoader() {
        this.loadPlugins();
    }

    /**
     * Loads all available {@link Core} plugins using the {@link ServiceLoader} mechanism.
     * <p>
     * This method clears any previously loaded plugins and then loads plugins from the
     * plugin loader provided by {@link PluginManager}. After loading, it validates that the
     * plugins are properly annotated and that there are no duplicate {@link ModId}s.
     * </p>
     */
    @Override
    public void loadPlugins() {
        plugins.clear();
        ServiceLoader<Core> loader = ServiceLoader.load(Core.class, PluginManager.getPluginLoader());
        loader.forEach(this.plugins::add);
        validate(this);
        for (Core core : plugins) {
            log.info("Loaded plugin: {}", core.getClass().getName());
        }
    }

    /**
     * Returns the list of all currently loaded {@link Core} plugins.
     *
     * @return a list of plugins of type {@code Core}
     */
    @Override
    public List<Core> getPlugins() {
        return plugins;
    }

    /**
     * Retrieves the base plugin, which is identified by its {@link ModId} annotation matching
     * the {@link Base} class.
     *
     * <p>This method utilizes {@link #getPlugin(String)} to retrieve the plugin by its {@code ModId} value,
     * ensuring that the base plugin is correctly identified by its {@link ModId} annotation.</p>
     *
     * @return the base plugin
     * @throws RuntimeException if no base plugin is found
     */
    public @NotNull Core getBase() {
        Core basePlugin = this.getPlugin(Base.class.getAnnotation(ModId.class).value());
        if (basePlugin == null) {
            throw new RuntimeException("Failed to load base plugin!");
        }
        return basePlugin;
    }


    /**
     * Retrieves a specific plugin by its {@link ModId}.
     * <p>
     * The plugin is identified by the {@code ModId} annotation, which is used as the unique
     * identifier for each plugin. If no plugin with the specified ID is found, {@code null}
     * is returned.
     * </p>
     *
     * @param id the {@code ModId} value to look for
     * @return the plugin associated with the given ID, or {@code null} if not found
     */
    @Override
    public @Nullable Core getPlugin(String id) {
        for (Core core : plugins) {
            ModId plugin = core.getClass().getAnnotation(ModId.class);
            if (!Objects.equals(plugin.value(), id)) {
                continue;
            }
            return core;
        }
        log.warn("Plugin \"{}\" not found.", id);
        return null;
    }

    /**
     * Retrieves the plugin name from the {@link ModId} annotation of the provided plugin instance.
     *
     * <p>This method extracts the {@code value} of the {@link ModId} annotation from the plugin class,
     * which is expected to be the unique identifier of the plugin. The plugin name should match the value
     * specified in the {@link ModId} annotation on the plugin class.</p>
     *
     * <p>The method is marked as {@code pure}, meaning it does not have any side effects and always returns
     * the same result for the same input.</p>
     *
     * @param plugin the plugin instance from which the name is extracted
     * @param <C> the type of the plugin, which must extend {@link Core}
     * @return the plugin name as a {@code String}, which corresponds to the {@code value} of the {@link ModId} annotation
     * @throws NullPointerException if the provided {@code plugin} does not have the {@link ModId} annotation
     * @see ModId
     */
    @Contract(pure = true)
    public static <C extends Core> String getPluginName(@NotNull C plugin) {
        return plugin.getClass().getAnnotation(ModId.class).value();
    }

    /**
     * Validates that all loaded plugins are correctly annotated with {@link ModId} and that
     * each {@code ModId} is unique across all plugins.
     *
     * @param pluginsLoader the current instance of {@link PluginsLoader} containing the plugins to validate
     * @throws IllegalArgumentException if any plugin is not annotated with {@link ModId}
     * @throws IllegalStateException if any duplicate {@link ModId}s are found
     */
    private static void validate(@NotNull PluginsLoader pluginsLoader) {
        Set<String> IDs = new HashSet<>();
        for (Class<? extends Core> core : pluginsLoader.plugins.stream().map(Core::getClass).toList()) {
            ModId plugin = core.getAnnotation(ModId.class);
            if (plugin == null) {
                throw new IllegalArgumentException("Class " + core.getName() + " is not annotated with @ModId");
            }

            String modIdValue = plugin.value();
            if (!IDs.add(modIdValue)) {
                throw new IllegalStateException("Duplicate MOD_ID: " + modIdValue);
            }
        }
    }
}
