package game.core;
import game.map.Position;

/**
 * Represents a generic entity in the game world.
 * An entity has a position, visibility status, display symbol, and color.
 */
public interface GameEntity {

    /**
     * Gets the current position of the entity.
     * @return the current  Position of the entity
     */
    Position getPosition();

    /**
     * Sets the position of the entity.
     * @param newPos the new Position to set
     * @return true if the position was successfully updated, false otherwise
     */
    boolean setPosition(Position newPos);

    /**
     * Gets the symbol that represents the entity for display purposes.
     * @return a  String representing the display symbol
     */
    String getDisplaySymbol();

    /**
     * Sets the visibility status of the entity.
     * @param visible true to make the entity visible, false to hide it
     */
    void setVisible(boolean visible);

    /**
     * Checks whether the entity is currently visible.
     * @return true if the entity is visible, false otherwise
     */
    boolean getVisible();

    /**
     * make a deep copy of a specific entity.
     * @return a String representing the color
     */
    GameEntity deepCopy();
}
