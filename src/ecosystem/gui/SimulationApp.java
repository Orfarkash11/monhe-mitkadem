package ecosystem.gui;

import ecosystem.core.Environment;
import ecosystem.core.SimulationEngine;

import javax.swing.*;

/**
 * Application entry point.
 *
 * <p>Bootstraps the model, then hands off to the EDT for all GUI
 * construction (Swing is not thread-safe and must be initialised on the EDT).</p>
 */
public final class SimulationApp {

    private SimulationApp() { /* utility class */ }

    /**
     * Main method — start here.
     *
     * @param args command-line arguments (ignored)
     */
    public static void main(String[] args) {
        // Construct the model on the calling thread (safe — no Swing yet)
        Environment      env    = new Environment(20, 20); // adjust dimensions
        SimulationEngine engine = new SimulationEngine(env);

        // Optionally seed the simulation with starting entities:
        // engine.seed();

        // All Swing work on the EDT
        SwingUtilities.invokeLater(() -> new MainFrame(engine, env));
    }
}