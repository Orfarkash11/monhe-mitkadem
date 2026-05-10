package ecosystem.entities.resources;

import ecosystem.core.Position;
import ecosystem.entities.StaticEntity;

/**
 * Abstract base class for all environmental resources.
 */
public abstract class Resource extends StaticEntity {
    /**
     * Constructs a Resource.
     * @param position initial position.
     * @param symbol character representation.
     */
    public Resource(Position position, char symbol) {
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
     * Uses superclass toString.
     */
    @Override
    public String toString() {
        return super.toString();
    }
}
