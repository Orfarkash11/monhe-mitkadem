package ecosystem.interfaces;
import ecosystem.core.Environment;

/**
 * Interface for entities that can move within the ecosystem.
 */
public interface Movable {
    /**
     * Moves the entity within the environment.
     * @param env the environment the entity is in.
     * @return true if the movement was successful, false otherwise.
     */
    boolean move(Environment env);
}
