package game.characters;

import game.characterBuilders.*;
import game.combat.MagicElement;
import game.map.Position;

import java.util.*;
import java.util.function.Supplier;

/**
 * PlayerFactory is responsible for creating player characters based on the specified type.
 * It uses the Builder pattern to construct different types of player characters (Warrior, Archer, Mage).
 */
public class PlayerFactory {

    /**
     * Constructor initializes the factory with available character builders.
     */
    public PlayerFactory() {
        builders.put(3, WarriorBuilder::new);
        builders.put(1, ArcherBuilder::new);
        builders.put(2, MageBuilder::new);
    }

    /**
     * Creates a player character based on the specified type, name, position, attributes, and magic element.
     * @param type       the type of player character (1 for Archer, 2 for Mage, 3 for Warrior)
     * @param name       the name of the player character
     * @param pos        the initial position of the player character on the map
     * @param attributes a map containing character attributes like Health, Power, and Defence
     * @param element    the magic element for Mage characters (can be null for non-Mage types)
     * @return a new PlayerCharacter instance
     */
    public PlayerCharacter createPlayerFactory(int type, String name, Position pos, Map<String, Integer> attributes, MagicElement element) {
        CharacterBuilder builder = builders.get(type).get();
        if (builder == null) {
            throw new IllegalArgumentException("Unknown player type: " + type);
        }
        builder.build(name, pos);
        builder.buildHealth(attributes.get("Health"));
        builder.buildPower(attributes.get("Power"));
        if (element != null) {
            if (builder instanceof MageBuilder mageBuilder)
                mageBuilder.buildElement(element);
        }
        if (builder instanceof WarriorBuilder warriorBuilder)
            warriorBuilder.buildDefence(attributes.get("Defence"));
        return (PlayerCharacter) builder.getCharacter();
    }

    // --- Fields ---
    private final Map<Integer, Supplier<CharacterBuilder>> builders = new HashMap<>();
}
