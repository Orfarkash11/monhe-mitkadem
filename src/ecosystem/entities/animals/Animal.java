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
import ecosystem.states.EntityState;
import ecosystem.states.IdleState;
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
    private EntityState currentState;

    public Animal(Position position, char symbol, int energy, int maxEnergy,
                  MovementStrategy movementStrategy, FeedingBehavior feedingBehavior) {
        super(position, symbol, energy, maxEnergy, null);
        this.movementStrategy = movementStrategy;
        this.feedingBehavior = feedingBehavior;
        this.currentState = new IdleState();
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
        if (!isAlive()) return false;

        incrementAge();

        if (currentState != null) {
            currentState.doAction(this);
        }

        if (getEnergy() <= 0) {
            setEnergy(0);
            setAlive(false);
        }

        return isAlive();
    }
    public void setState(EntityState state) {
        this.currentState = state;
    }

    public EntityState getState() {
        return currentState;
    }

    public FeedingBehavior getFeedingBehavior() {
        return feedingBehavior;
    }
}