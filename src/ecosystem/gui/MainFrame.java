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

    private static final int CELL_SIZE    = 48;
    private static final int SIDE_WIDTH   = 280;

    private final MapPanel     mapPanel;
    private final InfoPanel    infoPanel;
    private final StatsPanel   statsPanel;
    private final ControlPanel controlPanel;

    public MainFrame(SimulationEngine engine, Environment environment) {
        super("Ecosystem Simulation");

        ImageManager.getInstance().initialize(CELL_SIZE);

        infoPanel    = new InfoPanel(SIDE_WIDTH);
        statsPanel   = new StatsPanel(SIDE_WIDTH);
        mapPanel     = new MapPanel(environment, CELL_SIZE, infoPanel);
        controlPanel = new ControlPanel(engine, environment, mapPanel);

        JPanel sideBar = new JPanel(new BorderLayout(0, 8));
        sideBar.setPreferredSize(new Dimension(SIDE_WIDTH, 0));
        sideBar.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        sideBar.add(infoPanel,    BorderLayout.NORTH);
        sideBar.add(statsPanel,   BorderLayout.CENTER);
        sideBar.add(controlPanel, BorderLayout.SOUTH);

        setLayout(new BorderLayout(8, 0));

        JScrollPane scrollPane = new JScrollPane(mapPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
        add(sideBar,  BorderLayout.EAST);

        engine.addObserver(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void onSimulationUpdated(SimulationEvent event) {
        SwingUtilities.invokeLater(() -> {
            mapPanel.refresh(event.getEnvironment());
            statsPanel.update(event);
        });
    }
}