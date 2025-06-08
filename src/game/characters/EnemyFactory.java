package game.characters;

import game.characterBuilders.*;
import game.map.Position;

import java.util.*;
import java.util.function.Supplier;

public class EnemyFactory {
    private final Map<String, Supplier<AbstractCharacter>> creators = new HashMap<>();

    public EnemyFactory(Position pos) {
        creators.put("Goblin", () -> {
            EnemyBuilder builder = new EnemyBuilder();
            builder.setPosition(pos);
            builder.randomizeStats();
            return builder.build("Goblin");
        });

        creators.put("Orc", () -> {
            EnemyBuilder builder = new EnemyBuilder();
            builder.setPosition(pos);
            builder.randomizeStats();
            return builder.build("Orc");
        });

        creators.put("Dragon", () -> {
            EnemyBuilder builder = new EnemyBuilder();
            builder.setPosition(pos);
            builder.randomizeStats();
            return builder.build("Dragon");
        });
    }

    public AbstractCharacter create(String type) {
        Supplier<AbstractCharacter> creator = creators.get(type);
        if (creator == null)
            throw new IllegalArgumentException("Unknown enemy type: " + type);
        return creator.get();
    }
}