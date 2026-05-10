package ecosystem.entities;

import ecosystem.core.Position;

/**
 * Base class for all entities in the ecosystem.
 */
public abstract class AbstractEntity {
    private Position position;
    private char symbol;
    private boolean alive = true;

    /**
     * Constructs an AbstractEntity with position and symbol.
     * @param position the initial position.
     * @param symbol the character representation.
     */
    protected AbstractEntity(Position position, char symbol) {
        if (position != null) {
            this.position = new Position(position.getRow(), position.getCol());
        }
        this.symbol = symbol;
    }

    /**
     * @return a defensive copy of the entity's position.
     */
    public Position getPosition() {
        if (this.position == null) return null;
        return new Position(this.position.getRow(), this.position.getCol());
    }

    /**
     * Updates the entity's position.
     * @param position the new position.
     * @return true if set successfully, false if position is null.
     */
    public boolean setPosition(Position position) {
        if (position != null) {
            this.position = new Position(position.getRow(), position.getCol());
            return true;
        }
        return false;
    }

    /**
     * @return the character symbol of the entity.
     */
    public char getSymbol() {
        return this.symbol;
    }

    /**
     * Sets the character symbol of the entity.
     * @param symbol the new symbol.
     * @return true if set successfully.
     */
    protected boolean setSymbol(char symbol) {
        this.symbol = symbol;
        return true;
    }

    /**
     * @return true if the entity is alive.
     */
    public boolean isAlive() {
        return this.alive;
    }

    /**
     * Updates the alive status.
     * @param alive the new status.
     * @return true if status was changed.
     */
    public boolean setAlive(boolean alive) {
        this.alive = alive;
        return true;
    }

    /**
     * Returns a string representation: EntityType (row,col) energy=N/A alive=true/false
     */
    @Override
    public String toString() {
        String posStr = (position != null) ? position.toString() : "(N/A)";
        return getClass().getSimpleName() + " " + posStr + " energy=N/A alive=" + alive;
    }

    /**
     * Checks equality based on class, position, symbol and alive status.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEntity that = (AbstractEntity) o;
        if (symbol != that.symbol) return false;
        if (alive != that.alive) return false;
        return position != null ? position.equals(that.position) : that.position == null;
    }
}