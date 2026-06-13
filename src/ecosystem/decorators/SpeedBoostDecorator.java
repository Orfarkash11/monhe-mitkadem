package ecosystem.decorators;

import ecosystem.core.Environment;
import ecosystem.entities.LivingEntity;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 * Pattern: Decorator
 * * A decorator that applies a speed boost, allowing the entity to act twice per tick.
 */
public class SpeedBoostDecorator extends EntityDecorator {

    /**
     * Constructs a speed boost decorator for the given entity.
     *
     * @param decoratedEntity the entity to receive the speed boost
     */
    public SpeedBoostDecorator(LivingEntity decoratedEntity) {
        super(decoratedEntity);
    }

    /**
     * Executes the entity's normal action twice in a single tick.
     *
     * @param env the current simulation environment
     * @return true if the entity remains active, false otherwise
     */
    @Override
    public boolean act(Environment env) {
        if (!isAlive()) return false;

        boolean alive1 = decoratedEntity.act(env);
        boolean alive2 = false;
        if (alive1) {
            alive2 = decoratedEntity.act(env);
        }

        return super.act(env) && (alive1 || alive2);
    }

    /**
     * Gets the name of the decorator effect.
     *
     * @return the string "SpeedBoost"
     */
    @Override
    protected String getDecoratorName() {
        return "SpeedBoost";
    }
}