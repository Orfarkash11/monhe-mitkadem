package ecosystem.entities;

import ecosystem.core.Position;

public class AbstractEntity {
    private Position pos;
    private String symbol;
    private boolean alive = true;

    public Position getPosition() {
        return this.pos;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isAlive() {
        return this.alive;
    }

    protected boolean setSymbol(char symbol) {
        this.symbol = String.valueOf(symbol);
        return true;
    }

    public String toString() {
        return "";
    }

    public boolean equals(Object o) {
        return false;
    }
}