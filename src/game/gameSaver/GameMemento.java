package game.gameSaver;
import game.characters.Enemy;
import game.characters.PlayerCharacter;
import game.items.GameItem;
import game.map.GameMap;
import java.util.List;

/**
 * GameMemento is a class that represents a snapshot of the game state.
 * It contains the player character, game map, enemies, and items at a specific point in time.
 * This class is used in the Memento design pattern to save and restore game states.
 */
public class GameMemento {

    /**
     * Constructs a GameMemento with the current state of the game.
     * @param player the player character
     * @param gameMap the current game map
     * @param enemies the list of enemies in the game
     * @param items the list of items in the game
     */
    public GameMemento(PlayerCharacter player, GameMap gameMap, List<Enemy> enemies, List<GameItem> items) {
        this.player = player;
        this.gameMap = gameMap;
        this.enemies = enemies;
        this.items = items;
    }

    /**
     * gets the player character from the memento.
     * @return a new GameMemento instance with the same properties
     */
    public PlayerCharacter getPlayer() {
        return player;
    }

    /**
     * gets the game map from the memento.
     * @return the GameMap instance
     */
    public GameMap getGameMap() {
        return gameMap;
    }

    /**
     * gets the list of enemies from the memento.
     * @return the list of Enemy instances
     */
    public List<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * gets the list of items from the memento.
     * @return the list of GameItem instances
     */
    public List<GameItem> getItems() {
        return items;
    }

    // --- Fields ---
    private final PlayerCharacter player;
    private final GameMap gameMap;
    private final List<Enemy> enemies;
    private final List <GameItem> items;
}
