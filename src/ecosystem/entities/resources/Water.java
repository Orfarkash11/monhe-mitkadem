package ecosystem.entities.resources;

import ecosystem.interfaces.Consumable;

import ecosystem.core.Position;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 * Represents a water source that provides nutrition without being consumed.
 */
public class Water extends Resource implements Consumable {
    /**
     * Constructs a Water source.
     * @param pos initial position.
     */
    public Water(Position pos) {
        super(pos, 'W');
    }

    /**
     * @return 100 as the nutrition value.
     */
    @Override
    public int getNutritionValue() {
        return 100;
    }

    /**
     * Water does not disappear when consumed.
     * @return true always.
     */
    @Override
    public boolean onConsumed() {
        return true;
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
