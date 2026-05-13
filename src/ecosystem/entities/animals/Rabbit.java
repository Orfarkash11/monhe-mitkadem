package ecosystem.entities.animals;

import ecosystem.behaviors.HerbivoreBehavior;
import ecosystem.behaviors.RandomMovement;
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

    /**
     * Convenience constructor for automatic testing.
     * @param position initial position.
     */
    public Rabbit(Position position) {
        this(position, null);
    }

    /**
     * Constructs a Rabbit at the given position.
     * @param position initial position.
     * @param environment environment reference (not used in constructor).
     */
    public Rabbit(Position position, Environment environment) {
        super(position, 'R', 50, 80, new RandomMovement(), new HerbivoreBehavior());
    }

    /**
     * Rabbit specific action: standard animal behavior + reproduction.
     * @param env the environment.
     * @return true if cycle completed.
     */
    @Override
    public boolean act(Environment env) {
        if (!super.act(env)) {
            return false;
        }
        
        // Reproduction attempt after eating
        if (isAlive()) {
            reproduce(env);
        }
        
        return true;
    }

    /**
     * Attempts to reproduce in a free adjacent cell if energy > 30.
     * @param env the environment.
     * @return true if a new rabbit was added.
     */
    @Override
    public boolean reproduce(Environment env) {
        if (getEnergy() <= 30 || random.nextDouble() > 0.3) {
            return false;
        }

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