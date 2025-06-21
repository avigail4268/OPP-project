package game.characterBuilders;
import game.characters.AbstractCharacter;
import game.map.Position;

/**
 * Interface for building characters in the game.
 * Provides methods to set character type, position, power, and health.
 */
public interface CharacterBuilder {
    /**
     * Sets the type of character to be built.
     * @param type The type of character (e.g., "Warrior", "Mage").
     * @param pos The position where the character will be placed.
     */
    void build(String type,Position pos);
    /**
     * Sets the power of the character.
     * @param power The power level of the character.
     */
    void buildPower(int power);
    /**
     * Sets the health of the character.
     * @param health The health points of the character.
     */
    void buildHealth(int health);
    /**
     * Retrieves the character that has been built.
     * @return The constructed character.
     */
    AbstractCharacter getCharacter();
}
