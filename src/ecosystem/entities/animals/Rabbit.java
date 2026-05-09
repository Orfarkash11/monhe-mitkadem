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

    @Override
    public boolean act(Environment env) {
        if (!super.act(env)) {
            return false;
        }

        if (this.getEnergy() >= 30 && Math.random() <= 0.3) {
            this.reproduce(env);
        }

        return true;
    }

    @Override
    public boolean reproduce(Environment env) {
        return false;
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
        return "Rabbit " + getPosition() + " " + getEnergy() + " " + isAlive();
    }
}