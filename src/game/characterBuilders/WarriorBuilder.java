package game.characterBuilders;

import game.characters.*;
import game.map.Position;

/**
 * WarriorBuilder is a concrete builder for creating Warrior characters.
 * It implements the CharacterBuilder interface to define how to build a Warrior.
 */
public class WarriorBuilder implements CharacterBuilder {

    /**
     * Default constructor for WarriorBuilder.
     * Initializes a new instance of the WarriorBuilder.
     */
    public WarriorBuilder() {
    }

    /**
     * Builds the warrior health by setting it to a base value plus the specified health.
     * The base health is set to 100.
     * @param health The amount of health to add to the warrior's base health.
     */
    public void buildHealth(int health) {
        warrior.setHealth(100 + health);
    }

    /**
     * Gets the warrior character that has been created by this builder.
     * @return The constructed Warrior character.
     */
    public AbstractCharacter getCharacter() {
        return warrior;
    }

    /**
     * Builds the warrior defence by adding the specified defence to the current defence.
     * @param defence The amount of defence to add to the warrior's current defence.
     */
    public void buildDefence(int defence) {
        int oldDefence = warrior.getDefence();
        warrior.setDefence(oldDefence + defence);
    }

    /**
     * Builds a Warrior character with the specified name and position.
     * Initializes the warrior with a base health of 100.
     * @param name the name of the warrior
     * @param pos the position of the warrior on the map
     */
    public void build(String name , Position pos ) {
        warrior = new Warrior(name, pos , 100);

    }

    /**
     * Builds the warrior power by adding the specified power to the current power.
     * @param power The amount of power to add to the warrior's current power.
     */
    @Override
    public void buildPower(int power) {
        int oldPower = warrior.getPower();
        warrior.setPower(oldPower + power);
    }

    // --- Fields ---
    private Warrior warrior;

}