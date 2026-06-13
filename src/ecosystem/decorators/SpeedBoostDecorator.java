package ecosystem.decorators;

import ecosystem.core.Environment;
import ecosystem.entities.LivingEntity;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 * * Pattern: Decorator
 */
public class SpeedBoostDecorator extends EntityDecorator {

    public SpeedBoostDecorator(LivingEntity decoratedEntity) {
        super(decoratedEntity);
    }

    @Override
    public boolean act(Environment env) {
        if (!isAlive()) return false;

        // מפעיל את הפעולה המקורית פעמיים באותה פעימה!
        boolean alive1 = decoratedEntity.act(env);
        boolean alive2 = false;
        if (alive1) {
            alive2 = decoratedEntity.act(env);
        }

        return super.act(env) && (alive1 || alive2);
    }

    @Override
    protected String getDecoratorName() {
        return "SpeedBoost";
    }
}