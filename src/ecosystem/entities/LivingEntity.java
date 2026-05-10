package ecosystem.entities;

import ecosystem.core.Environment;
import ecosystem.core.Position;
import ecosystem.interfaces.Actable;

/**
 * Base class for entities that have biological needs and age over time.
 */
public abstract class LivingEntity extends AbstractEntity implements Actable {
    private int age = 0;
    private int energy;
    private int maxEnergy;

    /**
     * Constructs a LivingEntity.
     * @param position initial position.
     * @param symbol character representation.
     * @param energy initial energy level.
     * @param maxEnergy maximum allowed energy.
     * @param environment reference to the environment.
     */
    public LivingEntity(Position position, char symbol, int energy, int maxEnergy, Environment environment) {
        super(position, symbol);
        this.maxEnergy = (maxEnergy > 0) ? maxEnergy : 100;
        setEnergy(energy);
    }

    /**
     * @return current age in ticks.
     */
    public int getAge() { return this.age; }

    /**
     * @return current maximum energy.
     */
    public int getMaxEnergy() { return this.maxEnergy; }

    /**
     * @return current energy level.
     */
    public int getEnergy() {
        return this.energy;
    }

    /**
     * Updates energy level, capped at maxEnergy and floor at 0.
     * @param energy the new energy level.
     * @return true if set successfully.
     */
    protected boolean setEnergy(int energy) {
        if (energy > maxEnergy) {
            this.energy = maxEnergy;
        } else if (energy < 0) {
            this.energy = 0;
        } else {
            this.energy = energy;
        }
        return true;
    }

    /**
     * Updates maximum energy level.
     * @param maxEnergy the new max energy (must be positive).
     * @return true if updated, false if invalid value provided.
     */
    protected boolean setMaxEnergy(int maxEnergy) {
        if (maxEnergy > 0) {
            this.maxEnergy = maxEnergy;
            if (this.energy > this.maxEnergy) {
                this.energy = this.maxEnergy;
            }
            return true;
        }
        return false;
    }

    /**
     * Increases the entity's energy by the given amount.
     * @param amount the energy to add.
     * @return true if successful.
     */
    public boolean addEnergy(int amount) {
        if (amount > 0) {
            return setEnergy(this.energy + amount);
        }
        return false;
    }

    /**
     * Decreases the entity's energy by the given amount.
     * @param amount the energy to subtract.
     * @return true if successful.
     */
    public boolean reduceEnergy(int amount) {
        if (amount > 0) {
            return setEnergy(this.energy - amount);
        }
        return false;
    }

    /**
     * Increments the age of the entity by 1.
     * @return true always.
     */
    protected boolean incrementAge() {
        this.age++;
        return true;
    }

    /**
     * Performs basic biological updates: aging and energy consumption.
     * @param env the environment.
     * @return true if the entity is still alive after acting.
     */
    @Override
    public boolean act(Environment env) {
        this.age++;
        this.energy -= 2;
        if (this.energy <= 0) {
            this.energy = 0;
            setAlive(false);
        }
        return isAlive();
    }

    /**
     * Returns a string representation: EntityType (row,col) energy=80 alive=true/false
     */
    @Override
    public String toString() {
        String posStr = (getPosition() != null) ? getPosition().toString() : "(N/A)";
        return getClass().getSimpleName() + " " + posStr + " energy=" + energy + " alive=" + isAlive();
    }

    /**
     * Checks equality based on superclass fields and biological stats.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!super.equals(o)) return false;
        LivingEntity that = (LivingEntity) o;
        return age == that.age && energy == that.energy && maxEnergy == that.maxEnergy;
    }
}
