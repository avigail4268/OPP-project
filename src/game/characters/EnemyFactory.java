package game.characters;

import game.characterBuilders.*;
import game.map.Position;

import java.util.*;
import java.util.function.Supplier;

public class EnemyFactory {
    private final Map<String, Supplier<CharacterBuilder>> builders = new HashMap<>();


    public EnemyFactory() {
        builders.put("Goblin", GoblinBuilder::new);
        builders.put("Orc", OrcBuilder::new);
        builders.put("Dragon", DragonBuilder::new);
    }

    public Enemy createEnemy(Position pos) {
        List<String> types = new ArrayList<>(builders.keySet());
        String selected = types.get(new Random().nextInt(types.size()));

        CharacterBuilder builder = builders.get(selected).get();
        builder.buildPosition(pos);
        builder.randomizeStats();
        return (Enemy) builder.build();
    }
}
