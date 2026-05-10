package ecosystem.interfaces;

/**
 * Interface for entities that can eat consumables.
 */
public interface Eater {
    /**
     * Attempts to eat a consumable target.
     * @param target the consumable to eat.
     * @return true if eating was successful, false otherwise.
     */
    boolean eat(Consumable target);
}
