package game.gameSaver;

import game.characters.Enemy;
import game.characters.PlayerCharacter;
import game.items.GameItem;
import game.map.GameMap;

import java.util.ArrayList;
import java.util.List;

public class GameOriginator {
    private PlayerCharacter player;
    private GameMap gameMap;
    private List <Enemy> enemies;
    private List <GameItem> items;

    public PlayerCharacter getPlayer() {
        return player;
    }
    public void setPlayer(PlayerCharacter player) {
        this.player = (PlayerCharacter) player.deepCopy();
    }
    public GameMap getGameMap() {
        return gameMap;
    }
    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }
    public List<Enemy> getEnemies() {
        return enemies;
    }
    public void setEnemies(List<Enemy> enemies) {
        this.enemies = new ArrayList<>();
        for (Enemy enemy : enemies) {
            this.enemies.add((Enemy) enemy.deepCopy());
        }

    }
    public List<GameItem> getItems() {
        return items;
    }
    public void setItems(List<GameItem> items) {
        this.items = new ArrayList<>();
        for (GameItem item : items) {
            this.items.add((GameItem) item.deepCopy());
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
