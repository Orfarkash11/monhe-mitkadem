package ecosystem.core;

import ecosystem.entities.AbstractEntity;
import ecosystem.interfaces.Actable;
import java.util.ArrayList;
import java.util.List;

public class SimulationEngine {
    private Environment environment;
    public SimulationEngine(Environment environment) {
        this.environment = environment;
    }
    public void tick() {
        List<AbstractEntity> currentEntities = new ArrayList<>(this.environment.getEntities());

        for (AbstractEntity entity : currentEntities) {
            if (entity != null && entity.isAlive() && entity instanceof Actable) {
                ((Actable) entity).act(this.environment);
            }
        }

        List<AbstractEntity> deadEntities = new ArrayList<>();
        for (AbstractEntity entity : this.environment.getEntities()) {
            if (entity != null && !entity.isAlive()) {
                deadEntities.add(entity);
            }
        }

        for (AbstractEntity deadEntity : deadEntities) {
            this.environment.removeEntity(deadEntity);
        }
    }

    @Override
    public String toString() {
        return this.environment.toString();
    }
}
