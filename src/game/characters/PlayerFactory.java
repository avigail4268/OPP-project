package game.characters;

import game.characterBuilders.*;
import game.combat.MagicElement;
import game.map.Position;

import java.util.*;
import java.util.function.Supplier;

public class PlayerFactory {
    private final Map<Integer, Supplier<CharacterBuilder>> builders = new HashMap<>();

    public PlayerFactory() {
        builders.put(3, WarriorBuilder::new);
        builders.put(1, ArcherBuilder::new);
        builders.put(2, MageBuilder::new);
    }

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
}
