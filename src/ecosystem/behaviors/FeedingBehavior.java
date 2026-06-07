package ecosystem.behaviors;
import ecosystem.commands.Command;
import ecosystem.entities.AbstractEntity;
import ecosystem.entities.animals.Animal;
import java.util.List;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 * Interface for feeding logic used by animals.
 */
public interface FeedingBehavior {
    Command getCommand(Animal eater, List<AbstractEntity> nearby);
}
