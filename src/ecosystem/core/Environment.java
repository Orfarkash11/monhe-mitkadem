package ecosystem.core;

import ecosystem.entities.AbstractEntity;

public class Environment {
    private List<AbstractEntity> entities;
    private AbstractEntity[][] map;
    public boolean isPositionFree(Position pos) {}
    public List<AbstractEntity> getNearbyEntities(Position pos) {}
    public boolean addEntity(AbstractEntity entity) {}
    public boolean removeEntity(AbstractEntity entity) {}
}
