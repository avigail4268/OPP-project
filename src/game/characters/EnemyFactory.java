package game.characters;
import game.characterBuilders.*;
import game.map.Position;
import java.util.*;
import java.util.function.Supplier;

/**
 * EnemyFactory is responsible for creating enemy characters in the game.
 * It uses a map of builders to instantiate different types of enemies.
 */
public class EnemyFactory {

    /**
     * Constructs an EnemyFactory and initializes the builders for different enemy types.
     * Each builder is associated with a specific enemy type.
     */
    public EnemyFactory() {
        builders.put("Goblin", EnemyBuilder::new);
        builders.put("Orc", EnemyBuilder::new);
        builders.put("Dragon", EnemyBuilder::new);
    }

    /**
     * Creates a random enemy character at the specified position.
     * The type of enemy is randomly selected from the available builders.
     * @param pos the position where the enemy will be created
     * @return a new Enemy instance
     */
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

    // --- Fields ---
    private final Map<String, Supplier<CharacterBuilder>> builders = new HashMap<>();
}
