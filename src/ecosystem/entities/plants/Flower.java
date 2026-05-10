package ecosystem.entities.plants;

import ecosystem.interfaces.Reproducible;

import ecosystem.core.Environment;
import ecosystem.core.Position;
import java.util.Random;

/**
 * Represents a fast-growing plant that reproduces in clusters.
 */
public class Flower extends Plant {
    private static final Random random = new Random();

    /**
     * Constructs a Flower at the given position.
     * Initial energy is set to 30 based on reasonable baseline for a fast-growing plant.
     * @param pos the initial position.
     */
    public Flower(Position pos) {
        super(pos, 'F', 30, 70, 5, 0.2);
    }

    /**
     * Attempts to reproduce by placing 1-3 new flowers in nearby free cells.
     * @param env the environment.
     * @return true if at least one flower was successfully added.
     */
    @Override
    public boolean reproduce(Environment env) {
        if (random.nextDouble() > getReproductionChance()) {
            return false;
        }

        Position myPos = getPosition();
        if (myPos == null) return false;

        int row = myPos.getRow();
        int col = myPos.getCol();

        int numToCreate = random.nextInt(3) + 1; // 1 to 3
        int createdCount = 0;

        // Simple scan for available spots within Manhattan distance 2
        for (int dr = -2; dr <= 2 && createdCount < numToCreate; dr++) {
            for (int dc = -2; dc <= 2 && createdCount < numToCreate; dc++) {
                if (dr == 0 && dc == 0) continue;
                
                if (Math.abs(dr) + Math.abs(dc) <= 2) {
                    Position target = new Position(row + dr, col + dc);
                    if (env.isInsideMap(target) && env.isPositionFree(target)) {
                        Flower child = new Flower(target);
                        if (env.addEntity(child)) {
                            createdCount++;
                        }
                    }
                }
            }
        }

        return createdCount > 0;
    }

    /**
     * Checks equality based on superclass fields.
     */
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    /**
     * Uses superclass toString.
     */
    @Override
    public String toString() {
        return super.toString();
    }
}
