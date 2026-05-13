package ecosystem.entities;

import ecosystem.core.Position;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 * Base class for entities that do not act, move, or age.
 */
public abstract class StaticEntity extends AbstractEntity {
    /**
     * Constructs a StaticEntity.
     * @param position initial position.
     * @param symbol character representation.
     */
    public StaticEntity(Position position, char symbol) {
        super(position, symbol);
    }

    /**
     * Checks equality based on superclass fields.
     */
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    /**
     * Uses superclass toString (energy=N/A).
     */
    @Override
    public String toString() {
        return super.toString();
    }
}
