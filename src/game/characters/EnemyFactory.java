package game.characters;

import game.characterBuilders.*;
import game.map.Position;
import java.util.*;
import java.util.function.Supplier;

public class EnemyFactory {
    private final Map<String, Supplier<CharacterBuilder>> enemySuppliers = new HashMap<>();

    public EnemyFactory() {
        enemySuppliers.put("Goblin", GoblinBuilder::new);
        enemySuppliers.put("Orc", OrcBuilder::new);
        enemySuppliers.put("Dragon", DragonBuilder::new);
    }

    public Enemy createEnemy(Position pos) {
        List<String> types = new ArrayList<>(enemySuppliers.keySet());
        String selected = types.get(new Random().nextInt(types.size()));

        CharacterBuilder builder = enemySuppliers.get(selected).get();
        builder.setPosition(pos);
        builder.randomizeStats();
        return (Enemy) builder.build();
    }
}
