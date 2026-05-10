package ecosystem.core;

/**
 * Represents a 2D coordinate (row, col) in the ecosystem grid.
 * This is a value object used to track entity locations.
 */
public class Position {
    private int row;
    private int col;

    /**
     * Constructs a Position with specified row and column.
     * Coordinates must be non-negative.
     * @param row the grid row.
     * @param col the grid column.
     */
    public Position(int row, int col) {
        if (row < 0) row = 0;
        if (col < 0) col = 0;
        this.row = row;
        this.col = col;
    }

    /**
     * @return the current row.
     */
    public int getRow() {
        return row;
    }

    /**
     * @return the current column.
     */
    public int getCol() {
        return col;
    }

    /**
     * Sets a new row if non-negative.
     * @param row the new row.
     * @return true if set successfully, false if value was negative.
     */
    public boolean setRow(int row) {
        if (row >= 0) {
            this.row = row;
            return true;
        }
        return false;
    }

    /**
     * Sets a new column if non-negative.
     * @param col the new column.
     * @return true if set successfully, false if value was negative.
     */
    public boolean setCol(int col) {
        if (col >= 0) {
            this.col = col;
            return true;
        }
        return false;
    }

    /**
     * Calculates the Manhattan distance to another position.
     * Manhattan distance = |row1 - row2| + |col1 - col2|.
     * @param other the target position.
     * @return the distance, or Integer.MAX_VALUE if other is null.
     */
    public int distanceTo(Position other) {
        if (other == null) {
            return Integer.MAX_VALUE;
        }
        return Math.abs(this.row - other.row) + Math.abs(this.col - other.col);
    }

    /**
     * Checks equality based on row and column coordinates.
     * @param o the object to compare.
     * @return true if same coordinates.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row && col == position.col;
    }

    /**
     * @return a string representation in (row,col) format.
     */
    @Override
    public String toString() {
        return "(" + row + "," + col + ")";
    }
}
