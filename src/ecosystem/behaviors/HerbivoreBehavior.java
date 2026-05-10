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
    @Override
    public boolean eat(Animal eater, List<AbstractEntity> nearby) {
        if (eater == null || nearby == null) return false;

        AbstractEntity target = null;
        int minDist = Integer.MAX_VALUE;

        // 1. Try to find nearest adjacent EdibleByHerbivore
        for (AbstractEntity entity : nearby) {
            if (entity != eater && entity.isAlive() && entity instanceof Consumable && entity instanceof EdibleByHerbivore) {
                int dist = eater.getPosition().distanceTo(entity.getPosition());
                if (dist <= 1 && dist < minDist) {
                    minDist = dist;
                    target = entity;
                }
            }
        }

        // 2. If no plants, try adjacent Consumable resources (like Water)
        if (target == null) {
            for (AbstractEntity entity : nearby) {
                if (entity != eater && entity.isAlive() && entity instanceof Consumable && !(entity instanceof EdibleByHerbivore) && !(entity instanceof EdibleByCarnivore)) {
                    int dist = eater.getPosition().distanceTo(entity.getPosition());
                    if (dist <= 1 && dist < minDist) {
                        minDist = dist;
                        target = entity;
                    }
                }
            }
        }

        if (target != null) {
            return eater.eat((Consumable) target);
        }

        return false;
    }
}