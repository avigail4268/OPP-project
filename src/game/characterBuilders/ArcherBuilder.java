package game.characterBuilders;

import game.characters.AbstractCharacter;
import game.characters.Archer;
import game.map.Position;

/**
 * ArcherBuilder is a concrete builder for creating Archer characters.
 * It implements the CharacterBuilder interface to define how to build an Archer.
 */
public class ArcherBuilder implements CharacterBuilder {

    /**
     * Default constructor for ArcherBuilder.
     * Initializes a new instance of the ArcherBuilder.
     */
    public ArcherBuilder() {}

    /**
     * Builds the archer power by adding the specified power to the current power.
     */
    @Override
    public void buildPower(int power) {
        int oldPower = archer.getPower();
        archer.setPower(oldPower + power);
    }

    /**
     * Builds the archer health by setting it to a base value plus the specified health.
     * The base health is set to 100.
     */
    @Override
    public void buildHealth(int health) {
         archer.setHealth(100 + health);
    }

    /**
     * gets the archer character that created by this builder.
     */
    @Override
    public AbstractCharacter getCharacter() {
        return archer;
    }

    /**
     * Builds an Archer character with the specified name and position.
     * Initializes the archer with a base health of 100.
     *
     * @param name the name of the archer
     * @param pos the position of the archer on the map
     */
    @Override
    public void build(String name, Position pos) {
        archer = new Archer(name,pos,100);
    }

    // --- Fields ---
    private Archer archer;

}
