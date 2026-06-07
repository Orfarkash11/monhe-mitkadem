package ecosystem.behaviors;
import ecosystem.commands.Command;
import ecosystem.entities.animals.Animal;
import ecosystem.core.Environment;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 * Interface for movement algorithms used by animals.
 */
public interface MovementStrategy {
    Command getCommand(Animal entity, Environment env);
}
