package ecosystem.entities.plants;

import ecosystem.core.Environment;
import ecosystem.core.Position;
import java.util.Random;

/**
 * Represents a slow-growing, long-lived tree.
 */
public class OakTree extends Plant {
    private static final Random random = new Random();

    /**
     * Constructs an OakTree at the given position.
     * @param pos the initial position.
     */
    public OakTree(Position pos) {
        super(pos, 'T', 80, 120, 2, 0.05);
    }

    /**
     * Attempts to reproduce by placing one seed in an adjacent free cell.
     * @param env the environment.
     * @return true if a new tree was successfully added.
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

        // Check Manhattan distance 1 positions
        int[][] offsets = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] offset : offsets) {
            int r = row + offset[0];
            int c = col + offset[1];
            if (r >= 0 && c >= 0) {
                Position target = new Position(r, c);
                if (env.isInsideMap(target) && env.isPositionFree(target)) {
                    OakTree child = new OakTree(target);
                    return env.addEntity(child);
                }
            }
        }

        return false;
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
