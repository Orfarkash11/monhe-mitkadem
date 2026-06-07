package ecosystem.entities.animals;

import ecosystem.behaviors.HerbivoreBehavior;
import ecosystem.behaviors.RandomMovement;
import ecosystem.commands.ReproduceCommand;
import ecosystem.core.Environment;
import ecosystem.core.Position;
import ecosystem.interfaces.Reproducible;
import java.util.Random;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 * Small herbivore that reproduces quickly.
 */
public class Rabbit extends Animal implements Reproducible {
    private static final Random random = new Random();

    public Rabbit(Position position) { this(position, null); }

    public Rabbit(Position position, Environment environment) {
        super(position, 'R', 50, 80, new RandomMovement(), new HerbivoreBehavior());
    }

    @Override
    public boolean act(Environment env) {
        if (!super.act(env)) return false;
        if (isAlive()) reproduce(env);
        return true;
    }

    @Override
    public boolean reproduce(Environment env) {
        if (getEnergy() <= 30 || random.nextDouble() > 0.3) return false;

        Position current = getPosition();
        if (current == null) return false;

        int[][] directions = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},           {0, 1},
                {1, -1},  {1, 0},  {1, 1}
        };

        for (int[] dir : directions) {
            int r = current.getRow() + dir[0];
            int c = current.getCol() + dir[1];
            if (r >= 0 && c >= 0) {
                Position target = new Position(r, c);
                if (env.isInsideBounds(target) && env.isPositionFree(target)) {
                    Rabbit child = new Rabbit(target, env);
                    child.setEngine(getEngine());
                    if (getEngine() != null) getEngine().submitCommand(new ReproduceCommand(child));
                    return true;
                }
            }
        }
        return false;
    }
}