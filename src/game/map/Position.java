package game.map;

/**
 * Represents a position on the game map, identified by its row and column.
 * The position can be used to calculate distances and check equality between positions.
 */
public class Position {

    /**
     * Constructs a position with the specified row and column.
     * @param r the row of the position
     * @param c the column of the position
     */
    public Position(int r, int c) {
        this.row = r;
        this.col = c;
    }

    /**
     * Copy constructor that creates a new Position object from an existing Position object.
     * This allows for creating a new position with the same row and column as the given position.
     * @param pos the Position object to copy
     */
    public Position (Position pos) {
        this.row = pos.row;
        this.col = pos.col;
    }

    /**
     * Calculates the Manhattan distance to another position.
     * The Manhattan distance is the sum of the absolute differences of their row and column coordinates.
     * @param otherPos the other position to calculate the distance to
     * @return the Manhattan distance between this position and the other position
     */
    public int distanceTo(Position otherPos) {
        int otherRow = otherPos.getRow();
        int otherCol = otherPos.getCol();
        return Math.abs(this.row - otherRow) + Math.abs(this.col - otherCol);
    }

    /**
     * Returns the row of the position.
     * @return the row of the position
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column of the position.
     * @return the column of the position
     */
    public int getCol() {
        return col;
    }

    /**
     * Checks if this position is equal to another position.
     * Two positions are equal if their row and column are the same.
     * @param otherPos the other position to compare with
     * @return true if the positions are equal, false otherwise
     */
    public boolean equals(Position otherPos) {
        return otherPos.row == row && otherPos.col == col;
    }

    /**
     * Checks if this position is equal to another object.
     * This method overrides the default equals method to compare Position objects.
     * @param obj the object to compare with
     * @return true if the object is a Position and has the same row and column, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position other) {
            return this.row == other.row && this.col == other.col;
        }
        return false;
    }

    /**
     * Returns a string representation of the position.
     * The format is "(row, column)".
     * @return a string representation of the position
     */
    @Override
    public String toString() {
        return "(" + row + "," + col + ")";
    }

    /**
     * Returns the hash code for this position.
     * The hash code is computed based on the row and column of the position.
     * @return the hash code for the position
     */
    @Override
    public int hashCode() {
        return java.util.Objects.hash(row, col);
    }

    // --- Fields ---
    private int row, col;
}
