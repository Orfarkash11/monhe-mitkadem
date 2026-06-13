package ecosystem.states;

import ecosystem.core.Environment;
import ecosystem.core.Position;
import ecosystem.entities.LivingEntity;
import ecosystem.entities.animals.Animal;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 * Pattern: State
 * * Represents an idle state where the animal moves randomly and loses minimal energy.
 */
public class IdleState implements EntityState {

    /**
     * Executes the idle behavior, consisting of random movement and slow energy loss.
     *
     * @param entity the living entity performing the action
     */
    @Override
    public void doAction(LivingEntity entity) {
        if (!(entity instanceof Animal)) return;
        Animal animal = (Animal) entity;

        animal.reduceEnergy(1);

        Environment env = animal.getEngine() != null ? animal.getEngine().getEnvironment() : null;
        if (env != null) {
            if (Math.random() > 0.5) {
                animal.move(env);
            }

            checkSleepingCondition(animal, env);
        }

        if (animal.getEnergy() < animal.getMaxEnergy() * 0.3) {
            animal.setState(new HungryState());
        }
    }

    /**
     * Checks if the animal is in a corner of the environment to transition to a sleeping state.
     *
     * @param animal the animal to check
     * @param env    the current environment
     */
    private void checkSleepingCondition(Animal animal, Environment env) {
        Position pos = animal.getPosition();
        if (pos == null) return;
        int r = pos.getRow();
        int c = pos.getCol();
        boolean isCorner = (r == 0 && c == 0) ||
                (r == 0 && c == env.getCols() - 1) ||
                (r == env.getRows() - 1 && c == 0) ||
                (r == env.getRows() - 1 && c == env.getCols() - 1);
        if (isCorner) {
            animal.setState(new SleepingState());
        }
    }
}