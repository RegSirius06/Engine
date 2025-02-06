package net.regsirius06.engine.plugins.loaders;

import net.regsirius06.engine.API.Core;
import net.regsirius06.engine.API.Executable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

/**
 * A {@link PluginLoader} implementation for loading {@link Executable} plugins.
 * This class loads executable plugins for a specific core type and provides
 * additional functionality to filter and retrieve a special "About" executable.
 *
 * <p>It extends the {@link DefaultPluginLoader} to handle the loading of plugins
 * of type {@link Executable} and offers methods to retrieve all executables
 * as well as a specific "About" executable if available.</p>
 */
public final class ExecutablesPluginLoader extends DefaultPluginLoader<Executable> {

    /**
     * Constructs a new {@code ExecutablesPluginLoader} for the given core type.
     *
     * <p>This constructor initializes the loader and automatically loads the executables
     * using the {@link DefaultPluginLoader#loadPlugins()} method.</p>
     *
     * @param type the core type to associate with the loader
     */
    public ExecutablesPluginLoader(@NotNull Class<? extends Core> type) {
        super(Executable.class, type);
    }

    /**
     * Returns a list of all executable plugins, excluding the one with the name "About".
     *
     * <p>The "About" executable is excluded from this list as it serves a specific
     * purpose (typically displaying information about the plugin or application).</p>
     *
     * @return a list of executable plugins, excluding the "About" executable
     */
    @Override
    public List<Executable> getPlugins() {
        return super.getPlugins().stream().filter(e -> !Objects.equals(e.name(), "About")).toList();
    }

    /**
     * Retrieves the "About" executable plugin, if available.
     *
     * <p>This method returns the first plugin whose name is "About". If no such plugin
     * is found, it returns {@code null}.</p>
     *
     * @return the "About" executable plugin, or {@code null} if not found
     */
    public @Nullable Executable getAbout() {
        try {
            return super.getPlugins().stream().filter(e -> Objects.equals(e.name(), "About")).toList().get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
}
