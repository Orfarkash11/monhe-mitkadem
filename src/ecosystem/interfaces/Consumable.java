package ecosystem.interfaces;

/**
 * Interface for entities that can be consumed by other entities.
 */
public interface Consumable {
    /**
     * @return the nutrition value of this consumable.
     */
    int getNutritionValue();

    /**
     * Called when the entity is consumed.
     * @return true if consumption was handled successfully.
     */
    boolean onConsumed();
}
