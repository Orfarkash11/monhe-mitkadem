package ecosystem.states;

import ecosystem.entities.LivingEntity;
import ecosystem.entities.animals.Animal;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 * Pattern: State
 * * Represents a state where the animal rests, recovers energy, and remains stationary.
 */
public class SleepingState implements EntityState {

    /**
     * The fixed duration of the sleeping state in ticks.
     */
    private static final int SLEEP_DURATION = 5;

    private int sleepCounter = 0;

    /**
     * Executes the sleeping behavior, recovering energy for a fixed duration.
     *
     * @param entity the living entity performing the action
     */
    @Override
    public void doAction(LivingEntity entity) {
        if (!(entity instanceof Animal)) return;
        Animal animal = (Animal) entity;

        int newEnergy = Math.min(animal.getEnergy() + 2, animal.getMaxEnergy());
        animal.setEnergy(newEnergy);

        sleepCounter++;
        if (sleepCounter >= SLEEP_DURATION) {
            animal.setState(new IdleState());
        }
    }
}