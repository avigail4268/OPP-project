package game.engine;

import game.combat.MagicElement;
import game.controller.GameController;
import game.characters.*;
import game.combat.CombatSystem;
import game.core.GameEntity;
import game.gameSaver.GameMemento;
import game.items.*;
import game.log.LogManager;
import game.map.GameMap;
import game.map.Position;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import game.observer.GameObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * The GameWorld class manages the overall game state, including the map,
 * player, enemies, and items. It initializes the game, places entities on
 * the map, and handles player actions like movement, combat, and item pickup.
 */
public class GameWorld {

    /**
     * Constructs a new GameWorld with a given map size, player type, and player name.
     *
     * @param size       the size of the game map (NxN)
     * @param playerType the type of player: 1-Warrior, 2-Mage, 3-Archer
     * @param playerName the name of the player character
     */
    public GameWorld(int size, int playerType, String playerName, Map<String, Integer> attributes, MagicElement element) {
        this.map = GameMap.getInstance(size);
        this.players = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.items = new ArrayList<>();
        this.enemyTasks = new ArrayList<>();
        int N = (int) (map.getSize() * map.getSize() * 0.03);
        if (N > 10)
            N = 10;
        this.enemyExecutor = Executors.newFixedThreadPool(N);
        LogManager.startLogger();
        createPlayer(playerType, playerName, attributes, element);
        populateGameMap();
    }

    public GameWorld(GameMap map, List<Enemy> enemies, List<GameItem> items, PlayerCharacter player) {
        System.out.println("Creating a new world");
        this.map = map;
        this.players = new ArrayList<>();
        this.players.add(player); // הוספת השחקן לרשימת השחקנים
        this.enemies = enemies;
        this.items = items;
        this.enemyTasks = new ArrayList<>();
        int N = (int) (map.getSize() * map.getSize() * 0.03);
        if (N > 10)
            N = 10;
        this.enemyExecutor = Executors.newFixedThreadPool(N);
        LogManager.startLogger();
    }


    /**
     * Creates and adds a player of the selected type to a random empty position on the map.
     *
     * @param playerType the selected player type (1-3)
     * @param playerName the name of the player
     */
    private void createPlayer(int playerType, String playerName, Map<String, Integer> attributes, MagicElement element) {
        Position pos = map.getRandomEmptyPosition();
        PlayerCharacter player;
        player = playerFactory.createPlayerFactory(playerType, playerName, pos, attributes, element);
        map.addToGrid(pos, player);
        players.add(player);
        LogManager.addLog("Game started with player type: " + player.getDisplaySymbol() + " and name: " + playerName);
    }

    /**
     * Populates the map with random entities such as enemies, walls, and items.
     * Uses random probabilities to determine what to place in each empty cell.
     */
    private void populateGameMap() {
        // Randomly place enemies, walls, and items on the map
        for (int i = 0; i < map.getSize(); i++) {
            for (int j = 0; j < map.getSize(); j++) {
                Position pos = new Position(i, j);
                if (!map.isEmpty(pos)) continue;

                double random = Math.random();
                if (random <= 0.4) continue; // Leave cell empty
                else if (random <= 0.7) createEnemy(pos);
                else if (random < 0.8) createWall(pos);
                else if (random < 0.95) createPotion(pos);
                else createPowerPotion(pos);
            }
        }
    }

    /**
     * Creates and places an enemy at the specified position.
     * The type of enemy is randomly selected from Dragon, Orc, or Goblin.
     *
     * @param pos the position to place the enemy
     */
    public void createEnemy(Position pos) {
        Enemy enemy = enemyFactory.createEnemy(pos);
        enemies.add(enemy);
        map.addToGrid(pos, enemy);
    }

    /**
     * Creates and places a health potion.
     *
     * @param pos the position to place the potion
     */
    private void createPotion(Position pos) {
        Potion potion = new Potion(pos, false, 50, 10);
        items.add(potion);
        map.addToGrid(pos, potion);
    }

    /**
     * Creates and places a power potion.
     *
     * @param pos the position to place the power potion
     */
    private void createPowerPotion(Position pos) {
        PowerPotion powerPotion = new PowerPotion(pos, false, 5, 1);
        items.add(powerPotion);
        map.addToGrid(pos, powerPotion);
    }

