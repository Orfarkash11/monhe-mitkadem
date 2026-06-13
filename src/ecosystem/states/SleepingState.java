package ecosystem.states;

import ecosystem.entities.LivingEntity;
import ecosystem.entities.animals.Animal;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 * * Pattern: State
 * Animal rests in the corners of the map and recovers energy.
 */
public class SleepingState implements EntityState {
    private static final int SLEEP_DURATION = 5;
    private int sleepCounter = 0;

    @Override
    public void doAction(LivingEntity entity) {
        if (!(entity instanceof Animal)) return;
        Animal animal = (Animal) entity;

        // העלאת אנרגיה תוך הגבלה שלא תעבור את המקסימום המותר של החיה
        int newEnergy = Math.min(animal.getEnergy() + 2, animal.getMaxEnergy());
        animal.setEnergy(newEnergy);

        sleepCounter++;
        if (sleepCounter >= SLEEP_DURATION) {
            animal.setState(new IdleState());
        }
    }
}