package ecosystem.entities.plants;

import ecosystem.core.Environment;
import ecosystem.core.Position;
import ecosystem.entities.LivingEntity;
import ecosystem.interfaces.Consumable;
import ecosystem.interfaces.EdibleByHerbivore;
import ecosystem.interfaces.Reproducible;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 * Base class for all plant life in the ecosystem.
 * Plants grow and reproduce without consuming other entities.
 */
public abstract class Plant extends LivingEntity implements Reproducible, EdibleByHerbivore, Consumable {
    private int growthRate;
    private double reproductionChance;

    /**
     * Constructs a Plant.
     * @param position initial position.
     * @param symbol character representation.
     * @param energy initial energy.
     * @param maxEnergy maximum energy.
     * @param growthRate energy gained each tick.
     * @param reproductionChance probability of reproducing each tick.
     */
    public Plant(Position position, char symbol, int energy, int maxEnergy,
          int growthRate, double reproductionChance) {
        super(position, symbol, energy, maxEnergy, null);
        this.growthRate = (growthRate >= 0) ? growthRate : 0;
        this.reproductionChance = (reproductionChance >= 0.0 && reproductionChance <= 1.0) 
                ? reproductionChance : 0.0;
    }

    /**
     * @return current energy level as nutrition value.
     */
    @Override
    public int getNutritionValue() {
        return getEnergy();
    }

    /**
     * Sets the plant to dead when consumed.
     * @return true always.
     */
    @Override
    public boolean onConsumed() {
        return setAlive(false);
    }

    /**
     * Plant specific action: increments age and adds growth energy.
     * Plants do NOT lose 2 energy per tick.
     * @param env the environment.
     * @return true if action performed successfully.
     */
    @Override
    public boolean act(Environment env) {
        if (!isAlive()) {
            return false;
        }
        incrementAge();
        setEnergy(getEnergy() + growthRate);
        reproduce(env);
        return true;
    }

    /**
     * @return the current growth rate.
     */
    public int getGrowthRate() {
        return growthRate;
    }

    /**
     * @return the reproduction probability.
     */
    public double getReproductionChance() {
        return reproductionChance;
    }

    /**
     * Checks equality based on superclass fields and plant-specific properties.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!super.equals(o)) return false;
        Plant plant = (Plant) o;
        return growthRate == plant.growthRate && 
               Double.compare(plant.reproductionChance, reproductionChance) == 0;
    }

    /**
     * Uses superclass toString.
     */
    @Override
    public String toString() {
        return super.toString();
    }
}
