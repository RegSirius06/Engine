package net.regsirius06.engine.gui;

import net.regsirius06.engine.control.KeyDefinition;
import net.regsirius06.engine.world.World;
import net.regsirius06.engine.API.Initializer;
import net.regsirius06.engine.utils.panels.AbstractLabyrinthPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static net.regsirius06.engine.plugins.loaders.PluginsLoader.PLUGINS;

/**
 * The {@code WorldFrame} class provides a user interface for selecting and creating worlds.
 * It allows users to load an existing world from a list of saved worlds or create a new one.
 * The frame provides buttons for loading or creating a world and displays an input dialog for creating new worlds.
 */
public class WorldFrame extends MainMenu.Child {

    /**
     * Constructs a new {@code WorldFrame} that displays the list of available worlds,
     * and allows the user to either load a selected world or create a new one.
     */
    @SuppressWarnings("unchecked")
    public WorldFrame() {
        super();
        setTitle("Select World");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(33, 33, 33));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        List<String> savedWorlds = World.getSavedWorlds();
        JComboBox<String> worldSelector = new JComboBox<>();
        if (savedWorlds.isEmpty()) {
            worldSelector.addItem("No saved worlds");
        } else {
            for (String world : savedWorlds) {
                worldSelector.addItem(world);
            }
        }

        JTextField widthField = createTextField();
        JTextField heightField = createTextField();
        JTextField dimensionField = createTextField();
        JTextField quantityOfLightField = createTextField();
        JTextField seedField = createTextField();

        JComboBox<Initializer<? extends AbstractLabyrinthPanel>> initializerComboBox = new JComboBox<>();
        Map<String, String> nameToClassMap = new HashMap<>();
        for (Initializer<? extends AbstractLabyrinthPanel> initializer : PLUGINS.getBase().getLabyrinthPanels().getPlugins()) {
            String name = initializer.getName();
            String className = initializer.getType().getSimpleName();
            if (nameToClassMap.containsKey(name)) name += " (" + className + ")";
            initializerComboBox.addItem(initializer);
            nameToClassMap.put(name, className);
        }

        initializerComboBox.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
            String name = value.getName();
            String className = value.getType().getSimpleName();

            if (nameToClassMap.containsKey(name) && !nameToClassMap.get(name).equals(className)) {
                name += " (" + className + ")";
            }

