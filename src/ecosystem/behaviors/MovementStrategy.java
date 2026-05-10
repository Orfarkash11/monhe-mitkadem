package ecosystem.behaviors;

import ecosystem.entities.animals.Animal;
import ecosystem.core.Environment;

/**
 * Interface for movement algorithms used by animals.
 */
public interface MovementStrategy {
    /**
     * Attempts to move an animal within the environment.
     * @param entity the animal to move.
     * @param env the environment.
     * @return true if a move was performed.
     */
    boolean move(Animal entity, Environment env);
}
