package net.regsirius06.engine.gui.labyrinth;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Set;

import net.regsirius06.engine.labyrinth.*;
import net.regsirius06.engine.minimap.MiniMap;
import net.regsirius06.engine.ui.KeyDefinition;
import net.regsirius06.engine.ui.KeyLogic;
import net.regsirius06.engine.ui.UIKeyListener;
import net.regsirius06.engine.util.DirectionalLightSource;
import org.jetbrains.annotations.NotNull;

public class LabyrinthPanel extends JPanel implements UIKeyListener {
    private final LabyrinthGraphics2D graphics2D;
    private final Labyrinth2D labyrinth;
    private final Player player;
    private final int width, height;
    private final KeyLogic keyLogic;
    private boolean visibleMiniMap;

    private static final int MINI_MAP_SIZE = 200;
    private final MiniMap miniMap;

    public LabyrinthPanel(int width, int height, int dimension, int quantityOfLights, boolean visibleMiniMap, KeyDefinition definition) {
        this.visibleMiniMap = visibleMiniMap;
        this.width = width;
        this.height = height;
        this.labyrinth = new Labyrinth2D(dimension, quantityOfLights);
        this.player = new Player(labyrinth.getSpawnPoint());
        this.graphics2D = new LabyrinthGraphics2D(width, height, this.labyrinth, this.player);
        this.keyLogic = new KeyLogic(definition);

        this.addKeyListener(this);
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);

        this.setBackground(Color.BLACK);
        this.miniMap = new MiniMap(this.labyrinth, MINI_MAP_SIZE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        this.graphics2D.draw(g2d);
        renderWarning(g2d, this.graphics2D.getVisibleLightSources());
        if (visibleMiniMap) renderMiniMap(g2d);
    }

    private void renderWarning(@NotNull Graphics2D g2d, @NotNull Set<DirectionalLightSource> visibleLightSources) {
        g2d.setColor(new Color(10, 10, 10));
        if (visibleLightSources.stream().anyMatch(e -> e.lightSource().isPointInside(this.player))) {
            g2d.drawString("You are in light!", width / 2, height / 2);
        }
    }

    private void renderMiniMap(Graphics2D g2d) {
        miniMap.render(g2d);
        miniMap.renderPlayer(g2d, player.getX(), player.getY());
        miniMap.renderBorder(g2d);
    }

    @Override
    public void keyPressed(@NotNull KeyEvent e) {
        keyLogic.keyMoveAnalysis(e, player, labyrinth);
        if (keyLogic.keyMapAnalysis(e, visibleMiniMap) != null) {
            this.visibleMiniMap = keyLogic.keyMapAnalysis(e, visibleMiniMap);
        }
        this.repaint();
    }
}
