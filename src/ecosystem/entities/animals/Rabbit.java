package ecosystem.entities.animals;
import ecosystem.core.Environment;
import ecosystem.core.Position;
import ecosystem.behaviors.RandomMovement;
import ecosystem.behaviors.HerbivoreBehavior;
import ecosystem.interfaces.Reproducible;
public class Rabbit extends Animal implements Reproducible {
    public Rabbit(Position position, Environment environment) {
        super(position, environment);
        setEnergy(50);
        setSymbol('R');
        setMovementStrategy(new RandomMovement());
        setFeedingBehavior(new HerbivoreBehavior());
    }

}