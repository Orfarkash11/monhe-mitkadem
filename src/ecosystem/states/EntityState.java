package ecosystem.states;

import ecosystem.entities.LivingEntity;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 * * Pattern: State
 */
public interface EntityState {
    void doAction(LivingEntity entity);
}