package ecosystem.core;

import ecosystem.entities.AbstractEntity;
import ecosystem.entities.animals.Animal;
import ecosystem.entities.plants.Plant;
import ecosystem.interfaces.Actable;
import java.util.List;

/**
 * Orchestrates the simulation by advancing time in discrete steps (ticks).
 */
public class SimulationEngine {
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
        List<AbstractEntity> snapshot = environment.getEntities();

        // 2. Perform actions for all actable entities
        for (AbstractEntity entity : snapshot) {
            if (entity.isAlive() && entity instanceof Actable) {
                ((Actable) entity).act(environment);
            }
        }

        // 3. Remove dead entities from the environment
        List<AbstractEntity> currentPopulation = environment.getEntities();
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
     * Minimal demo entry point to verify simulation logic.
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        Environment env = new Environment(15, 15);
        SimulationEngine engine = new SimulationEngine(env);

        // Simple demo population setup
        env.addEntity(new ecosystem.entities.resources.Rock(new Position(0, 0)));
        env.addEntity(new ecosystem.entities.resources.Water(new Position(1, 1)));
        
        env.addEntity(new ecosystem.entities.plants.OakTree(new Position(5, 5)));
        env.addEntity(new ecosystem.entities.plants.Flower(new Position(2, 8)));
        
        env.addEntity(new ecosystem.entities.animals.Rabbit(new Position(7, 7), env));
        env.addEntity(new ecosystem.entities.animals.Deer(new Position(3, 3), env));
        env.addEntity(new ecosystem.entities.animals.Lion(new Position(12, 12)));

        System.out.println("Starting Simulation Demo (5 ticks)...");
        for (int i = 0; i < 5; i++) {
            engine.tick();
        }
    }
}
