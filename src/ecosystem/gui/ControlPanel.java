package ecosystem.gui;

import ecosystem.core.Environment;
import ecosystem.core.Position;
import ecosystem.core.SimulationEngine;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import ecosystem.entities.AbstractEntity;
import ecosystem.entities.LivingEntity;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 */
public class ControlPanel extends JPanel {

    // ── Model references ─────────────────────────────────────────────────────
    private final SimulationEngine engine;
    private final Environment      environment;
    private final MapPanel         mapPanel;

    // ── Controls ─────────────────────────────────────────────────────────────
    private final JButton   btnTick;
    private final JButton   btnRun;
    private final JButton   btnStop;
    private final JButton   btnReset;
    private final JButton   btnAddEntity;
    private final JSlider   speedSlider;

    // ── Background thread state ──────────────────────────────────────────────
    private volatile boolean running   = false;

    /** Minimum delay between ticks in continuous mode (ms). Higher = slower. */
    private static final int DELAY_MIN_MS  = 50;
    private static final int DELAY_MAX_MS  = 2000;
    private static final int DELAY_INIT_MS = 500;

    /**
     * Constructs the control panel and wires all button actions.
     *
     * @param engine      simulation model; never {@code null}
     * @param environment the shared environment grid
     * @param mapPanel    the map view (used for refresh after reset)
     */
    public ControlPanel(SimulationEngine engine,
                        Environment environment,
                        MapPanel mapPanel) {
        this.engine      = engine;
        this.environment = environment;
        this.mapPanel    = mapPanel;

        JButton btnAddRandom = new JButton("🎲 Add Random");
        btnAddRandom.addActionListener(this::onAddRandomEntities);

        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Controls",
                TitledBorder.LEFT, TitledBorder.TOP));
        setLayout(new GridBagLayout());

        // Build controls
        btnTick      = new JButton("▶ Single Tick");
        btnRun       = new JButton("⏩ Continuous Run");
        btnStop      = new JButton("⏹ Stop");
        btnReset     = new JButton("↺ Reset");
        btnAddEntity = new JButton("＋ Add Entity");
        speedSlider  = buildSpeedSlider();

        btnStop.setEnabled(false);

        // Wire actions
        btnTick     .addActionListener(this::onSingleTick);
        btnRun      .addActionListener(this::onContinuousRun);
        btnStop     .addActionListener(this::onStop);
        btnReset    .addActionListener(this::onReset);
        btnAddEntity.addActionListener(this::onAddEntity);

        // Layout
        GridBagConstraints c = new GridBagConstraints();
        c.fill    = GridBagConstraints.HORIZONTAL;
        c.insets  = new Insets(3, 3, 3, 3);
        c.weightx = 1.0;
        c.gridx   = 0;

        c.gridy = 0; add(btnTick,      c);
        c.gridy = 1; add(btnRun,       c);
        c.gridy = 2; add(btnStop,      c);
        c.gridy = 3; add(btnReset,     c);
        c.gridy = 4; add(btnAddEntity, c);
        c.gridy = 7; add(btnAddRandom, c);

        JLabel speedLabel = new JLabel("Speed:", SwingConstants.LEFT);
        c.gridy = 5; add(speedLabel,   c);
        c.gridy = 6; add(speedSlider,  c);
    }

    // ── Action handlers ──────────────────────────────────────────────────────

    private void onSingleTick(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "Single Tick is disabled in Multithreading mode.");
    }

    private void onContinuousRun(ActionEvent e) {
        if (engine.isRunning()) return;
        setRunningState(true);

        engine.startSimulation();

        for (AbstractEntity entity : engine.getEnvironment().getEntities()) {
            if (entity instanceof LivingEntity) {
                LivingEntity le = (LivingEntity) entity;
                le.setEngine(engine);
                le.startThread();
            }
        }
    }

    /**
     * Signals the background thread to stop and restores button states.
     */
    private void onStop(ActionEvent e) {
        setRunningState(false);

        // 1. Stop the simulation engine
        engine.stopSimulation();

        // 2. Stop all living entity threads
        for (AbstractEntity entity : engine.getEnvironment().getEntities()) {
            if (entity instanceof LivingEntity) {
                ((LivingEntity) entity).stopThread();
            }
        }
    }

    private void onReset(ActionEvent e) {
        // First, explicitly stop all threads safely
        onStop(null);

        // Then reset the engine and update UI
        engine.reset();
        mapPanel.refresh(environment);
    }

    private void onAddEntity(ActionEvent e) {
        Window parent = SwingUtilities.getWindowAncestor(this);
        AddEntityDialog dialog = new AddEntityDialog(
                parent instanceof JFrame ? (JFrame) parent : null,
                environment);
        dialog.setVisible(true);

        if (dialog.wasConfirmed()) {
            for (AbstractEntity entity : engine.getEnvironment().getEntities()) {
                if (entity instanceof LivingEntity) {
                    LivingEntity le = (LivingEntity) entity;
                    le.setEngine(engine);
                    if (engine.isRunning()) {
                        le.startThread();
                    }
                }
            }
            mapPanel.refresh(environment);
        }
    }

    // ── Private helpers ──────────────────────────────────────────────────────


    /** Enables or disables controls to reflect running vs. idle state. */
    private void setRunningState(boolean isRunning) {
        btnTick  .setEnabled(!isRunning);
        btnRun   .setEnabled(!isRunning);
        btnStop  .setEnabled( isRunning);
        btnReset .setEnabled(!isRunning);
        btnAddEntity.setEnabled(!isRunning);
    }

    /**
     * Converts the slider position (0–100) to an inter-tick delay in ms.
     * Slider left = slow, slider right = fast.
     */
    private int sliderToDelayMs() {
        // Slider goes 0 (left = slow) to 100 (right = fast)
        int pos = speedSlider.getValue();
        // Map: 0 → DELAY_MAX_MS,  100 → DELAY_MIN_MS
        return DELAY_MAX_MS - (int)((DELAY_MAX_MS - DELAY_MIN_MS) * (pos / 100.0));
    }

    /** Creates the speed slider with labelled tick marks. */
    private JSlider buildSpeedSlider() {
        JSlider s = new JSlider(0, 100,
                (int)(100.0 * (DELAY_MAX_MS - DELAY_INIT_MS) / (DELAY_MAX_MS - DELAY_MIN_MS)));
        s.setMajorTickSpacing(50);
        s.setPaintTicks(true);
        s.setPaintLabels(false);
        s.setToolTipText("Left = slower, Right = faster");
        return s;
    }
    private void onAddRandomEntities(ActionEvent e) {
        java.util.Random rnd = new java.util.Random();
        int count = 5 + rnd.nextInt(6); // בוחר בין 5 ל-10 ישויות מכל סוג

        for (int i = 0; i < count; i++) {
            Position pos = new Position(rnd.nextInt(environment.getRows()), rnd.nextInt(environment.getCols()));

            int type = rnd.nextInt(5);
            AbstractEntity entity = null;

            switch (type) {
                case 0: entity = new ecosystem.entities.animals.Rabbit(pos); break;
                case 1: entity = new ecosystem.entities.animals.Deer(pos); break;
                case 2: entity = new ecosystem.entities.animals.Lion(pos); break;
                case 3: entity = new ecosystem.entities.plants.OakTree(pos); break;
                case 4: entity = new ecosystem.entities.plants.Flower(pos); break;
            }

            if (entity != null && environment.addEntity(entity)) {
                if (entity instanceof LivingEntity) {
                    LivingEntity le = (LivingEntity) entity;
                    le.setEngine(engine);
                    if (engine.isRunning()) {
                        le.startThread();
                    }
                }
            }
        }
        mapPanel.refresh(environment);
    }
}