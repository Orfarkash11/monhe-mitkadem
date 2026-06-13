package ecosystem.entities;

import ecosystem.core.Environment;
import ecosystem.core.Position;
import ecosystem.core.SimulationEngine;
import ecosystem.interfaces.Actable;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 * * Base class for all entities that run as independent threads.
 * Manages biological states (energy, age) and lifecycle synchronization.
 */
public abstract class LivingEntity extends AbstractEntity implements Actable, Runnable {
    private int age = 0;
    private int energy;
    private int maxEnergy;

    protected volatile boolean running = false;
    protected SimulationEngine engine;
    private Thread entityThread;

    public LivingEntity(Position position, char symbol, int energy, int maxEnergy, Environment environment) {
        super(position, symbol);
        this.maxEnergy = (maxEnergy > 0) ? maxEnergy : 100;
        setEnergy(energy);
    }

    public void setEngine(SimulationEngine engine) {
        this.engine = engine;
    }

    public SimulationEngine getEngine() {
        return this.engine;
    }

    public void startThread() {
        if (!running && isAlive()) {
            running = true;
            entityThread = new Thread(this, getClass().getSimpleName() + "-" + System.identityHashCode(this));
            entityThread.start();
        }
    }

    public void stopThread() {
        running = false;
        if (entityThread != null) {
            entityThread.interrupt();
        }
    }

    @Override
    public void run() {
        while (running && isAlive()) {
            try {
                Thread.sleep((long) (Math.random() * 1000) + 500); // Wait 500-1500 ms
                if (engine != null && engine.isRunning()) {
                    act(engine.getEnvironment());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.err.println("Entity error: " + e.getMessage());
            }
        }
    }

    public int getAge() { return this.age; }

    public int getMaxEnergy() { return this.maxEnergy; }

    public int getEnergy() { return this.energy; }

    public boolean setEnergy(int energy) {
        if (energy > maxEnergy) {
            this.energy = maxEnergy;
        } else if (energy < 0) {
            this.energy = 0;
        } else {
            this.energy = energy;
        }
        return true;
    }

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

    public boolean addEnergy(int amount) {
        if (amount > 0) {
            return setEnergy(this.energy + amount);
        }
        return false;
    }

    public boolean reduceEnergy(int amount) {
        if (amount > 0) {
            return setEnergy(this.energy - amount);
        }
        return false;
    }

    protected boolean incrementAge() {
        this.age++;
        return true;
    }

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

    @Override
    public String toString() {
        String posStr = (getPosition() != null) ? getPosition().toString() : "(N/A)";
        return getClass().getSimpleName() + " " + posStr + " energy=" + energy + " alive=" + isAlive();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!super.equals(o)) return false;
        LivingEntity that = (LivingEntity) o;
        return age == that.age && energy == that.energy && maxEnergy == that.maxEnergy;
    }

    @Override
    public Actable getActable() {
        return this;
    }
    public void resumeSimulation() {
        this.running = true;
    }
    public void stopSimulation() {
        this.running = false;
    }
}
