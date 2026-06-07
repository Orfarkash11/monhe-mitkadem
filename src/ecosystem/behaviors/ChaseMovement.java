package ecosystem.behaviors;

import ecosystem.commands.Command;
import ecosystem.commands.MoveCommand;
import ecosystem.core.Environment;
import ecosystem.core.Position;
import ecosystem.entities.AbstractEntity;
import ecosystem.entities.animals.Animal;
import ecosystem.interfaces.Consumable;
import ecosystem.interfaces.EdibleByCarnivore;
import ecosystem.interfaces.EdibleByHerbivore;
import java.util.List;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 * Strategy for moving toward the nearest edible target.
 * Chases nearby edible prey within sensing range.
 * If no prey is nearby, it performs random movement so predators do not stay frozen.
 */
public class ChaseMovement implements MovementStrategy {
    @Override
    public Command getCommand(Animal entity, Environment env) {
        Position current = entity.getPosition();
        if (current == null) return null;

        List<AbstractEntity> nearby = env.getNearbyEntities(current);
        AbstractEntity target = null;
        int minDist = Integer.MAX_VALUE;

        for (AbstractEntity e : nearby) {
            if (e.isAlive() && e != entity) {
                if (e instanceof Consumable && e instanceof EdibleByCarnivore && !(e instanceof EdibleByHerbivore)) {
                    int dist = current.distanceTo(e.getPosition());
                    if (dist < minDist) {
                        minDist = dist;
                        target = e;
                    }
                }
            }
        }

        if (target == null) {
            return new RandomMovement().getCommand(entity, env);
        }

        Position targetPos = target.getPosition();
        int dr = Integer.compare(targetPos.getRow(), current.getRow());
        int dc = Integer.compare(targetPos.getCol(), current.getCol());

        int[][] attempts = {{dr, dc}, {dr, 0}, {0, dc}};
        for (int[] offset : attempts) {
            if (offset[0] == 0 && offset[1] == 0) continue;
            int r = current.getRow() + offset[0];
            int c = current.getCol() + offset[1];
            if (r >= 0 && c >= 0) {
                Position next = new Position(r, c);
                if (env.isInsideBounds(next) && env.isPositionFree(next)) {
                    return new MoveCommand(entity, next);
                }
            }
        }
        return null;
    }
}