package net.regsirius06.engine.gui;

import net.regsirius06.engine.control.KeyDefinition;
import net.regsirius06.engine.control.KeyDefinition.Action;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The {@code SettingsFrame} class represents a settings window for configuring
 * key bindings and saving the changes.
 * It allows the user to view and modify the key bindings for various actions.
 *
 * @see KeyDefinition
 */
public class SettingsFrame extends MainMenu.Child {

    /**
     * The {@link KeyDefinition} instance used to manage key bindings.
     *
     * <p>This field is responsible for loading and storing key mappings for various actions.
     * It provides methods to retrieve and modify key bindings, which are displayed and edited
     * in the settings menu.</p>
     *
     * <p><b>Deprecated:</b> This field is deprecated because a new key-binding system is planned
     * for future versions, providing better flexibility and customization options.</p>
     *
     * @deprecated Use the new key-binding management system instead.
     * @see KeyDefinition#load()
     */
    @Deprecated
    private final KeyDefinition keyDefinition = KeyDefinition.load();

    /**
     * Constructs a new {@code SettingsFrame} to display and modify key bindings.
     */
    public SettingsFrame() {
        super();
        setTitle("Settings");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(33, 33, 33));

        // Add panels for each action's key binding
        for (Action action : Action.values()) {
            panel.add(createActionPanel(action));
        }

        JButton saveButton = createStyledButton("Save Changes", e -> {
            keyDefinition.save();
            JOptionPane.showMessageDialog(this, "Changes saved!", "Settings", JOptionPane.INFORMATION_MESSAGE);
            this.onClose();
        });

        panel.add(Box.createVerticalStrut(20));
        panel.add(saveButton);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(new Color(33, 33, 33));
        getContentPane().add(scrollPane);
    }

    /**
     * Creates a panel to display and modify the key binding for a specific action.
     *
     * @param action The action whose key binding needs to be modified.
     * @return A {@code JPanel} containing the action label, current key binding,
     *         and a button to change the key.
     */
    private @NotNull JPanel createActionPanel(@NotNull Action action) {
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        actionPanel.setBackground(new Color(44, 44, 44));

        JLabel actionLabel = new JLabel(action.name().replace('_', ' ') + ": ");
        actionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        actionLabel.setForeground(Color.WHITE);
        actionPanel.add(actionLabel);

        // Get the current key binding for the action
        String keyText;
        if (keyDefinition.getKey(action) == null) {
            keyText = "Unknown";
        } else {
            keyText = KeyEvent.getKeyText(keyDefinition.getKey(action));
        }

        JLabel keyLabel = new JLabel(keyText);
        keyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        keyLabel.setForeground(Color.WHITE);
        actionPanel.add(keyLabel);

        JButton changeButton = createStyledButton("Change", e -> {
            KeyStroke newKey = getKeyInput();
            if (newKey != null) {
                keyDefinition.setKey(action, newKey.getKeyCode());
                keyLabel.setText(KeyEvent.getKeyText(newKey.getKeyCode()));
            }
        });
        actionPanel.add(changeButton);

        return actionPanel;
    }

    /**
     * Opens a dialog for the user to press a key, and returns the {@code KeyStroke}
     * representing the key that was pressed.
     *
     * @return The {@code KeyStroke} of the key pressed by the user.
     */
    private KeyStroke getKeyInput() {
        JDialog keyDialog = new JDialog(this, "Press a key", true);
        keyDialog.setSize(300, 150);
        keyDialog.setLocationRelativeTo(this);
        keyDialog.setLayout(new FlowLayout());

        JLabel instructionLabel = new JLabel("Press any key to bind...");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        instructionLabel.setForeground(Color.BLACK);
        keyDialog.add(instructionLabel);

        final KeyStroke[] pressedKey = new KeyStroke[1];
        keyDialog.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                pressedKey[0] = KeyStroke.getKeyStrokeForEvent(e);
                keyDialog.dispose();
            }
        });

        keyDialog.setVisible(true);

        return pressedKey[0];
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
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(new Color(0, 123, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 2));
        button.setPreferredSize(new Dimension(200, 50));
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
