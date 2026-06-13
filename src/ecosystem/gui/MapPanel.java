package ecosystem.gui;

import ecosystem.core.Environment;
import ecosystem.core.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 */
public class MapPanel extends JPanel {

    private static final Color COLOR_HIGHLIGHT = new Color(255, 215, 0, 120); // translucent gold
    private static final Color COLOR_GRID = new Color(200, 200, 200);

    private final int cellSize;
    private final InfoPanel infoPanel;
    private Environment environment;

    /**
     * Currently selected cell, or {@code null} when nothing is selected.
     */
    private Position selectedCell = null;

    /**
     * Constructs the map panel.
     *
     * @param environment initial environment; never {@code null}
     * @param cellSize    pixel dimension of each square cell
     * @param infoPanel   panel that receives entity details on click
     */
    public MapPanel(Environment environment, int cellSize, InfoPanel infoPanel) {
        this.environment = environment;
        this.cellSize = cellSize;
        this.infoPanel = infoPanel;

        int cols = environment.getCols();
        int rows = environment.getRows();
        setPreferredSize(new Dimension(cols * cellSize, rows * cellSize));
        setBackground(Color.WHITE);

        // ── Mouse interactions ────────────────────────────────────────────
        MouseAdapter mouse = new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                Position pos = pixelToPosition(e.getX(), e.getY());
                Object entity = getEntityAt(pos);
                if (entity != null) {
                    setToolTipText(entity.toString());
                } else {
                    setToolTipText("Empty (" + pos.getCol() + ", " + pos.getRow() + ")");
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                selectedCell = pixelToPosition(e.getX(), e.getY());
                Object entity = getEntityAt(selectedCell);
                infoPanel.display(entity, selectedCell);
                repaint();
            }
        };

        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        ToolTipManager.sharedInstance().setInitialDelay(200);
    }

    // ── Public API ───────────────────────────────────────────────────────────

    /**
     * Updates the displayed environment and repaints the grid.
     * Must be called on the EDT.
     *
     * @param updatedEnvironment the latest environment snapshot
     */
    public void refresh(Environment updatedEnvironment) {
        this.environment = updatedEnvironment;
        repaint();
    }

    // ── Painting ─────────────────────────────────────────────────────────────

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int cols = environment.getCols();
        int rows = environment.getRows();
        ImageManager imgMgr = ImageManager.getInstance();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                int px = col * cellSize;
                int py = row * cellSize;

                // Background
                g2.setColor(Color.WHITE);
                g2.fillRect(px, py, cellSize, cellSize);

                // Icon
                Position pos = new Position(col, row);
                Object entity = getEntityAt(pos);

                // --- תוספת לטיפול בדקורטורים ---
                Object entityToDraw = entity;
                if (entity instanceof ecosystem.decorators.EntityDecorator) {
                    entityToDraw = ((ecosystem.decorators.EntityDecorator) entity).getDecoratedEntity();
                }

                // מבקש תמונה עבור החיה המקורית, גם אם היא עטופה
                ImageManager.EntityType type = imgMgr.resolveType(entityToDraw);
                ImageIcon icon = imgMgr.getIcon(type);
                g2.drawImage(icon.getImage(), px, py, cellSize, cellSize, this);

                // Highlight selected cell
                if (selectedCell != null
                        && selectedCell.getCol() == col
                        && selectedCell.getRow() == row) {
                    g2.setColor(COLOR_HIGHLIGHT);
                    g2.fillRect(px, py, cellSize, cellSize);
                }

                // Grid lines
                g2.setColor(COLOR_GRID);
                g2.drawRect(px, py, cellSize, cellSize);
            }
        }
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    /**
     * Converts pixel coordinates to a grid {@link Position}.
     *
     * @param px x pixel coordinate
     * @param py y pixel coordinate
     * @return clamped grid position
     */
    private Position pixelToPosition(int px, int py) {
        int col = Math.min(px / cellSize, environment.getCols() - 1);
        int row = Math.min(py / cellSize, environment.getRows() - 1);
        return new Position(col, row);
    }

    /**
     * Returns the entity (or {@code null}) at the given position.
     *
     * <p>Adjust this to match your {@link Environment} API for entity lookup.</p>
     */
    private Object getEntityAt(Position pos) {
        // ── ADAPT TO YOUR Environment API ───────────────────────────────────
        // Example: return environment.getEntityAt(pos);
        // If Environment uses a List, scan for matching position:
        return environment.getEntities().stream()
                .filter(e -> e.getPosition().equals(pos))
                .findFirst()
                .orElse(null);
    }
}