            JLabel label = new JLabel(name);
            if (isSelected) {
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
            } else {
                label.setBackground(list.getBackground());
                label.setForeground(list.getForeground());
            }
            label.setOpaque(true);
            return label;
        });

        JButton loadButton = createStyledButton("Load World", e -> {
            String selectedWorld = (String) worldSelector.getSelectedItem();
            if (selectedWorld != null && !selectedWorld.equals("No saved worlds")) {
                loadWorld(selectedWorld);
            } else {
                JOptionPane.showMessageDialog(panel, "Please select a world.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton newWorldButton = createStyledButton("Create New World", e -> {
            try {
                int width = parseIntWithDefault(widthField.getText(), 960);
                int height = parseIntWithDefault(heightField.getText(), 540);
                int dimension = Integer.parseInt(dimensionField.getText());
                int quantityOfLight = Integer.parseInt(quantityOfLightField.getText());

                if (dimension < 10) {
                    JOptionPane.showMessageDialog(panel, "Dimension must be at least 10.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (quantityOfLight >= 50) {
                    JOptionPane.showMessageDialog(panel, "Quantity of lights must be less than 50.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } else if (quantityOfLight <= 0) {
                    JOptionPane.showMessageDialog(panel, "Quantity of lights must be positive number.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                long seed = parseSeed(seedField.getText());
                Initializer<? extends AbstractLabyrinthPanel> selectedInitializer = (Initializer<? extends AbstractLabyrinthPanel>) initializerComboBox.getSelectedItem();
                if (selectedInitializer == null) {
                    JOptionPane.showMessageDialog(panel, "Please select a labyrinth initializer.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String worldName = JOptionPane.showInputDialog(this, "Enter the name of the new world:");
                if (!isValidWorldName(worldName)) {
                    JOptionPane.showMessageDialog(panel, "World name cannot be empty or just whitespace!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                createNewWorld(worldName, width, height, dimension, quantityOfLight, seed, selectedInitializer);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "All fields must be filled with valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(createCenteredText("Select the existing world:"));
        panel.add(worldSelector);
        panel.add(createCenteredText("... or create a new world:"));
        panel.add(createLine("Width (optional):", widthField));
        panel.add(createLine("Height (optional):", heightField));
        panel.add(createLine("Dimension:", dimensionField));
        panel.add(createLine("Quantity of Lights:", quantityOfLightField));
        panel.add(createLine("Seed (optional):", seedField));
        panel.add(createLine("Labyrinth Initializer:", initializerComboBox));

        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        actionPanel.setBackground(new Color(44, 44, 44));

        actionPanel.add(loadButton);
        actionPanel.add(newWorldButton);

        panel.add(actionPanel);

        getContentPane().add(panel);
    }

    /**
     * Creates a styled {@code JButton} with a hover effect.
     *
     * @param text          The text to be displayed on the button.
     * @param actionListener The action listener to handle button clicks.
     * @return A styled {@code JButton}.
     */
    private @NotNull JButton createStyledButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(new Color(0, 128, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        button.setPreferredSize(new Dimension(180, 60));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(0, 150, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(0, 128, 255));
            }
        });

        button.addActionListener(actionListener);
        return button;
    }

    /**
     * Creates a text field with a specific font and foreground color.
     *
     * @return A {@code JTextField}.
     */
    private @NotNull JTextField createTextField() {
        JTextField textField = new JTextField(10);
        textField.setFont(new Font("Arial", Font.PLAIN, 18));
        textField.setForeground(Color.BLACK);
        return textField;
    }

    /**
     * Creates a JLabel with a specific font and foreground color.
     *
     * @param text The text to be displayed on the label.
     * @return A {@code JLabel}.
     */
    private @NotNull JLabel createJLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Serif", Font.PLAIN, 18));
        label.setForeground(Color.WHITE);
        return label;
    }

    /**
     * Creates a JPanel containing a centered {@code JLabel}.
     *
     * @param label The label to center inside the panel.
     * @return A {@code JPanel}.
     */
    private @NotNull JPanel createCenteredText(JLabel label) {
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        actionPanel.setBackground(new Color(44, 44, 44));

        actionPanel.add(label);

        return actionPanel;
    }

    /**
     * Creates a JPanel containing a centered {@code String}.
     *
     * @param text The text to center inside the panel.
     * @return A {@code JPanel}.
     */
    private @NotNull JPanel createCenteredText(String text) {
        return createCenteredText(createJLabel(text));
    }

    /**
     * Creates a JPanel containing a {@code JLabel} and a {@code JTextField} for input.
     *
     * @param label     The label to be displayed.
     * @param textField The text field for user input.
     * @return A {@code JPanel}.
     */
    private @NotNull JPanel createLine(JLabel label, JTextField textField) {
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        actionPanel.setBackground(new Color(44, 44, 44));

        actionPanel.add(label);
        actionPanel.add(textField);

        return actionPanel;
    }

    /**
     * Creates a JPanel containing a {@code String} label and a {@code JTextField}.
     *
     * @param text      The text for the label.
     * @param textField The text field for user input.
     * @return A {@code JPanel}.
     */
    private @NotNull JPanel createLine(String text, JTextField textField) {
        return createLine(createJLabel(text), textField);
    }

    /**
     * Creates a JPanel containing a {@code JLabel} and a {@code JComboBox}.
     *
     * @param label   The label to be displayed.
     * @param comboBox The combo box to be displayed.
     * @param <T>     The type of items in the combo box.
     * @return A {@code JPanel}.
     */
    private @NotNull <T> JPanel createLine(JLabel label, JComboBox<T> comboBox) {
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        actionPanel.setBackground(new Color(44, 44, 44));

        actionPanel.add(label);
        actionPanel.add(comboBox);

        return actionPanel;
    }

    /**
     * Creates a JPanel containing a {@code String} label and a {@code JComboBox}.
     *
     * @param text     The text for the label.
     * @param comboBox The combo box for user selection.
     * @param <T>      The type of items in the combo box.
     * @return A {@code JPanel}.
     */
    private @NotNull <T> JPanel createLine(String text, JComboBox<T> comboBox) {
        return createLine(createJLabel(text), comboBox);
    }

    /**
     * Loads the selected world and transitions to the game screen.
     *
     * @param worldName The name of the world to load.
     */
    private void loadWorld(String worldName) {
        World world = World.load(worldName);
        if (world != null) {
            new LabyrinthFrame(world);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to load world \"" + worldName + "\".", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Creates a new world with the specified parameters, saves it, and transitions to the game screen.
     *
     * @param worldName        The name of the new world.
     * @param width            The width of the labyrinth.
     * @param height           The height of the labyrinth.
     * @param dimension        The dimension of the labyrinth grid.
     * @param quantityOfLights The number of light sources.
     * @param seed             The random seed for generating the labyrinth (optional).
     * @param labyrinthInitializer The initializer to create the labyrinth panel.
     */
    private void createNewWorld(String worldName, int width, int height, int dimension, int quantityOfLights,
                                Long seed, Initializer<? extends AbstractLabyrinthPanel> labyrinthInitializer) {
        if (World.load(worldName) != null) {
            JOptionPane.showMessageDialog(this, "A world with this name already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        World newWorld = new World(worldName, width, height, dimension, quantityOfLights, seed != null ? seed : System.currentTimeMillis(),
                KeyDefinition.load(), labyrinthInitializer);
        newWorld.save();
        JOptionPane.showMessageDialog(this, "New world \"" + worldName + "\" created!", "World Creation", JOptionPane.INFORMATION_MESSAGE);
        dispose();
        loadWorld(worldName);
    }

    /**
     * Validates if the world name is valid (not empty or whitespace).
     *
     * @param worldName The name to validate.
     * @return true if valid, false otherwise.
     */
    private boolean isValidWorldName(String worldName) {
        return worldName != null && !worldName.trim().isEmpty();
    }

    /**
     * Parses a string as an integer, providing a default value if parsing fails.
     *
     * @param value       The string to parse.
     * @param defaultValue The default value to return if parsing fails.
     * @return The parsed integer or the default value.
     */
    private int parseIntWithDefault(@Nullable String value, int defaultValue) {
        try {
            if (value == null) {
                throw new NullPointerException();
            }
            return Integer.parseInt(value);
        } catch (NumberFormatException | NullPointerException e) {
            return defaultValue;
        }
    }

    /**
     * Parses a string as a long, or returns null if parsing fails.
     *
     * @param value The string to parse.
     * @return The parsed long or null.
     */
    private Long parseSeed(String value) {
        Random random = new Random();
        try {
            return value == null || value.isEmpty() ? random.nextLong() : Long.parseLong(value, 36);
        } catch (NumberFormatException e) {
            return random.nextLong();
        }
    }
}
