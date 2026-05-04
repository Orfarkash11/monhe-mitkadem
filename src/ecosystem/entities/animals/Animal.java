package ecosystem.entities.animals;

import ecosystem.behaviors.FeedingBehavior;
import ecosystem.behaviors.MovementStrategy;
import ecosystem.core.Environment;
import ecosystem.entities.LivingEntity;
import ecosystem.interfaces.*;

import java.util.function.Consumer;

public class Animal extends LivingEntity implements EdibleByCarnivore, Movable, Consumer, Eater, Sensory {
    private int visionRange=2;
    private MovementStrategy movementStrategy;
    private FeedingBehavior feedingBehavior;

    @Override
    public boolean move (Environment env){}

    @Override
    public boolean eat (Consumable target){}

    @Override
    public boolean onConsumed(){}
}
