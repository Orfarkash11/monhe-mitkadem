package ecosystem.gui.observer;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
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