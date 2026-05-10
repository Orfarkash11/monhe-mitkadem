package ecosystem.interfaces;
import ecosystem.core.Environment;

/**
 * Interface for entities that can perform an action each simulation tick.
 */
public interface Actable {
    /**
     * Performs the entity's action for the current tick.
     * @param env the environment the entity is in.
     * @return true if the action was successful, false otherwise.
     */
    boolean act(Environment env);
}
