package ecosystem.gui.observer;

import ecosystem.core.Environment;
/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 */
public final class SimulationEvent {
    /** Total number of ticks elapsed since simulation start or last reset. */
    private final int tickCount;

    /** Live reference to the environment grid. GUI must treat this as read-only. */
    private final Environment environment;

    /**
     * Constructs a new event snapshot.
     *
     * @param tickCount   number of completed ticks
     * @param environment live environment reference
     */
    public SimulationEvent(int tickCount, Environment environment) {
        this.tickCount   = tickCount;
        this.environment = environment;
    }

    /**
     * Returns the total number of ticks completed so far.
     *
     * @return tick count (≥ 0)
     */
    public int getTickCount() {
        return tickCount;
    }

    /**
     * Returns the live environment.
     *
     * @return environment; never {@code null}
     */
    public Environment getEnvironment() {
        return environment;
    }
}
