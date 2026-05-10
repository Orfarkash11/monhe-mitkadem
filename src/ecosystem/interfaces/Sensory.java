package ecosystem.interfaces;
import ecosystem.core.Environment;
import ecosystem.entities.AbstractEntity;

import java.util.List;

/**
 * Interface for entities that can sense their surroundings.
 */
public interface Sensory {
    /**
     * Senses nearby entities in the environment.
     * @param env the environment to sense.
     * @return a list of detected entities.
     */
    List<AbstractEntity> sense(Environment env);
}
