package ecosystem.entities.animals;

import ecosystem.behaviors.FeedingBehavior;
import ecosystem.behaviors.MovementStrategy;
import ecosystem.core.Environment;
import ecosystem.core.Position;
import ecosystem.entities.LivingEntity;
import ecosystem.interfaces.*;

import java.util.function.Consumer;

public class Animal extends LivingEntity implements EdibleByCarnivore, Movable, Consumer, Eater, Sensory {
    private int visionRange=2;
    private MovementStrategy movementStrategy;
    private FeedingBehavior feedingBehavior;

    public Animal(Position position, Environment environment) {
        super(position, environment);
    }

    protected boolean setMovementStrategy(MovementStrategy ms) {
        this.movementStrategy = ms;
        return true;
    }
    protected boolean setFeedingBehavior(FeedingBehavior mb) {
        this.feedingBehavior = mb;
        return true;
    }

    @Override
    public boolean move (Environment env){}

    @Override
    public boolean eat (Consumable target){}

    @Override
    public boolean onConsumed(){}
}
