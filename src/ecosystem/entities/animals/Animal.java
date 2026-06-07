package ecosystem.entities.animals;

import ecosystem.behaviors.FeedingBehavior;
import ecosystem.behaviors.MovementStrategy;
import ecosystem.commands.Command;
import ecosystem.core.Environment;
import ecosystem.core.Position;
import ecosystem.entities.AbstractEntity;
import ecosystem.entities.LivingEntity;
import ecosystem.interfaces.*;
import java.util.List;
/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 * Base class for all animals in the ecosystem.
 * Animals move, sense surroundings, and eat other entities.
 */
public abstract class Animal extends LivingEntity implements EdibleByCarnivore, Movable, Eater, Sensory, Consumable {
    private int visionRange = 2;
    private MovementStrategy movementStrategy;
    private FeedingBehavior feedingBehavior;

    public Animal(Position position, char symbol, int energy, int maxEnergy,
                  MovementStrategy movementStrategy, FeedingBehavior feedingBehavior) {
        super(position, symbol, energy, maxEnergy, null);
        this.movementStrategy = movementStrategy;
        this.feedingBehavior = feedingBehavior;
    }

    @Override
    public int getNutritionValue() { return (int) (getEnergy() * 0.8); }

    @Override
    public boolean onConsumed() { return setAlive(false); }

    @Override
    public List<AbstractEntity> sense(Environment env) {
        if (env == null) return new java.util.ArrayList<>();
        return env.getNearbyEntities(getPosition());
    }

    @Override
    public boolean move(Environment env) {
        if (movementStrategy != null && getEngine() != null) {
            Command moveCmd = movementStrategy.getCommand(this, env);
            if (moveCmd != null) {
                getEngine().submitCommand(moveCmd);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean eat(Consumable target) {
        if (target == null) return false;
        int nutrition = target.getNutritionValue();
        setEnergy(getEnergy() + nutrition);
        return target.onConsumed();
    }

    @Override
    public boolean act(Environment env) {
        if (!super.act(env)) return false;

        List<AbstractEntity> nearby = sense(env);
        Command eatCmd = (feedingBehavior != null) ? feedingBehavior.getCommand(this, nearby) : null;

        if (eatCmd != null && getEngine() != null) {
            getEngine().submitCommand(eatCmd);
        } else if (getEnergy() < getMaxEnergy() / 2 && getEngine() != null) {
            synchronized (getEngine().resourceLock) {
                try {
                    getEngine().resourceLock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
        }

        move(env);
        return true;
    }
}