package net.regsirius06.engine.core.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Set;
import net.regsirius06.engine.utils.panels.AbstractLabyrinthPanel;
import net.regsirius06.engine.core.graphics.labyrinth.default2D.Labyrinth2D;
import net.regsirius06.engine.core.graphics.labyrinth.default2D.LabyrinthGraphics2D;
import net.regsirius06.engine.core.graphics.minimap.MiniMap;
import net.regsirius06.engine.point.Wall;
import net.regsirius06.engine.point.entities.Player;
import net.regsirius06.engine.control.KeyLogic;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a panel for rendering a labyrinth game and handling user input.
 * This class extends {@link JPanel} and provides a user interface to interact with
 * the labyrinth game. It handles key events for player movement and rendering
 * of the labyrinth graphics and a mini-map.
 * <p>
 * The panel allows users to control the player within the labyrinth using keyboard input.
 * It also displays the labyrinth, with visual elements such as light sources and a mini-map.
 * </p>
 */
public class LabyrinthPanel extends AbstractLabyrinthPanel {

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

    /**
     * The seed used to generate the labyrinth layout.
     * Ensures consistency in labyrinth generation for the same seed value.
     */
    private final long seed;

    /** Indicates whether the mini-map should be visible. */
    private boolean visibleMiniMap;

    /**
     * The size of the mini-map in pixels.
     * This determines the dimensions of the mini-map displayed on the panel.
     */
    private static final int MINI_MAP_SIZE = 200;

    /** The mini-map display for the labyrinth. */
    private final MiniMap miniMap;

    /**
     * Constructs a new {@link LabyrinthPanel} with the specified parameters.
     * This constructor initializes the labyrinth, player, key logic, and graphics renderer.
     * It also sets up the mini-map and adds the necessary key listener for player interaction.
     *
     * @param width  The width of the panel in pixels.
     * @param height The height of the panel in pixels.
     * @param dimension The dimension of the labyrinth grid.
     * @param quantityOfLights The number of light sources in the labyrinth.
     * @param seed The seed value used for generating the labyrinth layout, ensuring reproducibility.
     * @param keyLogic The key logic used for player movement.
     */
    public LabyrinthPanel(int width, int height, int dimension, int quantityOfLights, long seed, KeyLogic keyLogic) {
        super(width, height, dimension, quantityOfLights, seed, keyLogic);
        this.visibleMiniMap = false;
        this.width = width;
        this.height = height;
        this.labyrinth = new Labyrinth2D(dimension, quantityOfLights, seed);
        this.player = new Player(labyrinth.getSpawnPoint());
        this.graphics2D = new LabyrinthGraphics2D(width, height, this.labyrinth, this.player);
        this.keyLogic = keyLogic;
        this.seed = seed;

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
     * @param g2d The {@link Graphics2D} object used for drawing the panel's content.
     */
    @Override
    public void draw(Graphics2D g2d) {
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
    private void renderWarning(@NotNull Graphics2D g2d, @NotNull Set<Wall> visibleLightSources) {
        g2d.setColor(new Color(10, 10, 10));
        if (visibleLightSources.stream().anyMatch(e -> e.isPointInside(this.player))) {
            g2d.drawString("You are in light!", width / 2, height / 2);
        }
    }

    /**
     * Handles key press events to control player movement and toggle the visibility of the mini-map.
     * <p>
     * This method delegates movement control to {@link KeyLogic#keyMoveAnalysis(KeyEvent, Player, Labyrinth2D)}
     * and checks whether the mini-map should be toggled based on key input.
     * If a relevant key is pressed, the mini-map visibility state is updated accordingly.
     * </p>
     *
     * @param e The {@link KeyEvent} that describes the key press event.
     */
    @Override
    public final void keyPressed(@NotNull KeyEvent e) {
        // Analyze the key press for player movement
        keyLogic.keyMoveAnalysis(e, player, labyrinth);

        // Toggle mini-map visibility if necessary
        if (keyLogic.keyMapAnalysis(e, visibleMiniMap) != null) {
            this.visibleMiniMap = Boolean.TRUE.equals(keyLogic.keyMapAnalysis(e, visibleMiniMap));
        }

        // Repaint the panel to update the display
        this.repaint();
    }
}
