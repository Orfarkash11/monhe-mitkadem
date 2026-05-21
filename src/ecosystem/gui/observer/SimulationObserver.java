package ecosystem.gui.observer;

/**
 * Observer interface for the Simulation MVC pattern.
 *
 * <p>Any GUI component that needs to react to model state changes
 * must implement this interface and register itself with the
 * {@link ecosystem.core.SimulationEngine}.</p>
 *
 * <p>All methods on concrete implementations MUST be called via
 * {@code SwingUtilities.invokeLater} when triggered from a background
 * thread to remain thread-safe.</p>
 */
public interface SimulationObserver {

    /**
     * Called by the model after every state change (e.g., end of a tick,
     * entity added, or simulation reset).
     *
     * @param event a snapshot of the simulation state at the moment of notification;
     *              never {@code null}
     */
    void onSimulationUpdated(SimulationEvent event);
}