package game.engine;
import game.characters.*;
import game.combat.CombatSystem;
import game.core.GameEntity;
import game.items.*;
import game.map.GameMap;
import game.map.Position;
import java.util.ArrayList;
import java.util.List;

public class GameWorld {
    private GameMap map;
    private List<PlayerCharacter> players;
    private List<Enemy> enemies;
    private List<GameItem> items;

    public GameWorld(int size, int playerType, String playerName) {
        this.map = new GameMap(size);
        this.players = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.items = new ArrayList<>();
        createPlayer(playerType, playerName);
        populateGameMap();
    }

    private void createPlayer(int playerType, String playerName) {
        Position pos = map.getRandomEmptyPosition();
        PlayerCharacter player;
        switch (playerType) {
            case 1 -> player = new Warrior(playerName, pos, 100);
            case 2 -> player = new Mage(playerName, pos, 100);
            case 3 -> player = new Archer(playerName, pos, 100);
            default -> throw new IllegalArgumentException("Invalid player type: " + playerType);
        }
        map.addToGrid(pos, player);
        players.add(player);
    }

    private void populateGameMap() {
        for (int i = 0; i < map.getSize(); i++) {
            for (int j = 0; j < map.getSize(); j++) {
                Position pos = new Position(i, j);
                if (!map.isEmpty(pos)) continue;
                double random = Math.random();
                if (random <= 0.4) continue;
                else if (random <= 0.7) createEnemy(pos);
                else if (random < 0.8) createWall(pos);
                else if (random < 0.95) createPotion(pos);
                else createPowerPotion(pos);
            }
        }
    }

    public void createEnemy(Position pos) {
        double random = Math.random();
        Enemy enemy;
        if (random <= 1.0 / 3.0) {
            enemy = new Dragon(pos, 50);
        } else if (random <= 2.0 / 3.0) {
            enemy = new Orc(pos, 50);
        } else {
            enemy = new Goblin(pos, 50);
        }
        enemies.add(enemy);
        map.addToGrid(pos, enemy);
    }

    private void createPotion(Position pos) {
        Potion potion = new Potion(pos, false, 50, 10);
        items.add(potion);
        map.addToGrid(pos, potion);
    }

    private void createPowerPotion(Position pos) {
        PowerPotion powerPotion = new PowerPotion(pos, false, 5, 1);
        items.add(powerPotion);
        map.addToGrid(pos, powerPotion);
    }

    private void createWall(Position pos) {
        Wall wall = new Wall(pos, true);
        items.add(wall);
        map.addToGrid(pos, wall);
    }

    public boolean isValidMove(Position from, Position to, PlayerCharacter player) {
        if (to.getRow() < 0 || to.getRow() >= map.getSize() ||
                to.getCol() < 0 || to.getCol() >= map.getSize()) {
            return false;
        }

        int rowDiff = Math.abs(to.getRow() - from.getRow());
        int colDiff = Math.abs(to.getCol() - from.getCol());
        if (rowDiff + colDiff > 1) return false;

        List<GameEntity> entities = map.getEntitiesAt(to);
        for (GameEntity entity : entities) {
            if (entity instanceof Wall) return false;
        }

        return true;
    }

    public void movePlayerTo(Position newPos) {
        PlayerCharacter player = getPlayer();
        Position oldPos = player.getPosition();
        player.setPosition(newPos);
        map.removeFromGrid(oldPos, player);
        map.addToGrid(newPos, player);
    }

    public void fightEnemyAt(Position pos) {
        List<GameEntity> entities = map.getEntitiesAt(pos);
        for (GameEntity entity : entities) {
            if (entity instanceof Enemy enemy) {
                CombatSystem.resolveCombat(getPlayer(), enemy);
                if (enemy.isDead()) {
                    Treasure treasure = enemy.defeat();
                    map.removeFromGrid(pos, entity);
                    enemies.remove(enemy);
                    map.addToGrid(pos, treasure);
                    items.add(treasure);
                }
                break;
            }
        }
    }

    public void pickUpItemAt(Position pos) {
        List<GameEntity> entities = map.getEntitiesAt(pos);
        for (GameEntity entity : entities) {
            if (entity instanceof Interactable item) {
                item.collect(getPlayer());
                map.removeFromGrid(pos, entity);
                items.remove(item);
                break;
            }
        }
    }

    public boolean isVisibleToPlayer(int row, int col) {
        Position playerPos = getPlayer().getPosition();
        Position newPos = new Position(row, col);
        int distance = playerPos.distanceTo(newPos);
        return distance <= 2;
    }

    public GameMap getMap() {
        return map;
    }

    public PlayerCharacter getPlayer() {
        return players.get(0);
    }

    public List<PlayerCharacter> getPlayers() {
        return players;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<GameItem> getItems() {
        return items;
    }
}