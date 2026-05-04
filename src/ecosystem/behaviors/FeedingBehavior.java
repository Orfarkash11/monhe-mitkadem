package ecosystem.behaviors;

import ecosystem.entities.animals.Animal;
import ecosystem.entities.AbstractEntity;
import java.util.List;
public interface FeedingBehavior {
    boolean eat(Animal eater, List<AbstractEntity> nearby);
}
