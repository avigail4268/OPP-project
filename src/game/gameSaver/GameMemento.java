package game.gameSaver;

import game.characters.Enemy;
import game.characters.PlayerCharacter;
import game.items.GameItem;
import game.map.GameMap;

import java.util.List;

public class GameMemento {
    private final PlayerCharacter player;
    private final GameMap gameMap;
    private final List<Enemy> enemies;
    private final List <GameItem> items;

    public GameMemento(PlayerCharacter player, GameMap gameMap, List<Enemy> enemies, List<GameItem> items) {
        this.player = player;
        this.gameMap = gameMap;
        this.enemies = enemies;
        this.items = items;
    }
    public PlayerCharacter getPlayer() {
        return player;
    }
    public GameMap getGameMap() {
        return gameMap;
    }
    public List<Enemy> getEnemies() {
        return enemies;
    }
    public List<GameItem> getItems() {
        return items;
    }
}
