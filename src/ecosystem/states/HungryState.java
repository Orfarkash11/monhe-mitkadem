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
 * * Pattern: State
 * Animal seeks food actively and loses 5 energy per tick.
 */
public class HungryState implements EntityState {

    @Override
    public void doAction(LivingEntity entity) {
        if (!(entity instanceof Animal)) return;
        Animal animal = (Animal) entity;

        // איבוד 5 אנרגיה כפי שנדרש בהנחיות
        animal.reduceEnergy(5);

        Environment env = animal.getEngine() != null ? animal.getEngine().getEnvironment() : null;
        if (env != null) {
            List<AbstractEntity> nearby = animal.sense(env);

            // מנסה למצוא אוכל
            Command eatCmd = animal.getFeedingBehavior() != null ?
                    animal.getFeedingBehavior().getCommand(animal, nearby) : null;

            if (eatCmd != null && animal.getEngine() != null) {
                animal.getEngine().submitCommand(eatCmd);
            } else {
                // אם רעב מאוד ואין אוכל - נכנס למצב המתנה (Wait) כדי למנוע Deadlock/לולאת סרק מהמטלה הקודמת
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
                // נע לכיוון משאבים/חיפוש אוכל
                animal.move(env);
            }

            // בדיקת פינות לשינה
            checkSleepingCondition(animal, env);
        }

        // מעבר חזרה למצב בטלה אם האנרגיה התמלאה מעל 80%
        if (animal.getEnergy() > animal.getMaxEnergy() * 0.8) {
            animal.setState(new IdleState());
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