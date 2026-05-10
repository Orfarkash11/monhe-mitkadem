package ecosystem.behaviors;

import ecosystem.core.Environment;
import ecosystem.core.Position;
import ecosystem.entities.AbstractEntity;
import ecosystem.entities.animals.Animal;
import java.util.List;

/**
 * Strategy for moving away from the nearest threat.
 */
public class EscapeMovement implements MovementStrategy {
    @Override
    public boolean move(Animal entity, Environment env) {
        Position current = entity.getPosition();
        if (current == null) return false;

        List<AbstractEntity> nearby = env.getNearbyEntities(current);
        AbstractEntity threat = null;
        int minDist = Integer.MAX_VALUE;

        // Find nearest threat (any other animal)
        for (AbstractEntity e : nearby) {
            if (e.isAlive() && e instanceof Animal && e != entity) {
                int dist = current.distanceTo(e.getPosition());
                if (dist < minDist) {
                    minDist = dist;
                    threat = e;
                }
            }
        }

        if (threat == null) return false;

        Position threatPos = threat.getPosition();
        Position bestPos = null;
        int maxDist = minDist;

        // Check 8 adjacent spots for the one that maximizes distance from threat
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                
                int r = current.getRow() + dr;
                int c = current.getCol() + dc;
                if (r >= 0 && c >= 0) {
                    Position candidate = new Position(r, c);
                    if (env.isInsideBounds(candidate) && env.isPositionFree(candidate)) {
                        int dist = candidate.distanceTo(threatPos);
                        if (dist > maxDist) {
                            maxDist = dist;
                            bestPos = candidate;
                        }
                    }
                }
            }
        }

        if (bestPos != null) {
            return env.moveEntity(entity, bestPos);
        }

        return false;
    }
}
