package ecosystem.states;

import ecosystem.core.Environment;
import ecosystem.core.Position;
import ecosystem.entities.LivingEntity;
import ecosystem.entities.animals.Animal;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 * * Pattern: State
 * Animal moves randomly or stands still, losing 1 energy per tick.
 */
public class IdleState implements EntityState {

    @Override
    public void doAction(LivingEntity entity) {
        if (!(entity instanceof Animal)) return;
        Animal animal = (Animal) entity;

        // איבוד 1 אנרגיה כפי שנדרש
        animal.reduceEnergy(1);

        Environment env = animal.getEngine() != null ? animal.getEngine().getEnvironment() : null;
        if (env != null) {
            // מסתובב רנדומלית או עומד במקום (50% סיכוי לזוז)
            if (Math.random() > 0.5) {
                animal.move(env);
            }

            // בדיקה האם הגענו לפינה כדי לעבור לישון
            checkSleepingCondition(animal, env);
        }

        // מעבר למצב רעב אם האנרגיה צונחת מתחת ל-30%
        if (animal.getEnergy() < animal.getMaxEnergy() * 0.3) {
            animal.setState(new HungryState());
        }
    }

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