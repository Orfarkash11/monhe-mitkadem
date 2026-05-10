package ecosystem.entities.animals;

import ecosystem.behaviors.EscapeMovement;
import ecosystem.behaviors.HerbivoreBehavior;
import ecosystem.core.Environment;
import ecosystem.core.Position;

/**
 * Large herbivore that escapes from threats.
 */
public class Deer extends Animal {
    /**
     * Constructs a Deer at the given position.
     * @param position initial position.
     * @param environment environment reference (not used in constructor but part of original stub).
     */
    public Deer(Position position, Environment environment) {
        super(position, 'D', 70, 100, new EscapeMovement(), new HerbivoreBehavior());
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
