package ecosystem.entities.animals;

import ecosystem.core.Environment;
import ecosystem.core.Position;
import ecosystem.behaviors.ChaseMovement;
import ecosystem.behaviors.CarnivoreBehavior;

public class Lion extends Animal {
    public Lion(Position position, Environment environment) {
        super(position, environment);
        setEnergy(100);
        setSymbol('L');
        setMovementStrategy(new ChaseMovement());
        setFeedingBehavior(new CarnivoreBehavior());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o != null && this.getClass() == o.getClass()) {
            return this.toString().equals(o.toString());
        }
        return false;
    }

    @Override
    public String toString() {
        return "Lion " + getPosition() + " " + getEnergy() + " " + isAlive();
    }
}

