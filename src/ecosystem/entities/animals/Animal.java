package ecosystem.entities.animals;

import ecosystem.behaviors.FeedingBehavior;
import ecosystem.behaviors.MovementStrategy;
import ecosystem.core.Environment;
import ecosystem.core.Position;
import ecosystem.entities.AbstractEntity;
import ecosystem.entities.LivingEntity;
import ecosystem.interfaces.*;
import java.util.List;

public abstract class Animal extends LivingEntity implements EdibleByCarnivore, Movable, Eater, Sensory {
    private int visionRange = 2;
    private MovementStrategy movementStrategy;
    private FeedingBehavior feedingBehavior;

    public Animal(Position position, char symbol, int energy, int maxEnergy,
                  MovementStrategy movementStrategy, FeedingBehavior feedingBehavior) {
        super();
    }

    protected boolean setMovementStrategy(MovementStrategy ms) {
        this.movementStrategy = ms;
        return true;
    }

    protected boolean setFeedingBehavior(FeedingBehavior fb) {
        this.feedingBehavior = fb;
        return true;
    }

    @Override
    public int getNutritionValue() {
        return (int) (this.getEnergy() * 0.8);
    }

    @Override
    public boolean onConsumed() {
        this.setAlive(false);
        return true;
    }

    @Override
    public List<AbstractEntity> sense(Environment env) {
        return env.getNearbyEntities(this.getPosition());
    }

    @Override
    public boolean move(Environment env) {
        return this.movementStrategy.Move(this, env);
    }

    @Override
    public boolean eat(Consumable target) {
        if (target != null) {
            this.setEnergy(this.getEnergy() + target.getNutritionValue());
            target.onConsumed();
            return true;
        }
        return false;
    }

    @Override
    public boolean act(Environment env) {
        if (!super.act(env)) {
            return false;
        }

        List<AbstractEntity> nearby = this.sense(env);
        this.move(env);
        this.feedingBehavior.eat(this, nearby);

        return true;
    }
}