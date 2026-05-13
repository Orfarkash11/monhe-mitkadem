package ecosystem.interfaces;
import ecosystem.core.Environment;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
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
