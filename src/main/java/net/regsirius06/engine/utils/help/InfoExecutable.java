package net.regsirius06.engine.utils.help;

import net.regsirius06.engine.API.Executable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * An abstract class that provides a base implementation for creating an {@code "About"} informational dialog for plugins.
 * This class implements the {@link Executable} interface, providing the logic for displaying information about
 * the plugin, including details like its description, version, authors, dependencies, and contribution instructions.
 * <p>
 * The {@link #execute()} method shows a modal dialog containing formatted information about the plugin,
 * while the actual content (description, version, authors, etc.) must be provided by subclasses
 * through abstract methods.
 * </p>
 * <p>
 * This class is particularly useful for adding an {@code "About"} button to a plugin, allowing users to view
 * detailed information about the plugin when clicked.
 * </p>
 *
 * @see Executable
 * @see net.regsirius06.engine.plugins.loaders.ExecutablesPluginLoader
 */
public abstract class InfoExecutable implements Executable {
    private static final String border = " ".repeat(10);

    /**
     * Returns the name of this executable task.
     *
     * <p>The name is used to identify this executable within the system. For the {@link InfoExecutable} class,
     * the name will always be "About", as it is specifically for displaying plugin information.</p>
     *
     * @return the name of the executable task ({@code "About"})
     */
    @Override
    public String name() {
        return "About";
    }

    /**
     * Executes the task of displaying the plugin information in a dialog box.
     *
     * <p>This method creates and shows a dialog with information about the plugin, including details such as
     * description, version, authors, dependencies, and contribution instructions.</p>
     * <p>The content of the dialog is populated using the abstract methods defined in this class, which must be
     * implemented by subclasses to provide the actual plugin-specific information.</p>
     *
     * @see JOptionPane#showMessageDialog(Component, Object, String, int)
     */
    @Override
    public void execute() {
        String pluginInfo = getText();

        JTextArea textArea = new JTextArea(pluginInfo);
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setBackground(new Color(33, 33, 33));
        textArea.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 300));

        JOptionPane.showMessageDialog(null, scrollPane, "Plugin Information", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Retrieves the formatted text to be displayed in the plugin information dialog.
     *
     * <p>This method assembles the plugin information (description, version, authors, etc.) into a formatted
     * string, which is then displayed in the dialog box. The actual content of each section is provided
     * by the abstract methods that must be implemented by subclasses.</p>
     *
     * @return a formatted string containing the plugin's information
     */
    private @NotNull String getText() {
        StringBuilder text = new StringBuilder();
        text.append("Plugin Information:\n\n");
        text.append("Description:\n").append(border).append(format(description())).append("\n\n");
        text.append("Version:\n").append(border).append(format(version())).append("\n\n");
        text.append("Authors:\n").append(border)
                .append(String.join(";\n" + border, authors())).append("\n\n");
        text.append("Dependencies:\n").append(border)
                .append(String.join(";\n" + border, dependencies())).append("\n\n");
        text.append("Contribute:\n").append(border).append(format(contribute())).append("\n");

        return text.toString();
    }

    /**
     * Formats a given string by adding a border of spaces between lines.
     *
     * <p>This method splits the input text into lines and adds a specified border (i.e., ten spaces)
     * between each line to create a visually distinct format for the information.</p>
     *
     * @param text the text to format
     * @return the formatted text with borders between lines
     */
    @Contract("_ -> new")
    private static @NotNull String format(@NotNull String text) {
        return String.join("\n" + border, text.split("\n"));
    }

    /**
     * Returns the authors of the plugin.
     *
     * <p>This method must be implemented by subclasses to return the list of authors of the plugin.</p>
     *
     * @return a list of authors
     */
    protected abstract List<String> authors();

    /**
     * Returns the description of the plugin.
     *
     * <p>This method must be implemented by subclasses to return a description of the plugin.</p>
     *
     * @return a description of the plugin
     */
    protected abstract String description();

    /**
     * Returns the version of the plugin.
     *
     * <p>This method must be implemented by subclasses to return the version of the plugin.</p>
     *
     * @return the version of the plugin
     */
    protected abstract String version();

    /**
     * Returns the dependencies of the plugin.
     *
     * <p>This method must be implemented by subclasses to return the list of dependencies of the plugin.</p>
     *
     * @return a list of dependencies
     */
    protected abstract List<String> dependencies();

    /**
     * Returns the contribution instructions for the plugin.
     *
     * <p>This method must be implemented by subclasses to return the contribution guidelines for the plugin.</p>
     *
     * @return the contribution instructions for the plugin
     */
    protected abstract String contribute();
}
