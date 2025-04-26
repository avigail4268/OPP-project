package game.items;

import game.core.GameEntity;
import game.map.Position;

/**
 * Represents a generic item in the game world.
 *
 * Game items have a position, visibility status, description, and
 * can optionally block movement.
 */
public abstract class GameItem implements GameEntity {

    /**
     * Constructs a new game item.
     *
     * @param position the initial position of the item
     * @param blocksMovement true if the item blocks movement, false otherwise
     */
    public GameItem(Position position, boolean blocksMovement) {
        this.position = position;
        this.blocksMovement = blocksMovement;
        this.visible = false;
        this.description = "This is a game item";
    }

    /**
     * Returns the current position of the item.
     *
     * @return the position of the item
     */
    @Override
    public Position getPosition() {
        return position;
    }

    /**
     * Sets whether the item is visible.
     *
     * @param visible true to make the item visible, false to hide it
     */
    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Checks if the item is visible.
     *
     * @return true if the item is visible, false otherwise
     */
    @Override
    public boolean getVisible() {
        return visible;
    }

    /**
     * Sets the position of the item.
     *
     * @param newPos the new position to assign
     * @return true if the position was successfully updated, false if newPos is null
     */
    @Override
    public boolean setPosition(Position newPos) {
        if (newPos != null) {
            this.position = newPos;
            return true;
        }
        return false;
    }

    /**
     * Returns a string representation of the item.
     *
     * @return a string describing the item
     */
    @Override
    public abstract String toString();

    /**
     * Compares this item to another object for equality.
     *
     * @param obj the object to compare with
     * @return true if the objects are considered equal, false otherwise
     */
    @Override
    public abstract boolean equals(Object obj);

    /**
     * Returns the symbol used to display this item.
     *
     * @return a string representing the display symbol
     */
    @Override
    public abstract String getDisplaySymbol();

    /**
     * Returns the color code used to render this item.
     *
     * @return a string representing the color code
     */
    public abstract String getColorCode();

    /**
     * Checks whether the item blocks movement.
     *
     * @return true if the item blocks movement, false otherwise
     */
    public boolean isBlocksMovement() {
        return blocksMovement;
    }

    /**
     * Returns the description of the item.
     *
     * @return the item's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the item.
     *
     * @param description the new description to assign
     */
    protected void setDescription(String description) {
        this.description = description;
    }

    private Position position;
    private final boolean blocksMovement;
    private String description;
    private boolean visible;
}

