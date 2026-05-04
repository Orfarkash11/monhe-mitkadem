package ecosystem.behaviors;
import ecosystem.entities.animals.Animal;
import ecosystem.core.Environment;

public class RandomMovement implements MovementStrategy {
    @Override
    public boolean Move(Animal entity, Environment env) {
        return false;
    }
}
