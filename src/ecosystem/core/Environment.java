package ecosystem.core;

import ecosystem.entities.AbstractEntity;
import java.util.List;

public class Environment {
    private List<AbstractEntity> entities;
    private AbstractEntity[][] map;

    public boolean IsInsideMap(Position pos) {}
    public boolean isPositionFree(Position pos) {}
    public Position findFreeNearbyPosition(Position origin, int maxDistance){}
    public boolean moveEntity(AbstractEntity entity, Position newPosition) {}
    public List<AbstractEntity> getNearbyEntities(Position pos) {}
    public boolean addEntity(AbstractEntity entity) {}
    public boolean removeEntity(AbstractEntity entity) {}
    public List<AbstractEntity> getEntities() { return this.entities; }
    public String toSting(){}
}
