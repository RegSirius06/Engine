package net.regsirius06.engine.API;

import net.regsirius06.engine.utils.help.InfoExecutable;

/**
 * Represents a task or process that can be executed within the system.
 *
 * <p>The {@code Executable} interface extends {@link Loadable}, meaning it can be loaded into the system
 * as part of a plugin. Implementations of this interface are expected to provide specific behavior
 * for execution within the application.</p>
 *
 * <p>All {@code Executable} tasks can be executed within the "Plugins" section of the main application.
 * A button labeled {@code Executables} will appear next to your plugin. When clicked, a window will open,
 * displaying buttons for each executable task. The button labels are taken from the {@link #name()} method,
 * while the corresponding actions are triggered by calling {@link #execute()}.</p>
 *
 * <p>Executable names are allowed to overlap with other tasks, if necessary, as the name serves merely
 * as an identifier. Additionally, the name {@code "About"} is reserved by the system and will create a special
 * button next to the plugin's name, which opens a page with information about the plugin.</p>
 *
 * <p>If you need to create an "About" button, you can easily do so by extending the {@link InfoExecutable}
 * abstract class, which provides a simple implementation for creating informational pages about the plugin.</p>
 *
 * <p>Plugins can implement this interface to provide custom executable tasks that can be triggered
 * or scheduled by the system. These tasks could range from simple operations to complex workflows.</p>
 *
 * @see Loadable
 * @see InfoExecutable
 */
public interface Executable extends Loadable {

    /**
     * Returns the name of this executable task.
     *
     * <p>This name can be used to identify the executable task within the system. It should be unique
     * within the plugin to avoid confusion with other tasks. However, multiple tasks may share the same name
     * if needed.</p>
     *
     * @return the name of the executable task
     */
    String name();

    /**
     * Executes the task or action defined by this executable.
     *
     * <p>This method contains the logic that is triggered when the executable is run.
     * The exact behavior depends on the plugin's implementation of this interface.</p>
     */
    void execute();
}
