package ecosystem.behaviors;

import ecosystem.entities.AbstractEntity;
import ecosystem.entities.animals.Animal;
import java.util.List;

/**
 * Interface for feeding logic used by animals.
 */
public interface FeedingBehavior {
    /**
     * Attempts to find and eat a nearby consumable.
     * @param eater the animal eating.
     * @param nearby list of nearby detected entities.
     * @return true if eating was successful.
     */
    boolean eat(Animal eater, List<AbstractEntity> nearby);
}
