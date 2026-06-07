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

    public Plant(Position position, char symbol, int energy, int maxEnergy, int growthRate, double reproductionChance) {
        super(position, symbol, energy, maxEnergy, null);
        this.growthRate = (growthRate >= 0) ? growthRate : 0;
        this.reproductionChance = (reproductionChance >= 0.0 && reproductionChance <= 1.0) ? reproductionChance : 0.0;
    }

    public double getReproductionChance() {
        return reproductionChance;
    }

    public int getGrowthRate() {
        return growthRate;
    }

    @Override
    public int getNutritionValue() {
        return getEnergy();
    }

    @Override
    public boolean onConsumed() {
        return setAlive(false);
    }

    @Override
    public boolean act(Environment env) {
        if (!super.act(env)) return false;

        // Compensate for the energy lost in LivingEntity.act() and add growthRate
        addEnergy(2 + growthRate);

        if (isAlive()) {
            reproduce(env);
        }
        return true;
    }
}
