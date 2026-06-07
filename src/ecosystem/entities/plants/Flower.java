package ecosystem.entities.plants;

import ecosystem.commands.ReproduceCommand;
import ecosystem.core.Environment;
import ecosystem.core.Position;
import java.util.Random;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 * Represents a fast-growing plant that reproduces in clusters.
 */
public class Flower extends Plant {
    private static final Random random = new Random();

    public Flower(Position pos) {
        super(pos, 'F', 30, 70, 5, 0.2);
    }

    @Override
    public boolean reproduce(Environment env) {
        if (random.nextDouble() > getReproductionChance()) return false;

        Position myPos = getPosition();
        if (myPos == null) return false;

        int numToCreate = random.nextInt(3) + 1;
        int createdCount = 0;

        for (int dr = -2; dr <= 2 && createdCount < numToCreate; dr++) {
            for (int dc = -2; dc <= 2 && createdCount < numToCreate; dc++) {
                if (dr == 0 && dc == 0) continue;
                if (Math.abs(dr) + Math.abs(dc) <= 2) {
                    int r = myPos.getRow() + dr;
                    int c = myPos.getCol() + dc;
                    if (r >= 0 && c >= 0) {
                        Position target = new Position(r, c);
                        if (env.isInsideMap(target) && env.isPositionFree(target)) {
                            Flower child = new Flower(target);
                            child.setEngine(getEngine());
                            if (getEngine() != null) {
                                getEngine().submitCommand(new ReproduceCommand(child));
                                createdCount++;
                            }
                        }
                    }
                }
            }
        }
        return createdCount > 0;
    }
}