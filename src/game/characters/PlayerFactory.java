package game.characters;

import game.characterBuilders.CharacterBuilder;
import game.characterBuilders.EnemyBuilder;
import game.map.Position;

import java.util.*;
import java.util.function.Supplier;

public class PlayerFactory {
    private final Map<String, Supplier<CharacterBuilder>> builders = new HashMap<>();

    public PlayerFactory() {
        builders.put("Warrior", EnemyBuilder::new);
        builders.put("Archer", EnemyBuilder::new);
        builders.put("Mage", EnemyBuilder::new);
    }
    public Enemy createPlayer (String type , Position pos) {
        List<String> types = new ArrayList<>(builders.keySet());
        CharacterBuilder builder = builders.get(type).get();
        builder.build(selected,pos);
        builder.randomizeStats();
        return (Enemy)builder.getCharacter();
    }
}
