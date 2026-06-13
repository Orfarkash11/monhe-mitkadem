package ecosystem.decorators;

import ecosystem.core.Environment;
import ecosystem.core.Position;
import ecosystem.entities.LivingEntity;
import ecosystem.interfaces.Consumable;
import ecosystem.interfaces.EdibleByCarnivore;
import ecosystem.interfaces.EdibleByHerbivore;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 * Pattern: Decorator
 * * Abstract base class for all entity decorators, delegating properties to the wrapped entity.
 */
public abstract class EntityDecorator extends LivingEntity
        implements Consumable, EdibleByCarnivore, EdibleByHerbivore {

    protected LivingEntity decoratedEntity;
    protected int duration = 10;

    /**
     * Constructs a new decorator wrapping the specified living entity.
     *
     * @param decoratedEntity the underlying entity to be decorated
     */
    public EntityDecorator(LivingEntity decoratedEntity) {
        super(
                decoratedEntity.getPosition(),
                decoratedEntity.getSymbol(),
                decoratedEntity.getEnergy(),
                decoratedEntity.getMaxEnergy(),
                decoratedEntity.getEngine() != null ? decoratedEntity.getEngine().getEnvironment() : null
        );
        this.decoratedEntity = decoratedEntity;
    }

    /**
     * Executes the decorator's action, reducing its active duration.
     *
     * @param env the current simulation environment
     * @return true if the decorator is still active, false otherwise
     */
    @Override
    public boolean act(Environment env) {
        duration--;
        if (duration <= 0) {
            removeDecorator();
            return false;
        }
        return true;
    }

    /**
     * Removes the decorator from the entity and restores the original entity in the environment.
     */
    protected void removeDecorator() {
        if (getEngine() != null) {
            getEngine().replaceEntity(this, decoratedEntity);
        }
    }

    /**
     * Retrieves the original entity that is wrapped by this decorator.
     *
     * @return the original living entity
     */
    public LivingEntity getDecoratedEntity() {
        return decoratedEntity;
    }

    // --- Delegated Methods ---

    @Override
    public Position getPosition() {
        return decoratedEntity != null ? decoratedEntity.getPosition() : super.getPosition();
    }

    @Override
    public boolean setPosition(Position p) {
        if (decoratedEntity != null) return decoratedEntity.setPosition(p);
        return super.setPosition(p);
    }

    @Override
    public int getEnergy() {
        return decoratedEntity != null ? decoratedEntity.getEnergy() : super.getEnergy();
    }

    @Override
    public boolean setEnergy(int e) {
        if (decoratedEntity != null) {
            int diff = e - decoratedEntity.getEnergy();
            if (diff > 0) return decoratedEntity.addEnergy(diff);
            if (diff < 0) return decoratedEntity.reduceEnergy(-diff);
            return true;
        }
        return super.setEnergy(e);
    }

    @Override
    public boolean isAlive() {
        return decoratedEntity != null ? decoratedEntity.isAlive() : super.isAlive();
    }

    @Override
    public boolean setAlive(boolean alive) {
        if (decoratedEntity != null) return decoratedEntity.setAlive(alive);
        return super.setAlive(alive);
    }

    @Override
    public int getMaxEnergy() {
        return decoratedEntity != null ? decoratedEntity.getMaxEnergy() : super.getMaxEnergy();
    }

    @Override
    public char getSymbol() {
        return decoratedEntity != null ? decoratedEntity.getSymbol() : super.getSymbol();
    }

    @Override
    public int getNutritionValue() {
        if (decoratedEntity instanceof Consumable) {
            return ((Consumable) decoratedEntity).getNutritionValue();
        }
        return 0;
    }

    @Override
    public boolean onConsumed() {
        if (decoratedEntity instanceof Consumable) {
            return ((Consumable) decoratedEntity).onConsumed();
        }
        return setAlive(false);
    }

    @Override
    public String toString() {
        if (decoratedEntity != null) {
            return decoratedEntity.toString() + " [" + getDecoratorName() + " " + duration + "T]";
        }
        return super.toString();
    }

    /**
     * Returns the name of the specific decorator.
     *
     * @return the name of the decorator
     */
    protected abstract String getDecoratorName();
}