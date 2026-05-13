package ecosystem.behaviors;

import ecosystem.core.Environment;
import ecosystem.core.Position;
import ecosystem.entities.animals.Animal;
import java.util.Random;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 * Strategy for moving to a random adjacent free cell.
 */
public class RandomMovement implements MovementStrategy {
    private static final Random random = new Random();

    /**
     * Constructs a RandomMovement strategy.
     */
    public RandomMovement() {}

    @Override
    public boolean move(Animal entity, Environment env) {
        Position current = entity.getPosition();
        if (current == null) return false;

        int row = current.getRow();
        int col = current.getCol();

        int[][] directions = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},           {0, 1},
            {1, -1},  {1, 0},  {1, 1}
        };

        // Try directions in random order
        int startIdx = random.nextInt(directions.length);
        for (int i = 0; i < directions.length; i++) {
            int[] dir = directions[(startIdx + i) % directions.length];
            int r = row + dir[0];
            int c = col + dir[1];
            
            if (r >= 0 && c >= 0) {
                Position target = new Position(r, c);
                if (env.isInsideBounds(target) && env.isPositionFree(target)) {
                    return env.moveEntity(entity, target);
                }
            }
        }
        return false;
    }

    /**
     * Checks equality based on class type.
     */
    @Override
    public boolean equals(Object o) {
        return o != null && getClass() == o.getClass();
    }

    /**
     * Returns class name as string.
     */
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
