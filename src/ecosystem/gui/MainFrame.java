package ecosystem.gui;

import ecosystem.core.SimulationEngine;
import ecosystem.core.Environment;
import ecosystem.gui.observer.SimulationEvent;
import ecosystem.gui.observer.SimulationObserver;

import javax.swing.*;
import java.awt.*;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 */
public class MainFrame extends JFrame implements SimulationObserver {

    // ── Constants ────────────────────────────────────────────────────────────
    private static final int CELL_SIZE    = 48;
    private static final int SIDE_WIDTH   = 280;

    // ── Child panels ─────────────────────────────────────────────────────────
    private final MapPanel     mapPanel;
    private final InfoPanel    infoPanel;
    private final StatsPanel   statsPanel;
    private final ControlPanel controlPanel;

    /**
     * Constructs the main application window, wires all panels together,
     * and registers this frame as an observer on the engine.
     *
     * @param engine      the simulation model; never {@code null}
     * @param environment the shared environment grid; never {@code null}
     */
    public MainFrame(SimulationEngine engine, Environment environment) {
        super("Ecosystem Simulation");

        // 1. Boot the image cache before any painting occurs
        ImageManager.getInstance().initialize(CELL_SIZE);

        // 2. Build panels
        infoPanel    = new InfoPanel(SIDE_WIDTH);
        statsPanel   = new StatsPanel(SIDE_WIDTH);
        mapPanel     = new MapPanel(environment, CELL_SIZE, infoPanel);
        controlPanel = new ControlPanel(engine, environment, mapPanel);

        // 3. Compose the side bar
        JPanel sideBar = new JPanel(new BorderLayout(0, 8));
        sideBar.setPreferredSize(new Dimension(SIDE_WIDTH, 0));
        sideBar.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        sideBar.add(infoPanel,    BorderLayout.NORTH);
        sideBar.add(statsPanel,   BorderLayout.CENTER);
        sideBar.add(controlPanel, BorderLayout.SOUTH);

        // 4. Root layout
        setLayout(new BorderLayout(8, 0));
        add(mapPanel, BorderLayout.CENTER);
        add(sideBar,  BorderLayout.EAST);

        // 5. Register as observer
        engine.addObserver(this);

        // 6. Window housekeeping
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ── SimulationObserver ───────────────────────────────────────────────────

    /**
     * Fans the update event out to all child panels.
     * This method may be called from a background thread, so it delegates
     * all Swing work to the EDT via {@code SwingUtilities.invokeLater}.
     *
     * @param event current simulation snapshot; never {@code null}
     */
    @Override
    public void onSimulationUpdated(SimulationEvent event) {
        SwingUtilities.invokeLater(() -> {
            mapPanel.refresh(event.getEnvironment());
            statsPanel.update(event);
        });
    }
}