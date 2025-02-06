package net.regsirius06.engine.plugins.files;

import net.regsirius06.engine.API.Core;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages plugin loading from the {@code plugins} directory.
 * <p>
 * This class provides functionality for scanning the {@code plugins} directory,
 * searching for {@code .jar} files, and loading them into a {@link URLClassLoader}.
 * It also provides functionality to load the current JAR file into a {@link URLClassLoader}.
 * </p>
 */
public final class PluginManager {

    /**
     * Logger instance for logging plugin management operations, including scanning for plugins, loading them,
     * and handling errors related to file operations.
     * <p>
     * This logger is used to record significant events during the plugin loading process, such as:
     * <ul>
     *   <li>Start and completion of plugin loading operations.</li>
     *   <li>Warnings when expected directories or files are not found or when there are issues creating directories.</li>
     *   <li>Errors encountered during scanning, loading, or converting plugin JAR files.</li>
     *   <li>Information about the number of plugins found in the plugins directory.</li>
     * </ul>
     * <p>
     * The logger uses the SLF4J logging framework, allowing the application to configure different log levels (e.g., INFO, WARN, ERROR)
     * and direct output to different logging sinks (console, files, etc.). This makes it easy to track the state of plugin loading during development,
     * debugging, and production deployment.
     * </p>
     * <p>
     * Example log outputs:
     * <ul>
     *   <li>Informational log when the plugin loading starts: "Plugin loading starts."</li>
     *   <li>Warning log when the plugins folder doesn't exist and is being created: "Plugins folder not found. Creating it now."</li>
     *   <li>Error log when a failure occurs while creating the plugins directory: "Failed to create plugins folder: {@code error-message}"</li>
     *   <li>Error log when a failure occurs while scanning for plugin JAR files: "Failed to scan plugin directory: {@code error-message}"</li>
     *   <li>Informational log when no plugins are found: "No plugins found in the plugins folder."</li>
     *   <li>Informational log when the plugin loading finishes: "Plugin loading finished."</li>
     * </ul>
     * <p>
     * Using this log, developers and administrators can track the success or failure of plugin loading and identify specific issues
     * related to missing plugins, directory creation, or file reading operations.
     * </p>
     */
    public static final Logger log = LoggerFactory.getLogger(PluginManager.class);

    private static final String PLUGINS_DIR = "plugins";

    // Private constructor to prevent instantiation of this class
    private PluginManager() {
    }

    /**
     * Retrieves a {@link URLClassLoader} that loads plugins from the {@code plugins} directory.
     * <p>
     * If the {@code plugins} directory does not exist, it will be created. The method then scans
     * the directory for {@code .jar} files and adds them to the {@link URLClassLoader}.
     * </p>
     *
     * @return a {@link URLClassLoader} containing plugins found in the {@code plugins} directory.
     */
    @Contract(" -> new")
    public static @NotNull URLClassLoader getPluginLoader() {
        List<URL> pluginUrls = new ArrayList<>();
        Path pluginsPath = Paths.get(PLUGINS_DIR);

        log.info("Plugin loading starts.");

        // If the plugins folder doesn't exist, attempt to create it
        if (!Files.exists(pluginsPath) || !Files.isDirectory(pluginsPath)) {
            log.warn("Plugins folder not found. Creating it now.");
            try {
                Files.createDirectories(pluginsPath);
            } catch (IOException e) {
                log.error("Failed to create plugins folder: {}", e.getMessage());
                return new URLClassLoader(pluginUrls.toArray(new URL[0]), PluginManager.class.getClassLoader());
            }
        }

        // Scan the folder for .jar files and add them to the list of URLs
        try {
            Files.walk(pluginsPath)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().toLowerCase().endsWith(".jar"))
                    .forEach(path -> {
                        try {
                            pluginUrls.add(path.toUri().toURL());
                        } catch (IOException e) {
                            log.error("Failed to convert plugin jar to URL: {}", path);
                            log.error(e.getMessage());
                        }
                    });
        } catch (IOException e) {
            log.error("Failed to scan plugin directory: {}", e.getMessage());
            return new URLClassLoader(pluginUrls.toArray(new URL[0]), PluginManager.class.getClassLoader());
        }

        // If no plugins are found, return an empty class loader
        if (pluginUrls.isEmpty()) {
            log.info("No plugins found in the plugins folder.");
            return new URLClassLoader(pluginUrls.toArray(new URL[0]), PluginManager.class.getClassLoader());
        }

        log.info("Plugin loading finished.");
        return new URLClassLoader(pluginUrls.toArray(new URL[0]), PluginManager.class.getClassLoader());
    }

    /**
     * Retrieves a {@link URLClassLoader} that loads the current JAR file.
     * <p>
     * This method is useful for loading the current application as a plugin.
     * </p>
     *
     * @param type the {@link Core} class used to retrieve the location of the JAR file.
     * @param <T> the type of {@link Core}.
     * @return a {@link URLClassLoader} for the current JAR file.
     * @throws RuntimeException if the current JAR file cannot be loaded.
     */
    @Contract("_ -> new")
    public static <T extends Core> @NotNull URLClassLoader getSelfLoader(@NotNull Class<T> type) throws RuntimeException {
        List<URL> pluginUrls = new ArrayList<>();

        log.info("Self loading starts.");
        try {
            pluginUrls.add(new File(type.getProtectionDomain().getCodeSource()
                    .getLocation().toURI()).toURI().toURL());
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to convert current jar to URL." + e.getMessage());
        }

        log.info("Self loading finished.");
        return new URLClassLoader(pluginUrls.toArray(new URL[0]), PluginManager.class.getClassLoader());
    }
}
