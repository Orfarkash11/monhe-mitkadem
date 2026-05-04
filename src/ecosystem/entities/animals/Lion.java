package ecosystem.entities.animals;

import ecosystem.behaviors.MovementStrategy;
import ecosystem.core.Environment;
import ecosystem.core.Position;
import ecosystem.behaviors.ChaseMovement;
import ecosystem.behaviors.CarnivoreBehavior;
public class Lion extends Animal{
    public Lion (Position position, Environment environment) {
        super(position,environment);
        setEnergy(100);
        setSymbol('L');
        setMovementStrategy(new ChaseMovement());
        setFeedingBehavior(new CarnivoreBehavior());
    }

    @Override
    public boolean equals(Object o) {}
    @Override
    public String toString() {}
}
