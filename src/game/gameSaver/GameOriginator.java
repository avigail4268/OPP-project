package game.gameSaver;

import game.characters.Enemy;
import game.characters.PlayerCharacter;
import game.core.GameEntity;
import game.engine.GameWorld;
import game.items.GameItem;
import game.map.GameMap;
import game.map.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameOriginator {
    private PlayerCharacter player;
    private GameMap gameMap;
    private Map<Position, List<GameEntity>> savedGrid;
    private List<Enemy> enemies;
    private List<GameItem> items;

    public PlayerCharacter getPlayer() {
        return player;
    }

    public void setPlayer(PlayerCharacter player) {
        this.player = (PlayerCharacter) player.deepCopy();
    }

    public GameMap getGameMap() {
        return gameMap;
    }

//    public void setGameMap( GameMap gameMap ) {
//        GameMap.getInstance(gameMap.getSize()).setGrid(savedGrid);
//    }
    public void setGameMap(GameMap map) {
        this.gameMap = GameMap.getInstance(map.getSize()); // שומרת את האינסטנס בגודל הנכון
        this.savedGrid = map.copyGrid(); // מעתיקה את תוכן הגריד
    }


//    public void setGrid(GameMap gameMap) {
//        this.savedGrid = gameMap.copyGrid();
//    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = new ArrayList<>();
        for (Enemy enemy : enemies) {
            this.enemies.add((Enemy)enemy.deepCopy());
        }

    }

    public List<GameItem> getItems() {
        return items;
    }

    public void setItems(List<GameItem> items) {
        this.items = new ArrayList<>();
        for (GameItem item : items) {
            this.items.add(item.deepCopy());
        }
    }

    public GameMemento createMemento() {
        return new GameMemento(player, gameMap, enemies, items);
    }

    public void setMemento(GameMemento memento) {
        this.player = memento.getPlayer();
        this.gameMap = memento.getGameMap();
        this.enemies = memento.getEnemies();
        this.items = memento.getItems();
    }
}
