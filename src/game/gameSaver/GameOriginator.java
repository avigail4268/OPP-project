package game.gameSaver;
import game.characters.Enemy;
import game.characters.PlayerCharacter;
import game.core.GameEntity;
import game.items.GameItem;
import game.map.GameMap;
import game.map.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * GameOriginator is a class responsible for creating and restoring game states.
 */
public class GameOriginator {

    /**
     * Gets the current player character.
     */
    public PlayerCharacter getPlayer() {
        return player;
    }

    /**
     * Sets the player character to a new instance.
     */
    public void setPlayer(PlayerCharacter player) {
        this.player = (PlayerCharacter) player.deepCopy();
    }

    /**
     * Sets the current game map to a new instance and saves the grid.
     */

    public void setGameMap(GameMap map) {
        Map<Position, List<GameEntity>> fullGrid = map.copyGrid();
        for (List<GameEntity> entities : fullGrid.values()) {
            entities.removeIf(e -> e instanceof PlayerCharacter);
        }

        this.savedGrid = fullGrid;
    }


    /**
     * Sets the enemies in the game.
     */
     public void setEnemies(List<Enemy> enemies) {
        this.enemies = new ArrayList<>();
        for (Enemy enemy : enemies) {
            this.enemies.add((Enemy)enemy.deepCopy());
        }

    }

    /**
     * Gets a list of the items that in the game.
     */
    public List<GameItem> getItems() {
        return items;
    }

    /**
     * Sets the items in the game.
     * Each item is deep copied to ensure the original items are not modified.
     */
    public void setItems(List<GameItem> items) {
        this.items = new ArrayList<>();
        for (GameItem item : items) {
            this.items.add(item.deepCopy());
        }
    }

    /**
     * Gets the memento of the current game state.
     */
     public GameMemento createMemento() {
        return new GameMemento(player, enemies, items, savedGrid);
    }

    // --- Fields ---
    private PlayerCharacter player;
    private Map<Position, List<GameEntity>> savedGrid;
    private List<Enemy> enemies;
    private List<GameItem> items;
}
