package ecosystem.entities;

import ecosystem.core.Position;

public abstract class AbstractEntity {
    private Position pos;
    private char symbol;
    private boolean alive = true;

    public AbstractEntity(Position pos, char symbol) {
        this.pos = pos;
        this.symbol = symbol;
    }

    public Position getPosition() {
        return this.pos;
    }
    //Set position and check if the function is success with the action
    protected boolean setPosition(Position position){
        if (this.pos != null) {
            this.pos = position;
            return true;
        }
        return false;
    }

    public boolean setAlive(boolean alive) {
        if (alive){
            this.alive = alive;
            return true;}
        return false;
    }

    public boolean isAlive() {
        return this.alive;
    }

    protected boolean setSymbol(char symbol) {
        if (this.symbol == symbol){
            return true;
        }
        if (symbol != ' '){
            this.symbol = symbol;
            return true;
        }
        return false;
    }

    public String toString() {
        return "";
    }

    public boolean equals(Object o) {
        return false;
    }
}