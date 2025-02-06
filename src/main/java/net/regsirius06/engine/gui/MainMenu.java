package net.regsirius06.engine.gui;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Represents the main menu window of the Maze Adventure game. It displays the main menu
 * with buttons for launching the game, settings, and plugins.
 * <p>
 * The background of the menu features a rotating image, providing a dynamic visual effect.
 * The menu buttons are styled, and clicking them opens different parts of the application:
 * - the game window,
 * - the settings window,
 * - the plugin interface.
 * </p>
 * <p>
 * Special features:
 * <ul>
 * <li>Animated background scrolling effect.</li>
 * <li>Hover effect on buttons.</li>
 * <li>Fixed window size and behavior.</li>
 * </ul>
 */
public class MainMenu extends JFrame {
    /** The background image used in the main menu. */
    private static final ImageIcon backgroundImage = new ImageIcon(Objects.requireNonNull(
            MainMenu.class.getClassLoader().getResource("pictures/background.png")
    ));

    /**
     * Creates a new instance of the main menu window.
     * <p>
     * The constructor sets up the window's title, size, default close operation, and
     * adds components such as the rotating background and buttons.
     * </p>
     */
    public MainMenu() {
        setTitle("Maze Adventure");
        setSize(960, 540);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new RotatingBack();

        panel.setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("Maze Adventure", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        buttonPanel.setOpaque(false);

        JButton playButton = createStyledButton("Play", WorldFrame::new);
        JButton settingsButton = createStyledButton("Settings", SettingsFrame::new);
        JButton pluginsButton = createStyledButton("Plugins", PluginsFrame::new);

        buttonPanel.add(playButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(settingsButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(pluginsButton);
        buttonPanel.add(Box.createVerticalStrut(20));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        add(panel);

        setVisible(true);
    }

    /**
     * Creates a styled button for the main menu.
     * <p>
     * This method sets the button's font, background color, hover effect, and click action.
     * The button's action listener opens the corresponding window when clicked and closes
     * the main menu.
     * </p>
     *
     * @param text the text to display on the button
     * @param newFrame a supplier that provides the new frame to open when the button is clicked
     * @return a styled JButton
     */
    private @NotNull JButton createStyledButton(String text, Supplier<? extends JFrame> newFrame) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setBackground(new Color(0, 128, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        button.setPreferredSize(new Dimension(200, 60));

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

        button.addActionListener(e -> {
            newFrame.get().setVisible(true);
            this.dispose();
        });

        return button;
    }

    /**
     * Opens the main menu window in the Event Dispatch Thread (EDT).
     * This method is used to ensure thread safety when creating the UI.
     */
    public static void open() {
        SwingUtilities.invokeLater(MainMenu::new);
    }

    /**
     * Base class for child windows that open from the main menu.
     * <p>
     * This class ensures that when the child window is closed, the main menu is reopened.
     * </p>
     */
    protected static abstract class Child extends JFrame {
        /**
         * Creates a new child window.
         * <p>
         * This constructor sets the default close operation to do nothing, and adds a window
         * listener to ensure that closing the child window brings back the main menu.
         * </p>
         */
        public Child() {
            this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    onClose();
                }
            });
        }

        /**
         * Handles the closing event of the child window.
         * <p>
         * This method is called when the window is closed, and it reopens the main menu.
         * </p>
         */
        protected void onClose() {
            MainMenu.open();
            dispose();
        }
    }

    /**
     * Custom JPanel that handles the rotating background effect used in the main menu.
     * <p>
     * The background image scrolls horizontally to create an animated visual effect.
     * </p>
     */
    protected static class RotatingBack extends JPanel {
        /** The horizontal position of the background image. */
        private float xPos = 0;

        /**
         * Creates a new instance of the rotating background panel.
         * <p>
         * A timer is started that updates the background position and repaints the panel
         * to create the scrolling effect.
         * </p>
         */
        public RotatingBack() {
            Timer timer = new Timer(64, e -> {
                xPos += 0.5f;
                if (xPos >= backgroundImage.getIconWidth()) {
                    xPos = 0;
                }
                repaint();
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int imageWidth = backgroundImage.getIconWidth();
            int imageHeight = backgroundImage.getIconHeight();

            int start = (int) xPos;
            int end = (int) (xPos + imageWidth / 4.0);

            if (end <= imageWidth) {
                g.drawImage(backgroundImage.getImage(),
                        0, 0, getWidth(), getHeight(),
                        start, 0, end, imageHeight, this);
            } else {
                g.drawImage(backgroundImage.getImage(),
                        0, 0, getWidth() - (end - imageWidth) * 4 * getWidth() / imageWidth, getHeight(),
                        start, 0, imageWidth, imageHeight, this);
                g.drawImage(backgroundImage.getImage(),
                        getWidth() - (end - imageWidth) * 4 * getWidth() / imageWidth, 0, getWidth(), getHeight(),
                        0, 0, end - imageWidth, imageHeight, this);
            }
        }
    }
}
