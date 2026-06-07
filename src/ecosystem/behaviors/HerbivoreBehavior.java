package ecosystem.behaviors;

import ecosystem.commands.Command;
import ecosystem.commands.EatCommand;
import ecosystem.entities.AbstractEntity;
import ecosystem.entities.animals.Animal;
import ecosystem.interfaces.Consumable;
import ecosystem.interfaces.EdibleByHerbivore;
import ecosystem.interfaces.EdibleByCarnivore;
import java.util.List;
/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 * Feeding behavior for herbivores.
 */
public class HerbivoreBehavior implements FeedingBehavior {
    @Override
    public Command getCommand(Animal eater, List<AbstractEntity> nearby) {
        if (eater == null || nearby == null) return null;

        for (AbstractEntity entity : nearby) {
            if (eater.getPosition().distanceTo(entity.getPosition()) == 1) {
                if (entity != eater && entity.isAlive() && entity instanceof Consumable && entity instanceof EdibleByHerbivore) {
                    return new EatCommand(eater, (Consumable) entity);
                }
            }
        }

        for (AbstractEntity entity : nearby) {
            if (eater.getPosition().distanceTo(entity.getPosition()) == 1) {
                if (entity != eater && entity.isAlive() && entity instanceof Consumable && !(entity instanceof EdibleByHerbivore) && !(entity instanceof EdibleByCarnivore)) {
                    return new EatCommand(eater, (Consumable) entity);
                }
            }
        }
        return null;
    }
}