package ecosystem.behaviors;

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
    /**
     * Constructs a ChaseMovement strategy.
     */
    public ChaseMovement() {}

    @Override
    public boolean move(Animal entity, Environment env) {
        Position current = entity.getPosition();
        if (current == null) return false;

        List<AbstractEntity> nearby = env.getNearbyEntities(current);
        AbstractEntity target = null;
        int minDist = Integer.MAX_VALUE;

        // Find nearest edible target (Carnivores only chase EdibleByCarnivore)
        for (AbstractEntity e : nearby) {
            if (e.isAlive() && e != entity) {
                // Ensure target is Consumable AND EdibleByCarnivore
                // AND not a Plant (EdibleByHerbivore)
                if (e instanceof Consumable && e instanceof EdibleByCarnivore && !(e instanceof EdibleByHerbivore)) {
                    int dist = current.distanceTo(e.getPosition());
                    if (dist < minDist) {
                        minDist = dist;
                        target = e;
                    }
                }
            }
        }

        // If no target is found in sensing range, wander randomly
        if (target == null) {
            return new RandomMovement().move(entity, env);
        }

        Position targetPos = target.getPosition();
        int dr = Integer.compare(targetPos.getRow(), current.getRow());
        int dc = Integer.compare(targetPos.getCol(), current.getCol());

        // Try diagonal or straight move toward target
        int r = current.getRow() + dr;
        int c = current.getCol() + dc;
        if (r >= 0 && c >= 0) {
            Position next = new Position(r, c);
            if (env.isInsideBounds(next) && env.isPositionFree(next)) {
                return env.moveEntity(entity, next);
            }
        }

        // Try straight row move
        r = current.getRow() + dr;
        c = current.getCol();
        if (dr != 0 && r >= 0 && c >= 0) {
            Position next = new Position(r, c);
            if (env.isInsideBounds(next) && env.isPositionFree(next)) {
                return env.moveEntity(entity, next);
            }
        }

        // Try straight col move
        r = current.getRow();
        c = current.getCol() + dc;
        if (dc != 0 && r >= 0 && c >= 0) {
            Position next = new Position(r, c);
            if (env.isInsideBounds(next) && env.isPositionFree(next)) {
                return env.moveEntity(entity, next);
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
