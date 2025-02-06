package net.regsirius06.engine.plugins.loaders;

import net.regsirius06.engine.utils.panels.AbstractLabyrinthPanel;
import net.regsirius06.engine.API.Core;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link PluginLoader} implementation for loading {@link AbstractLabyrinthPanel} initializers.
 * This class extends {@link InitializersPluginLoader} to load plugins of type {@link AbstractLabyrinthPanel}
 * for rendering or managing labyrinth panels in the engine.
 *
 * <p>This loader loads and manages the initialization of labyrinth panel components, which can
 * be used for displaying or manipulating the visual representation of a labyrinth.</p>
 */
public final class LabyrinthPanelsPluginLoader extends InitializersPluginLoader<AbstractLabyrinthPanel> {

    /**
     * Constructs a new {@code LabyrinthPanelsPluginLoader} for the given core type.
     *
     * <p>This constructor initializes the loader and automatically loads the labyrinth panel initializers
     * using the {@link InitializersPluginLoader#loadPlugins()} method.</p>
     *
     * @param core the core type to associate with the loader
     */
    public LabyrinthPanelsPluginLoader(@NotNull Class<? extends Core> core) {
        super(AbstractLabyrinthPanel.class, core);
    }
}
