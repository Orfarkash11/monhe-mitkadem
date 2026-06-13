package ecosystem.decorators;

import ecosystem.core.Environment;
import ecosystem.entities.LivingEntity;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 * * Pattern: Decorator
 */
public class PoisonedDecorator extends EntityDecorator {

    public PoisonedDecorator(LivingEntity decoratedEntity) {
        super(decoratedEntity);
    }

    @Override
    public boolean act(Environment env) {
        if (!isAlive()) return false;

        // מפעיל את הפעולה המקורית
        boolean stillAlive = decoratedEntity.act(env);

        // מוריד עוד 5 אנרגיה כקנס על הרעל בעזרת הפונקציה הציבורית
        reduceEnergy(5);
        if (getEnergy() <= 0) {
            setAlive(false);
            return false;
        }

        return super.act(env) && stillAlive;
    }

    @Override
    protected String getDecoratorName() {
        return "Poisoned";
    }
}