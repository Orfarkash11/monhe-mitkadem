package ecosystem.behaviors;

import ecosystem.core.Environment;
import ecosystem.core.Position;
import ecosystem.entities.animals.Animal;
import java.util.Random;

/**
 * Strategy for moving to a random adjacent free cell.
 */
public class RandomMovement implements MovementStrategy {
    private static final Random random = new Random();

    /**
     * Moves the animal to a random adjacent free position.
     * @param entity the animal to move.
     * @param env the environment.
     * @return true if moved successfully.
     */
    @Override
    public boolean move(Animal entity, Environment env) {
        Position current = entity.getPosition();
        if (current == null) return false;

        int row = current.getRow();
        int col = current.getCol();

        // 8 possible adjacent directions
        int[][] directions = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},           {0, 1},
            {1, -1},  {1, 0},  {1, 1}
        };
        
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
}
