package game.characters;

import game.characterBuilders.*;
import game.map.Position;

import java.util.*;
import java.util.function.Supplier;

public class EnemyFactory {
    private final Map<String, Supplier<CharacterBuilder>> builders = new HashMap<>();


    public EnemyFactory() {
        builders.put("Goblin", EnemyBuilder::new);
        builders.put("Orc", EnemyBuilder::new);
        builders.put("Dragon", EnemyBuilder::new);
    }

    public Enemy createEnemy(Position pos) {
        List<String> types = new ArrayList<>(builders.keySet());
        String selected = types.get(new Random().nextInt(types.size()));
        CharacterBuilder builder = builders.get(selected).get();
        builder.build(selected,pos);
        if ( builder instanceof EnemyBuilder enemyBuilder){
            enemyBuilder.randomizeStats();
        }
        return (Enemy)builder.getCharacter();
    }
}
