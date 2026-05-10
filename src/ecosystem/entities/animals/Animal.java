package ecosystem.entities.animals;

import ecosystem.behaviors.FeedingBehavior;
import ecosystem.behaviors.MovementStrategy;
import ecosystem.core.Environment;
import ecosystem.core.Position;
import ecosystem.entities.AbstractEntity;
import ecosystem.entities.LivingEntity;
import ecosystem.interfaces.*;
import java.util.List;

/**
 * Base class for all animals in the ecosystem.
 * Animals move, sense surroundings, and eat other entities.
 */
public abstract class Animal extends LivingEntity implements EdibleByCarnivore, Movable, Eater, Sensory, Consumable {
    private int visionRange = 2;
    private MovementStrategy movementStrategy;
    private FeedingBehavior feedingBehavior;

    /**
     * Constructs an Animal.
     * @param position initial position.
     * @param symbol character representation.
     * @param energy initial energy.
     * @param maxEnergy maximum energy.
     * @param movementStrategy strategy for moving.
     * @param feedingBehavior strategy for eating.
     */
    public Animal(Position position, char symbol, int energy, int maxEnergy,
                  MovementStrategy movementStrategy, FeedingBehavior feedingBehavior) {
        super(position, symbol, energy, maxEnergy, null);
        this.movementStrategy = movementStrategy;
        this.feedingBehavior = feedingBehavior;
    }

    /**
     * @return 80% of current energy as nutrition.
     */
    @Override
    public int getNutritionValue() {
        return (int) (getEnergy() * 0.8);
    }

    /**
     * Sets the animal to dead when consumed.
     * @return true always.
     */
    @Override
    public boolean onConsumed() {
        return setAlive(false);
    }

    /**
     * Senses nearby entities in the environment.
     * @param env the environment.
     * @return list of detected entities.
     */
    @Override
    public List<AbstractEntity> sense(Environment env) {
        if (env == null) return new java.util.ArrayList<>();
        return env.getNearbyEntities(getPosition());
    }

    /**
     * Delegates movement to the movement strategy.
     * @param env the environment.
     * @return true if moved successfully.
     */
    @Override
    public boolean move(Environment env) {
        if (movementStrategy != null) {
            return movementStrategy.move(this, env);
        }
        return false;
    }

    /**
     * Consumes a target, gaining energy up to maxEnergy.
     * @param target the consumable to eat.
     * @return true if target was consumed.
     */
    @Override
    public boolean eat(Consumable target) {
        if (target == null) return false;
        
        int nutrition = target.getNutritionValue();
        setEnergy(getEnergy() + nutrition); // setEnergy handles capping at maxEnergy in LivingEntity
        return target.onConsumed();
    }

    /**
     * Performs the animal's action cycle: age, move, eat.
     * @param env the environment.
     * @return true if the cycle completed (even if move/eat failed).
     */
    @Override
    public boolean act(Environment env) {
        // 1. Biological update
        if (!super.act(env)) {
            return false;
        }

        // 2. Sense
        List<AbstractEntity> nearby = sense(env);

        // 3. Move
        move(env);

        // 4. Eat
        if (feedingBehavior != null) {
            feedingBehavior.eat(this, nearby);
        }

        return true;
    }

    /**
     * @return the current vision range.
     */
    protected int getVisionRange() {
        return visionRange;
    }
}