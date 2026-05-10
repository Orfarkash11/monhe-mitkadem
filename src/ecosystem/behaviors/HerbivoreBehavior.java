package ecosystem.behaviors;

import ecosystem.entities.AbstractEntity;
import ecosystem.entities.animals.Animal;
import ecosystem.interfaces.Consumable;
import ecosystem.interfaces.EdibleByCarnivore;
import ecosystem.interfaces.EdibleByHerbivore;
import java.util.List;

/**
 * Feeding behavior for herbivores.
 */
public class HerbivoreBehavior implements FeedingBehavior {
    /**
     * Constructs a HerbivoreBehavior.
     */
    public HerbivoreBehavior() {}

    @Override
    public boolean eat(Animal eater, List<AbstractEntity> nearby) {
        if (eater == null || nearby == null) return false;

        // 1. Prioritize adjacent EdibleByHerbivore
        for (AbstractEntity entity : nearby) {
            // Check if adjacent (Manhattan distance 1)
            if (eater.getPosition().distanceTo(entity.getPosition()) == 1) {
                if (entity != eater && entity.isAlive() && entity instanceof Consumable && entity instanceof EdibleByHerbivore) {
                    return eater.eat((Consumable) entity);
                }
            }
        }

        // 2. Fallback: Check for water or non-categorized resources
        for (AbstractEntity entity : nearby) {
            if (eater.getPosition().distanceTo(entity.getPosition()) == 1) {
                if (entity != eater && entity.isAlive() && entity instanceof Consumable && !(entity instanceof EdibleByHerbivore) && !(entity instanceof EdibleByCarnivore)) {
                    return eater.eat((Consumable) entity);
                }
            }
        }

        return false;
    }

    /**
     * Checks equality based on class type.
     */
    @Override
    public boolean equals(Object o) {
        return o != null && getClass() == o.getClass();
    }

    /**
     * Returns class name as string.
     */
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}