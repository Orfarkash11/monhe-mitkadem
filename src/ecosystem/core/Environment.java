package ecosystem.core;

import ecosystem.entities.AbstractEntity;
import java.util.ArrayList;
import java.util.List;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 * Manages the ecosystem's spatial layout and entity population.
 */
public class Environment {
    private int rows;
    private int cols;
    private List<AbstractEntity> entities;

    /**
     * Or Farkash 314920984
     * Oleg Magit 312544752
     */
    public Environment() {
        this(10, 10);
    }

    /**
     * Constructs an environment with specified dimensions.
     * Dimensions are normalized to at least 10x10.
     * @param rows number of rows.
     * @param cols number of columns.
     */
    public Environment(int rows, int cols) {
        this.rows = (rows >= 10) ? rows : 10;
        this.cols = (cols >= 10) ? cols : 10;
        this.entities = new ArrayList<>();
    }

    /**
     * @return the number of rows in the environment.
     */
    public int getRows() {
        return rows;
    }

    /**
     * @return the number of columns in the environment.
     */
    public int getCols() {
        return cols;
    }

    /**
     * Checks if a position is within the map boundaries.
     * @param pos the position to check.
     * @return true if inside bounds.
     */
    public boolean isInsideBounds(Position pos) {
        if (pos == null) return false;
        return pos.getRow() >= 0 && pos.getRow() < rows &&
               pos.getCol() >= 0 && pos.getCol() < cols;
    }

    /**
     * Alias for isInsideBounds to support existing code.
     * @param pos the position to check.
     * @return true if inside map.
     */
    public boolean isInsideMap(Position pos) {
        return isInsideBounds(pos);
    }

    /**
     * Checks if a position is free for occupation.
     * A cell is free if it is inside bounds and contains no alive entities.
     * @param pos the position to check.
     * @return true if free.
     */
    public boolean isPositionFree(Position pos) {
        if (!isInsideBounds(pos)) return false;
        
        for (AbstractEntity entity : entities) {
            if (entity.isAlive() && pos.equals(entity.getPosition())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Finds alive entities within a Manhattan distance of 2, excluding the origin.
     * @param pos the center position.
     * @return a new list of nearby entities.
     */
    public List<AbstractEntity> getNearbyEntities(Position pos) {
        List<AbstractEntity> nearby = new ArrayList<>();
        if (pos == null) return nearby;

        for (AbstractEntity entity : entities) {
            if (entity.isAlive() && entity.getPosition() != null) {
                int dist = pos.distanceTo(entity.getPosition());
                if (dist > 0 && dist <= 2) {
                    nearby.add(entity);
                }
            }
        }
        return nearby;
    }

    /**
     * Adds an entity to the environment if the position is free.
     * @param entity the entity to add.
     * @return true if added successfully.
     */
    public boolean addEntity(AbstractEntity entity) {
        if (entity == null || entity.getPosition() == null) return false;
        if (!isPositionFree(entity.getPosition())) return false;
        
        return entities.add(entity);
    }

    /**
     * Removes an entity from the environment.
     * @param entity the entity to remove.
     * @return true if found and removed.
     */
    public boolean removeEntity(AbstractEntity entity) {
        if (entity == null) return false;
        return entities.remove(entity);
    }

    /**
     * Moves an entity to a new position if it is free.
     * @param entity the entity to move.
     * @param newPosition the destination position.
     * @return true if moved successfully.
     */
    public boolean moveEntity(AbstractEntity entity, Position newPosition) {
        if (entity == null || newPosition == null) return false;
        if (!isPositionFree(newPosition)) return false;
        return entity.setPosition(newPosition);
    }

    /**
     * Finds a free position near the origin within maxDistance.
     * @param origin the starting position.
     * @param maxDistance the maximum Manhattan distance.
     * @return a free position, or null if none found.
     */
    public Position findFreeNearbyPosition(Position origin, int maxDistance) {
        if (origin == null) return null;
        for (int dr = -maxDistance; dr <= maxDistance; dr++) {
            for (int dc = -maxDistance; dc <= maxDistance; dc++) {
                if (Math.abs(dr) + Math.abs(dc) <= maxDistance) {
                    int r = origin.getRow() + dr;
                    int c = origin.getCol() + dc;
                    if (r >= 0 && c >= 0) {
                        Position candidate = new Position(r, c);
                        if (isPositionFree(candidate)) {
                            return candidate;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * @return a shallow copy of the entity list.
     */
    public List<AbstractEntity> getEntities() {
        return new ArrayList<>(this.entities);
    }

    /**
     * Alias for getEntities to support automatic testing.
     * @return a shallow copy of the entity list.
     */
    public List<AbstractEntity> getEntitiesCopy() {
        return getEntities();
    }

    /**
     * Renders the environment as a grid of characters.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Position currentPos = new Position(r, c);
                char symbol = '.';
                
                // Find first alive entity at this spot
                for (AbstractEntity entity : entities) {
                    if (entity.isAlive() && currentPos.equals(entity.getPosition())) {
                        symbol = entity.getSymbol();
                        break;
                    }
                }
                
                sb.append(symbol);
                if (c < cols - 1) sb.append(' ');
            }
            if (r < rows - 1) sb.append('\n');
        }
        return sb.toString();
    }

    /**
     * Checks equality based on dimensions and entity population.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Environment that = (Environment) o;
        if (rows != that.rows || cols != that.cols) return false;
        return entities.equals(that.entities);
    }
}
