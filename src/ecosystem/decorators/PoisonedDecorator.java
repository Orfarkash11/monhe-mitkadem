package ecosystem.decorators;

import ecosystem.core.Environment;
import ecosystem.entities.LivingEntity;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 * Pattern: Decorator
 * * A decorator that applies a poison effect, causing the entity to lose extra energy each tick.
 */
public class PoisonedDecorator extends EntityDecorator {

    /**
     * Constructs a poisoned decorator for the given entity.
     *
     * @param decoratedEntity the entity to be poisoned
     */
    public PoisonedDecorator(LivingEntity decoratedEntity) {
        super(decoratedEntity);
    }

    /**
     * Executes the entity's normal action and applies an additional energy penalty.
     *
     * @param env the current simulation environment
     * @return true if the entity is still alive and active, false otherwise
     */
    @Override
    public boolean act(Environment env) {
        if (!isAlive()) return false;

        boolean stillAlive = decoratedEntity.act(env);

        reduceEnergy(5);
        if (getEnergy() <= 0) {
            setAlive(false);
            return false;
        }

        return super.act(env) && stillAlive;
    }

    /**
     * Gets the name of the decorator effect.
     *
     * @return the string "Poisoned"
     */
    @Override
    protected String getDecoratorName() {
        return "Poisoned";
    }
}