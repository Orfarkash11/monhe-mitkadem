package ecosystem.core;

public class Position {
    private int row,col;
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }
    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }
    //Distance Function that calculate the distance to other position with Manhattan distance formula
    public int distanceTo(Position other) {
        return Math.abs(row - other.row) + Math.abs(col - other.col);
    }
    public boolean equals(Object o) {}
    public String toString() {}

}
