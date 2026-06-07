package ecosystem.core;

import ecosystem.entities.AbstractEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 * Manages the ecosystem's spatial layout and entity population.
 */
public class Environment {
    private int rows;
    private int cols;
    private List<AbstractEntity> entities;

    public Environment() {
        this(10, 10);
    }

    public Environment(int rows, int cols) {
        this.rows = (rows >= 10) ? rows : 10;
        this.cols = (cols >= 10) ? cols : 10;
        // Using CopyOnWriteArrayList for thread-safe iteration
        this.entities = new CopyOnWriteArrayList<>();
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public boolean isInsideBounds(Position pos) {
        if (pos == null) return false;
        return pos.getRow() >= 0 && pos.getRow() < rows &&
                pos.getCol() >= 0 && pos.getCol() < cols;
    }

    public boolean isInsideMap(Position pos) {
        return isInsideBounds(pos);
    }

    public boolean isPositionFree(Position pos) {
        if (!isInsideBounds(pos)) return false;

        for (AbstractEntity entity : entities) {
            if (entity.isAlive() && pos.equals(entity.getPosition())) {
                return false;
            }
        }
        return true;
    }

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

    public boolean addEntity(AbstractEntity entity) {
        if (entity == null || entity.getPosition() == null) return false;
        if (!isPositionFree(entity.getPosition())) return false;

        return entities.add(entity);
    }

    public boolean removeEntity(AbstractEntity entity) {
        if (entity == null) return false;
        return entities.remove(entity);
    }

    public boolean moveEntity(AbstractEntity entity, Position newPosition) {
        if (entity == null || newPosition == null) return false;
        if (!isPositionFree(newPosition)) return false;
        return entity.setPosition(newPosition);
    }

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

    public List<AbstractEntity> getEntities() {
        return new ArrayList<>(this.entities);
    }

    public List<AbstractEntity> getEntitiesCopy() {
        return getEntities();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Position currentPos = new Position(r, c);
                char symbol = '.';

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Environment that = (Environment) o;
        if (rows != that.rows || cols != that.cols) return false;
        return entities.equals(that.entities);
    }
    public void clear() {
        if (this.entities != null) {
            this.entities.clear();
        }
    }
}
