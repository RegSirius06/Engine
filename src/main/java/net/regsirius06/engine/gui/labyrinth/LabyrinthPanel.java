package net.regsirius06.engine.gui.labyrinth;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Set;

import net.regsirius06.engine.graphics.LabyrinthGraphics2D;
import net.regsirius06.engine.labyrinth.*;
import net.regsirius06.engine.minimap.MiniMap;
import net.regsirius06.engine.point.Player;
import net.regsirius06.engine.ui.KeyDefinition;
import net.regsirius06.engine.ui.KeyLogic;
import net.regsirius06.engine.annotations.UIKeyListener;
import net.regsirius06.engine.point.LightSource;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a panel for rendering the labyrinth game and handling user input.
 * This class extends {@link JPanel} and provides a user interface to interact with
 * the labyrinth game. It handles key events for player movement and rendering
 * of the labyrinth graphics and a mini-map.
 */
public class LabyrinthPanel extends JPanel implements UIKeyListener {

    /** The graphics renderer for the labyrinth. */
    private final LabyrinthGraphics2D graphics2D;

    /** The 2D labyrinth instance representing the game layout. */
    private final Labyrinth2D labyrinth;

    /** The player in the labyrinth. */
    private final Player player;

    /** The width of the panel in pixels. */
    private final int width, height;

    /** Logic for handling key presses. */
    private final KeyLogic keyLogic;

    /** Indicates whether the mini-map should be visible. */
    private boolean visibleMiniMap;

    /** The size of the mini-map in pixels. */
    private static final int MINI_MAP_SIZE = 200;

    /** The mini-map display for the labyrinth. */
    private final MiniMap miniMap;

    /**
     * Constructs a new LabyrinthPanel with the specified parameters.
     * This constructor initializes the labyrinth, player, key logic, and graphics renderer.
     * It also sets up the mini-map and adds the necessary key listener for player interaction.
     *
     * @param width  The width of the panel in pixels.
     * @param height The height of the panel in pixels.
     * @param dimension The dimension of the labyrinth grid.
     * @param quantityOfLights The number of light sources in the labyrinth.
     * @param visibleMiniMap A flag indicating whether the mini-map should be visible.
     * @param keyLogic The key logic used for player movement.
     */
    public LabyrinthPanel(int width, int height, int dimension, int quantityOfLights, boolean visibleMiniMap, KeyLogic keyLogic) {
        this.visibleMiniMap = visibleMiniMap;
        this.width = width;
        this.height = height;
        this.labyrinth = new Labyrinth2D(dimension, quantityOfLights);
        this.player = new Player(labyrinth.getSpawnPoint());
        this.graphics2D = new LabyrinthGraphics2D(width, height, this.labyrinth, this.player);
        this.keyLogic = keyLogic;

        this.addKeyListener(this);
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);

        this.setBackground(Color.BLACK);
        this.miniMap = new MiniMap(this.labyrinth, MINI_MAP_SIZE, player);
    }

    /**
     * Paints the component, rendering the labyrinth and any additional UI elements such as the mini-map.
     * This method is called automatically when the panel needs to be re-rendered.
     *
     * @param g The {@link Graphics} object used for drawing the panel's content.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw the labyrinth game using the LabyrinthGraphics2D renderer
        this.graphics2D.draw(g2d);

        // Render a warning if the player is inside light
        renderWarning(g2d, this.graphics2D.getVisibleLightSources());

        // Draw the mini-map if it's enabled
        if (visibleMiniMap) miniMap.draw(g2d);
    }

    /**
     * Renders a warning message if the player is inside a light source.
     * The warning is displayed at the center of the screen.
     *
     * @param g2d The {@link Graphics2D} object used for drawing the warning.
     * @param visibleLightSources The set of visible light sources in the labyrinth.
     */
    private void renderWarning(@NotNull Graphics2D g2d, @NotNull Set<LightSource> visibleLightSources) {
        g2d.setColor(new Color(10, 10, 10));
        if (visibleLightSources.stream().anyMatch(e -> e.isPointInside(this.player))) {
            g2d.drawString("You are in light!", width / 2, height / 2);
        }
    }

    /**
     * Handles key press events to control player movement and toggle the visibility of the mini-map.
     * This method is called automatically when a key is pressed while the panel is focused.
     *
     * @param e The {@link KeyEvent} that describes the key press event.
     */
    @Override
    public final void keyPressed(@NotNull KeyEvent e) {
        // Analyze the key press for player movement
        keyLogic.keyMoveAnalysis(e, player, labyrinth);

        // Toggle mini-map visibility if necessary
        if (keyLogic.keyMapAnalysis(e, visibleMiniMap) != null) {
            this.visibleMiniMap = keyLogic.keyMapAnalysis(e, visibleMiniMap);
        }

        // Repaint the panel to update the display
        this.repaint();
    }
}
