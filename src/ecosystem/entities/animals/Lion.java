package ecosystem.entities.animals;

import ecosystem.behaviors.CarnivoreBehavior;
import ecosystem.behaviors.ChaseMovement;
import ecosystem.core.Position;

/**
 * Top-tier predator that chases prey.
 */
public class Lion extends Animal {
    /**
     * Constructs a Lion at the given position.
     * @param position initial position.
     */
    public Lion(Position position) {
        super(position, 'L', 100, 150, new ChaseMovement(), new CarnivoreBehavior());
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
