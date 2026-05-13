package ecosystem.entities.resources;

import ecosystem.core.Position;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 * Represents a solid obstacle in the environment.
 */
public class Rock extends Resource {
    private boolean blocksMovement = true;

    /**
     * Constructs a Rock at the given position.
     * @param pos initial position.
     */
    public Rock(Position pos) {
        super(pos, 'X');
    }

    /**
     * @return true if this rock blocks movement.
     */
    public boolean blocksMovement() {
        return blocksMovement;
    }

    /**
     * Checks equality based on superclass fields and blocksMovement.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!super.equals(o)) return false;
        Rock rock = (Rock) o;
        return blocksMovement == rock.blocksMovement;
    }

    /**
     * Uses superclass toString.
     */
    @Override
    public String toString() {
        return super.toString();
    }
}
