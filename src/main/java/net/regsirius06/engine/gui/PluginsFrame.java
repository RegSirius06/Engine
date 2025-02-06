package net.regsirius06.engine.gui;

import net.regsirius06.engine.API.Core;
import net.regsirius06.engine.API.Executable;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import static net.regsirius06.engine.plugins.loaders.PluginsLoader.PLUGINS;

/**
 * The {@code PluginsFrame} class represents a window for managing plugins within
 * the application. It displays a list of available plugins and provides options
 * to view their commands and additional information.
 */
public class PluginsFrame extends MainMenu.Child {

    /**
     * Constructs a new {@code PluginsFrame} to display available plugins and
     * their corresponding actions (e.g., commands and about info).
     */
    public PluginsFrame() {
        super();
        setTitle("Plugins");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(33, 33, 33));

        List<Core> plugins = PLUGINS.getPlugins();

        // If no plugins are available, show a message
        if (plugins.isEmpty()) {
            JLabel noPluginsLabel = new JLabel("No plugins available.");
            noPluginsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            noPluginsLabel.setForeground(Color.WHITE);
            panel.add(noPluginsLabel);
        } else {
            // Create a panel for each available plugin
            for (Core plugin : plugins) {
                panel.add(createPluginPanel(plugin));
            }
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(new Color(33, 33, 33));
        getContentPane().add(scrollPane);
    }

    /**
     * Creates a panel for a given plugin that includes options to view its
     * commands and additional information (if available).
     *
     * @param plugin The {@link Core} plugin for which to create the panel.
     * @return A {@code JPanel} representing the plugin's options.
     */
    private @NotNull JPanel createPluginPanel(@NotNull Core plugin) {
        JPanel pluginPanel = new JPanel();
        pluginPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        pluginPanel.setBackground(new Color(44, 44, 44));

        JLabel pluginLabel = new JLabel(plugin.getName());
        pluginLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        pluginLabel.setForeground(Color.WHITE);
        pluginPanel.add(pluginLabel);

        JButton viewCommandsButton = createStyledButton("View Commands", e -> {
            showPluginCommands(plugin);
        });

        pluginPanel.add(viewCommandsButton);

        // If the plugin has an "About" section, display an additional button
        if (plugin.getExecutables().getAbout() != null) {
            JButton viewInfoButton = createStyledButton("About", e -> plugin.getExecutables().getAbout().execute());
            pluginPanel.add(viewInfoButton);
        }

        return pluginPanel;
    }

    /**
     * Displays a dialog with a list of commands available for the given plugin.
     *
     * @param plugin The {@link Core} plugin whose commands should be displayed.
     */
    private void showPluginCommands(@NotNull Core plugin) {
        JPanel commandsPanel = new JPanel();
        commandsPanel.setLayout(new BoxLayout(commandsPanel, BoxLayout.Y_AXIS));
        commandsPanel.setBackground(new Color(33, 33, 33));

        List<Executable> commands = plugin.getExecutables().getPlugins();

        // If no commands are available, show a message
        if (commands.isEmpty()) {
            JLabel noCommandsLabel = new JLabel("No commands available.");
            noCommandsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            noCommandsLabel.setForeground(Color.WHITE);
            commandsPanel.add(noCommandsLabel);
        } else {
            // Create a button for each command
            for (Executable command : commands) {
                JButton commandButton = createStyledButton(command.name(), e -> command.execute());
                commandsPanel.add(commandButton);
            }
        }

        JScrollPane scrollPane = new JScrollPane(commandsPanel);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        // Show the commands in a dialog
        JOptionPane.showMessageDialog(this, scrollPane, plugin.getName() + " Commands", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Creates a styled {@code JButton} with a hover effect for user interaction.
     *
     * @param text          The text to be displayed on the button.
     * @param actionListener The action listener to handle button clicks.
     * @return A styled {@code JButton}.
     */
    private @NotNull JButton createStyledButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(0, 123, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 2));
        button.setPreferredSize(new Dimension(150, 40));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(0, 150, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(0, 123, 255));
            }
        });

        button.addActionListener(actionListener);
        return button;
    }
}
