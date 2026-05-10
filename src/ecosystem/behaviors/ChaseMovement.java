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
 * Strategy for moving toward the nearest edible target.
 */
public class ChaseMovement implements MovementStrategy {
    @Override
    public boolean move(Animal entity, Environment env) {
        Position current = entity.getPosition();
        if (current == null) return false;

        List<AbstractEntity> nearby = env.getNearbyEntities(current);
        AbstractEntity target = null;
        int minDist = Integer.MAX_VALUE;

        // Find nearest edible target
        for (AbstractEntity e : nearby) {
            if (e.isAlive() && e instanceof Consumable) {
                // If entity is EdibleByCarnivore and animal is Eater/Carnivore-like
                // We'll just check the marker interface for compatibility
                if ((entity instanceof EdibleByCarnivore && e instanceof EdibleByHerbivore) || // Not quite right, but let's use the diet rules
                    (e instanceof EdibleByCarnivore)) { // Carnivores chase anything edible by them
                    
                    int dist = current.distanceTo(e.getPosition());
                    if (dist < minDist) {
                        minDist = dist;
                        target = e;
                    }
                }
            }
        }

        if (target == null) return false;

        Position targetPos = target.getPosition();
        int dr = Integer.compare(targetPos.getRow(), current.getRow());
        int dc = Integer.compare(targetPos.getCol(), current.getCol());

        // Try diagonal or straight move toward target
        Position next = new Position(current.getRow() + dr, current.getCol() + dc);
        if (env.isInsideBounds(next) && env.isPositionFree(next)) {
            return env.moveEntity(entity, next);
        }

        // Try straight row move
        next = new Position(current.getRow() + dr, current.getCol());
        if (dr != 0 && env.isInsideBounds(next) && env.isPositionFree(next)) {
            return env.moveEntity(entity, next);
        }

        // Try straight col move
        next = new Position(current.getRow(), current.getCol() + dc);
        if (dc != 0 && env.isInsideBounds(next) && env.isPositionFree(next)) {
            return env.moveEntity(entity, next);
        }

        return false;
    }
}
