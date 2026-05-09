package ecosystem.behaviors;

import ecosystem.entities.animals.Animal;
import ecosystem.entities.AbstractEntity;
import ecosystem.interfaces.EdibleByCarnivore;
import ecosystem.interfaces.Consumable;
import java.util.List;

public class CarnivoreBehavior implements FeedingBehavior {

    @Override
    public boolean eat(Animal eater, List<AbstractEntity> nearby) {
        AbstractEntity closest = null;
        int mindis = Integer.MAX_VALUE;

        for (AbstractEntity entity : nearby) {
            if (entity instanceof EdibleByCarnivore && entity.isAlive()) {
                int dist = eater.getPosition().distanceTo(entity.getPosition());
                if (dist < mindis) {
                    mindis = dist;
                    closest = entity;
                }
            }
        }

        if (closest != null) {
            return eater.eat((Consumable) closest);
        }
        return false;
    }
}