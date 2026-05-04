package ecosystem.entities.animals;
import ecosystem.core.Environment;
import ecosystem.core.Position;
import ecosystem.behaviors.EscapeMovement;
import ecosystem.behaviors.HerbivoreBehavior;
public class Deer extends Animal{
    public Deer(Position position, Environment environment) {
        super(position, environment);
        setEnergy(70);
        setSymbol('D');
        setMovementStrategy(new EscapeMovement());
        setFeedingBehavior(new HerbivoreBehavior());
    }
    @Override
    public boolean equals(Object o) {}
    @Override
    public String toString() {}
}
