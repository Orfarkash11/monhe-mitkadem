package ecosystem.states;

import ecosystem.commands.Command;
import ecosystem.core.Environment;
import ecosystem.core.Position;
import ecosystem.entities.AbstractEntity;
import ecosystem.entities.LivingEntity;
import ecosystem.entities.animals.Animal;
import java.util.List;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 * Pattern: State
 * * Represents a state where the animal actively seeks food and loses energy rapidly.
 */
public class HungryState implements EntityState {

    /**
     * Executes the hungry behavior, which includes seeking food, losing energy,
     * and potentially transitioning to other states.
     *
     * @param entity the living entity performing the action
     */
    @Override
    public void doAction(LivingEntity entity) {
        if (!(entity instanceof Animal)) return;
        Animal animal = (Animal) entity;

        animal.reduceEnergy(5);

        Environment env = animal.getEngine() != null ? animal.getEngine().getEnvironment() : null;
        if (env != null) {
            List<AbstractEntity> nearby = animal.sense(env);

            Command eatCmd = animal.getFeedingBehavior() != null ?
                    animal.getFeedingBehavior().getCommand(animal, nearby) : null;

            if (eatCmd != null && animal.getEngine() != null) {
                animal.getEngine().submitCommand(eatCmd);
            } else {
                if (animal.getEnergy() < animal.getMaxEnergy() / 2 && animal.getEngine() != null) {
                    synchronized (animal.getEngine().resourceLock) {
                        try {
                            animal.getEngine().resourceLock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                }
                animal.move(env);
            }

            checkSleepingCondition(animal, env);
        }

        if (animal.getEnergy() > animal.getMaxEnergy() * 0.8) {
            animal.setState(new IdleState());
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