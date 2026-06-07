package ecosystem.entities.plants;

import ecosystem.commands.ReproduceCommand;
import ecosystem.core.Environment;
import ecosystem.core.Position;
import java.util.Random;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 * Represents a slow-growing, long-lived tree.
 */
public class OakTree extends Plant {
    private static final Random random = new Random();

    public OakTree(Position pos) {
        super(pos, 'T', 80, 120, 2, 0.05);
    }

    @Override
    public boolean reproduce(Environment env) {
        if (random.nextDouble() > getReproductionChance()) return false;

        Position myPos = getPosition();
        if (myPos == null) return false;

        int[][] offsets = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] offset : offsets) {
            int r = myPos.getRow() + offset[0];
            int c = myPos.getCol() + offset[1];
            if (r >= 0 && c >= 0) {
                Position target = new Position(r, c);
                if (env.isInsideMap(target) && env.isPositionFree(target)) {
                    OakTree child = new OakTree(target);
                    child.setEngine(getEngine());
                    if (getEngine() != null) {
                        getEngine().submitCommand(new ReproduceCommand(child));
                        return true;
                    }
                }
            }
        }
        return false;
    }
}