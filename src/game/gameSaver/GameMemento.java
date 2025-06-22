package game.gameSaver;
import game.characters.Enemy;
import game.characters.PlayerCharacter;
import game.core.GameEntity;
import game.items.GameItem;
import game.map.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * GameMemento is a class that represents a snapshot of the game state.
 * It contains the player character, game map, enemies, and items at a specific point in time.
 * This class is used in the Memento design pattern to save and restore game states.
 */
public class GameMemento {

    /**
     * Constructs a GameMemento with the current state of the game.
     * The constructor expects deep-copied objects to be passed in.
     */
    public GameMemento(PlayerCharacter player, List<Enemy> enemies, List<GameItem> items, Map<Position, List<GameEntity>> savedGrid) {
        this.player = player;
        this.enemies = enemies;
        this.items = items;
        this.savedGrid = savedGrid;
    }

    /**
     * Gets a deep copy of the player character from the memento.
     * @return a deep copy of the PlayerCharacter
     */
    public PlayerCharacter getPlayer() {
        return (PlayerCharacter) player.deepCopy();
    }

    /**
     * Gets a deep copy of the list of enemies from the memento.
     * @return a list of deep-copied Enemy instances
     */
    public List<Enemy> getEnemies() {
        List<Enemy> copy = new ArrayList<>();
        for (Enemy enemy : enemies) {
            copy.add((Enemy) enemy.deepCopy());
        }
        return copy;
    }

    /**
     * Gets a deep copy of the list of items from the memento.
     * @return a list of deep-copied GameItem instances
     */
    public List<GameItem> getItems() {
        List<GameItem> copy = new ArrayList<>();
        for (GameItem item : items) {
            copy.add(item.deepCopy());
        }
        return copy;
    }

    /**
     * Gets the game grid from the memento.
     * @return the saved grid as a map of positions to lists of game entities
     */
    public Map<Position, List<GameEntity>> getSavedGrid() {
        return savedGrid;
    }


    // --- Fields ---
    private final PlayerCharacter player;
    private final List<Enemy> enemies;
    private final List<GameItem> items;
    private final Map<Position, List<GameEntity>> savedGrid;

}
