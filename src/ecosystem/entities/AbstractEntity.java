package ecosystem.entities;

import ecosystem.core.Position;

public class AbstractEntity {
    private Position pos;
    private String symbol;
    private boolean alive=true;
    protected boolean setSymbol(char symbol) {
        this.symbol=String.valueOf(symbol);
        return true;
    }

    public String toString(){
    }
    public boolean equals(Object o){
    }
}

