package ecosystem.gui;

import ecosystem.core.Environment;
import ecosystem.core.SimulationEngine;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

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
    private volatile Thread runThread  = null;
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

        JLabel speedLabel = new JLabel("Speed:", SwingConstants.LEFT);
        c.gridy = 5; add(speedLabel,   c);
        c.gridy = 6; add(speedSlider,  c);
    }

    // ── Action handlers ──────────────────────────────────────────────────────

    /**
     * Executes exactly one simulation tick synchronously.
     * Safe to call from the EDT because tick() is fast and non-blocking.
     */
    private void onSingleTick(ActionEvent e) {
        if (!running) {
            engine.tick();
        }
    }

    /**
     * Starts the continuous run loop in a background daemon thread.
     * The thread reads the slider value for inter-tick delay and calls
     * {@link SimulationEngine#tick()} in a loop until {@link #stopRun()} is invoked.
     */
    private void onContinuousRun(ActionEvent e) {
        if (running) return;
        running   = true;
        setRunningState(true);

        runThread = new Thread(() -> {
            while (running) {
                engine.tick();   // Model notifies observers via SwingUtilities.invokeLater

                int delayMs = sliderToDelayMs();
                try {
                    Thread.sleep(delayMs);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            SwingUtilities.invokeLater(() -> setRunningState(false));
        }, "SimulationRunThread");

        runThread.setDaemon(true);
        runThread.start();
    }

    /**
     * Signals the background thread to stop and restores button states.
     */
    private void onStop(ActionEvent e) {
        stopRun();
    }

    /**
     * Stops any running loop, resets the model, and refreshes the view.
     */
    private void onReset(ActionEvent e) {
        stopRun();
        engine.reset();              // Engine calls notifyObservers() → EDT refresh
    }

    /**
     * Opens the {@link AddEntityDialog} and, if the user confirmed an entity,
     * adds it to the environment and triggers a manual view refresh.
     */
    private void onAddEntity(ActionEvent e) {
        Window parent = SwingUtilities.getWindowAncestor(this);
        AddEntityDialog dialog = new AddEntityDialog(
                parent instanceof JFrame ? (JFrame) parent : null,
                environment);
        dialog.setVisible(true);   // blocks until dialog is closed

        if (dialog.wasConfirmed()) {
            // The dialog already added the entity to the environment;
            // force a repaint (the next tick will also do this automatically)
            mapPanel.refresh(environment);
        }
    }

    // ── Private helpers ──────────────────────────────────────────────────────

    /** Stops the run loop and interrupts the thread. */
    private void stopRun() {
        running = false;
        if (runThread != null) {
            runThread.interrupt();
            runThread = null;
        }
    }

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
}