package ecosystem.behaviors;

import ecosystem.commands.Command;
import ecosystem.commands.MoveCommand;
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

    @Override
    public Command getCommand(Animal entity, Environment env) {
        Position current = entity.getPosition();
        if (current == null) return null;

        int[][] directions = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},           {0, 1},
                {1, -1},  {1, 0},  {1, 1}
        };

        int startIdx = random.nextInt(directions.length);
        for (int i = 0; i < directions.length; i++) {
            int[] dir = directions[(startIdx + i) % directions.length];
            int r = current.getRow() + dir[0];
            int c = current.getCol() + dir[1];

            if (r >= 0 && c >= 0) {
                Position target = new Position(r, c);
                if (env.isInsideBounds(target) && env.isPositionFree(target)) {
                    return new MoveCommand(entity, target);
                }
            }
        }
        return null;
    }
}
