package game.characterBuilders;
import game.characters.AbstractCharacter;
import game.characters.Mage;
import game.combat.MagicElement;
import game.map.Position;

/**
 * MageBuilder is a concrete builder for creating Mage characters.
 * It implements the CharacterBuilder interface to define how to build a Mage.
 */
public class MageBuilder implements CharacterBuilder {

    /**
     * Default constructor for MageBuilder.
     * Initializes a new instance of the MageBuilder.
     */
    public MageBuilder() {
    }

    /**
     * Builds the mage element by setting it to the specified MagicElement.
     * @param element The MagicElement to set for the mage.
     */
    public void buildElement( MagicElement element) {
        mage.setElement(element);
    }

    /**
     * Builds the mage power by adding the specified power to the current power.
     * @param power The amount of power to add to the mage's current power.
     */
    @Override
    public void buildPower(int power) {
        int oldPower = mage.getPower();
        mage.setPower(oldPower + power);
    }

    /**
     * Builds the mage health by setting it to a base value plus the specified health.
     * The base health is set to 100.
     * @param health The amount of health to add to the mage's base health.
     */
    @Override
    public void buildHealth(int health) {
        mage.setHealth(100 + health);
    }

    /**
     * Gets the mage character that has been created by this builder.
     * @return The constructed Mage character.
     */
    @Override
    public AbstractCharacter getCharacter() {
        return mage;
    }

    /**
     * Builds a Mage character with the specified name and position.
     * Initializes the mage with a base health of 100.
     * @param name the name of the mage
     * @param pos the position of the mage on the map
     */
    @Override
    public void build(String name, Position pos) {
         mage = new Mage(name,pos,100);
    }

// --- Fields ---
    private Mage mage;

}
