package ecosystem.states;

import ecosystem.entities.LivingEntity;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 * Pattern: State
 * * Interface representing the state of a living entity.
 */
public interface EntityState {
    /**
     * Executes the action associated with the current state.
     *
     * @param entity the living entity performing the action
     */
    void doAction(LivingEntity entity);
}