package net.regsirius06.engine.tasks;

import net.regsirius06.engine.plugins.files.PluginDataProvider;
import org.jetbrains.annotations.NotNull;

/**
 * A utility class for initializing plugin data files required for loading plugins in the application.
 * <p>
 * The {@link #main(String[])} method is the entry point for creating the necessary files
 * for plugin data based on the provided root directory.
 * If a root directory is provided as an argument, it will be used to create the plugin data files;
 * otherwise, the default directory is used.
 * </p>
 * <p>
 * This class leverages {@link PluginDataProvider} to handle the creation of required data files
 * that enable the application to load and manage plugins.
 * </p>
 */
public class InitPlugins {

    /**
     * The main method for creating plugin data files required for the application.
     * The method checks if a root directory is provided as an argument. If so, the data files will
     * be created in that directory; otherwise, the default location will be used.
     *
     * @param args command-line arguments where:
     *             - args[0] is the optional root directory for storing plugin data files.
     * @throws RuntimeException if there are issues creating the data files.
     */
    public static void main(String @NotNull [] args) {
        String root = (args.length == 1) ? args[0] : null;
        PluginDataProvider.of(root).createDataFiles();
    }
}
