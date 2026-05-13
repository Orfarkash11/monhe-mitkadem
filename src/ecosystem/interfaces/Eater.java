package ecosystem.interfaces;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
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
