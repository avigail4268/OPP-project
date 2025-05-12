package game.items;

import game.map.Position;

/**
 * Represents a wall entity on the game map.

 * Walls block player movement and cannot be passed through.
 */
public class Wall extends GameItem {

    /**
     * Creates a new wall at the specified position.
     *
     * @param position the position of the wall
     * @param blocksMovement true if the wall blocks movement
     */
    public Wall(Position position, boolean blocksMovement) {
        super(position, blocksMovement);
        this.setDescription("This is a Wall, you can't pass it.");
    }

    /**
     * Returns a string representation of the wall.
     *
     * @return a string describing the wall and its position
     */
    @Override
    public String toString() {
        return "Wall at position " + getPosition();
    }

    /**
     * Compares this wall to another object for equality.
     * Two walls are equal if they are at the same position.
     *
     * @param obj the object to compare with
     * @return true if the walls are at the same position, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Wall other) {
            return this.getPosition().equals(other.getPosition());
        }
        return false;
    }

    /**
     * Returns the display symbol for the wall.
     *
     * @return "WALL" as the symbol representing the wall
     */
    @Override
    public String getDisplaySymbol() {
        return "Wall";
    }

}
