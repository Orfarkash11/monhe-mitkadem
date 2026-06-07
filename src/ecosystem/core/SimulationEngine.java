package ecosystem.core;
import ecosystem.gui.observer.SimulationObserver;
import ecosystem.gui.observer.SimulationEvent;
import java.util.ArrayList;
import java.util.Collections;
import ecosystem.entities.AbstractEntity;
import ecosystem.entities.animals.Animal;
import ecosystem.entities.plants.Plant;
import ecosystem.interfaces.Actable;
import java.util.List;
/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 * Orchestrates the simulation by advancing time in discrete steps (ticks).
 */
public class SimulationEngine {
    private final List<SimulationObserver> observers =
            Collections.synchronizedList(new ArrayList<>());

    /** Running count of completed ticks; reset to 0 on reset(). */
    private int tickCount = 0;
    private Environment environment;

    /**
     * Constructs a SimulationEngine with a default 10x10 environment.
     */
    public SimulationEngine() {
        this(new Environment(10, 10));
    }

    /**
     * Constructs a SimulationEngine with the provided environment.
     * @param environment the environment to use.
     */
    public SimulationEngine(Environment environment) {
        this.environment = (environment != null) ? environment : new Environment(10, 10);
    }

    /**
     * Advances the simulation by one tick.
     * Performs actions, removes dead entities, and prints status.
     * This method is void as per project requirements.
     */
    public void tick() {
        // 1. Get a snapshot of entities to avoid concurrent modification
        List<AbstractEntity> snapshot = environment.getEntitiesCopy();

        // 2. Perform actions for all actable entities
        for (AbstractEntity entity : snapshot) {
            Actable actable = entity.getActable();
            if (entity.isAlive() && actable != null) {
                actable.act(environment);
            }
        }

        // 3. Remove dead entities from the environment
        List<AbstractEntity> currentPopulation = environment.getEntitiesCopy();
        for (AbstractEntity entity : currentPopulation) {
            if (!entity.isAlive()) {
                environment.removeEntity(entity);
            }
        }

        // 4. Update and display world map
        System.out.println("\n--- Simulation Tick ---");
        System.out.println(environment.toString());

        // 5. Print summary counts
        printSummary();
        tickCount++;
        notifyObservers();
    }

    /**
     * Private internal printing helper to display population counts.
     * Returns void to keep logic simple and focused on output.
     */
    private void printSummary() {
        List<AbstractEntity> entities = environment.getEntities();
        int aliveCount = 0;
        int animalCount = 0;
        int plantCount = 0;

        for (AbstractEntity entity : entities) {
            if (entity.isAlive()) {
                aliveCount++;
                if (entity instanceof Animal) {
                    animalCount++;
                } else if (entity instanceof Plant) {
                    plantCount++;
                }
            }
        }

        System.out.println("Alive entities: " + aliveCount);
        System.out.println("Animals: " + animalCount);
        System.out.println("Plants: " + plantCount);
    }

    /**
     * @return the environment used by the engine.
     */
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * Checks equality based on the environment.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimulationEngine that = (SimulationEngine) o;
        return environment != null ? environment.equals(that.environment) : that.environment == null;
    }

    /**
     * Returns a string representation of the engine.
     */
    @Override
    public String toString() {
        return "SimulationEngine environment=" + environment;
    }

    /**
     * Minimal demo entry point to verify simulation logic.
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        Environment env = new Environment(15, 15);
        SimulationEngine engine = new SimulationEngine(env);

        // Simple demo population setup (Minimum 3 of each category)
        // Resources: Rock, Water, Rock
        env.addEntity(new ecosystem.entities.resources.Rock(new Position(0, 0)));
        env.addEntity(new ecosystem.entities.resources.Water(new Position(1, 1)));
        env.addEntity(new ecosystem.entities.resources.Rock(new Position(14, 14)));
        
        // Plants: OakTree, Flower, Flower
        env.addEntity(new ecosystem.entities.plants.OakTree(new Position(5, 5)));
        env.addEntity(new ecosystem.entities.plants.Flower(new Position(2, 8)));
        env.addEntity(new ecosystem.entities.plants.Flower(new Position(10, 2)));
        
        // Animals: Rabbit, Deer, Lion
        env.addEntity(new ecosystem.entities.animals.Rabbit(new Position(7, 7), env));
        env.addEntity(new ecosystem.entities.animals.Deer(new Position(3, 3), env));
        env.addEntity(new ecosystem.entities.animals.Lion(new Position(12, 12)));

        System.out.println("Starting Simulation Demo (5 ticks)...");
        for (int i = 0; i < 5; i++) {
            engine.tick();
        }
    }
    /**
     * Registers an observer that will be notified after every state change.
     *
     * @param observer the observer to register; ignored if {@code null}
     */
    public void addObserver(SimulationObserver observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }

    /**
     * Removes a previously registered observer.
     *
     * @param observer the observer to remove
     */
    public void removeObserver(SimulationObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all registered observers with the current simulation state.
     * Called internally at the end of every tick and after a reset.
     */
    private void notifyObservers() {
        SimulationEvent event = new SimulationEvent(tickCount, environment);
        // Iterate over a snapshot to avoid ConcurrentModificationException
        List<SimulationObserver> snapshot;
        synchronized (observers) {
            snapshot = new ArrayList<>(observers);
        }
        for (SimulationObserver obs : snapshot) {
            obs.onSimulationUpdated(event);
        }
    }

    /**
     * Returns the total number of ticks completed since the last reset.
     *
     * @return tick count
     */
    public int getTickCount() {
        return tickCount;
    }

    public void reset() {
        tickCount = 0;
        notifyObservers();
    }

}