    /**
     * Creates and places a wall.
     *
     * @param pos the position to place the wall
     */
    private void createWall(Position pos) {
        Wall wall = new Wall(pos, true);
        items.add(wall);
        map.addToGrid(pos, wall);
    }

    /**
     * Checks if a player's move from one position to another is valid.
     *
     * @param from the original position
     * @param to   the intended new position
     * @return true if the move is valid, false otherwise
     */
    public boolean isValidMove(Position from, Position to) {
        if (to.getRow() < 0 || to.getRow() >= map.getSize() || to.getCol() < 0 || to.getCol() >= map.getSize()) {
            LogManager.addLog("Move out of bounds");
            return false;
        }
        int rowDiff = Math.abs(to.getRow() - from.getRow());
        int colDiff = Math.abs(to.getCol() - from.getCol());
        if (rowDiff + colDiff > 1) {
            return false;
        }
        if (from.distanceTo(to) >= 2) return false;

        List<GameEntity> entities = map.getEntitiesAt(to);
        for (GameEntity entity : entities) {
            if (entity instanceof Wall) {
                LogManager.addLog("Cannot move through wall at " + to);
                return false;
            }
        }
        return true;
    }

    public void movePlayerTo(Position newPos) {
        PlayerCharacter player = getPlayer();
        Position oldPos = player.getPosition();
        player.setPosition(newPos);
        LogManager.addLog("Player moved from " + oldPos + " to " + newPos);
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

    public List<EnemyTask> getEnemyTasks() {
        return enemyTasks;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public GameMap getMap() {
        return map;
    }

    public PlayerCharacter getPlayer() {
        return players.get(0);
    }

    public List<GameItem> getItems() {
        return items;
    }

    public void startEnemyTask() {
        for (Enemy enemy : enemies) {
            EnemyTask enemy_Task = new EnemyTask(enemy, this);
            enemyTasks.add(enemy_Task);
            enemyExecutor.submit(enemy_Task);
        }
    }

    public void setController(GameController controller) {
        this.controller = controller;
    }

    public void setMap(GameMap map) {
        this.map = map;
    }

    public GameController getController() {
        return controller;
    }

    public ExecutorService getEnemyExecutor() {
        return enemyExecutor;
    }

    public void shutdown() {
        // Stop all enemy tasks
        for (EnemyTask ET : enemyTasks) {
            ET.stop();
        }
        enemyExecutor.shutdown();
        try {
            // Wait for all tasks to finish
            if (!enemyExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                enemyExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            // If the current thread is interrupted, force shutdown
            enemyExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers() {
        for (GameObserver observer : observers) {
            observer.onGameUpdated();
        }
    }

    public void setItems(List<GameItem> items) {
        this.items = items;
    }

    public static ReentrantLock getMapLock(Position pos) {
        return lockMap.computeIfAbsent(pos, p -> new ReentrantLock());
    }

    public AtomicBoolean getIsGameRunning() {
        return isGameRunning;
    }

    public void restoreFromMemento(GameMemento memento) {

        PlayerCharacter oldPlayer = getPlayer();
        map.removeFromGrid(oldPlayer.getPosition(), oldPlayer);
        this.players.clear();
        this.players.add(memento.getPlayer());
        this.enemies = memento.getEnemies();
        this.items = memento.getItems();
        this.map.setGrid(memento.getGameMap().copyGrid());
        map.addToGrid(memento.getPlayer().getPosition(), memento.getPlayer());
        this.controller.refresh();
    }


    // --- Fields ---
    // Map for locking specific positions in the game world
    private static final ConcurrentHashMap<Position, ReentrantLock> lockMap = new java.util.concurrent.ConcurrentHashMap<>();
    // Game-related fields
    private GameMap map;
    private List<PlayerCharacter> players;
    private List<Enemy> enemies;
    private List<GameItem> items;
    // Executor for running enemy behavior on a schedule
    private ExecutorService enemyExecutor;
    // List of enemy tasks that control enemy behavior
    private List<EnemyTask> enemyTasks;
    // List of observers to be notified when the game state updates
    private List<GameObserver> observers = new ArrayList<>();
    // Controller for handling game interactions
    private GameController controller;
    // Flag to indicate if the game is running
    private final AtomicBoolean isGameRunning = new AtomicBoolean(true);
    private final PlayerFactory playerFactory = new PlayerFactory();
    private final EnemyFactory enemyFactory = new EnemyFactory();


}