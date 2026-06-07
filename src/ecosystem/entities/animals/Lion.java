package ecosystem.entities.animals;

import ecosystem.behaviors.CarnivoreBehavior;
import ecosystem.behaviors.ChaseMovement;
import ecosystem.commands.ReproduceCommand;
import ecosystem.core.Environment;
import ecosystem.core.Position;
import ecosystem.interfaces.Reproducible;
import java.util.Random;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 * Top-tier predator that chases prey.
 */
public class Lion extends Animal implements Reproducible {
    private static final Random random = new Random();

    public Lion(Position position) {
        this(position, null);
    }

    public Lion(Position position, Environment environment) {
        super(position, 'L', 100, 150, new ChaseMovement(), new CarnivoreBehavior());
    }

    @Override
    public boolean act(Environment env) {
        if (!super.act(env)) return false;
        if (isAlive()) reproduce(env);
        return true;
    }

    @Override
    public boolean reproduce(Environment env) {
        if (getEnergy() <= 80 || random.nextDouble() > 0.1) return false;

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
                    Lion child = new Lion(target, env);
                    child.setEngine(getEngine());
                    if (getEngine() != null) getEngine().submitCommand(new ReproduceCommand(child));
                    return true;
                }
            }
        }
        return false;
    }
}