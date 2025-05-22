
package game.engine;

import game.controller.GameController;
import game.characters.*;
import game.combat.CombatSystem;
import game.core.GameEntity;
import game.items.*;
import game.log.LogManager;
import game.map.GameMap;
import game.map.Position;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Random;
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
     * @param size       the size of the game map (NxN)
     * @param playerType the type of player: 1-Warrior, 2-Mage, 3-Archer
     * @param playerName the name of the player character
     */
    public GameWorld(int size, int playerType, String playerName) {
        this.map = new GameMap(size);
        this.players = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.items = new ArrayList<>();
        this.enemyTasks = new ArrayList<>();
        this.enemyExecutor = Executors.newScheduledThreadPool(4);

        LogManager.startLogger();
        createPlayer(playerType, playerName);
        populateGameMap();
    }

    /**
     * Creates and adds a player of the selected type to a random empty position on the map.
     * @param playerType the selected player type (1-3)
     * @param playerName the name of the player
     */
    private void createPlayer(int playerType, String playerName) {
        Position pos = map.getRandomEmptyPosition();
        PlayerCharacter player;
        switch (playerType) {
            case 1 -> player = new Archer(playerName, pos, 100);
            case 2 -> player = new Mage(playerName, pos, 100);
            case 3 -> player = new Warrior(playerName, pos, 100);
            default -> throw new IllegalArgumentException("Invalid player type: " + playerType);
        }
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
     * @param pos the position to place the enemy
     */
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
     * @param pos the position to place the power potion
     */
    private void createPowerPotion(Position pos) {
        PowerPotion powerPotion = new PowerPotion(pos, false, 5, 1);
        items.add(powerPotion);
        map.addToGrid(pos, powerPotion);
    }

    /**
     * Creates and places a wall.
     * @param pos the position to place the wall
     */
    private void createWall(Position pos) {
        Wall wall = new Wall(pos, true);
        items.add(wall);
        map.addToGrid(pos, wall);
    }

    /**
     * Checks if a player's move from one position to another is valid.
     * @param from   the original position
     * @param to     the intended new position
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

    /**
     * Moves the player to a new position on the map.
     * @param newPos the new position to move to
     */
    public void movePlayerTo(Position newPos) {
        PlayerCharacter player = getPlayer();
        Position oldPos = player.getPosition();
        player.setPosition(newPos);
        LogManager.addLog("Player moved from " + oldPos + " to " + newPos);
        map.removeFromGrid(oldPos, player);
        map.addToGrid(newPos, player);
    }

    /**
     * Initiates combat with an enemy at the given position.
     * If the enemy is defeated, replaces it with a treasure.
     * @param pos the position where the combat occurs
     */
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

    /**
     * Picks up an interactable item at the specified position.
     * @param pos the position of the item to pick up
     */
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

    /**
     * Checks if a map cell at (row, col) is visible to the player (within distance 2).
     * @param row the row index
     * @param col the column index
     * @return true if the cell is within visibility range, false otherwise
     */
    public boolean isVisibleToPlayer(int row, int col) {
        Position playerPos = getPlayer().getPosition();
        Position newPos = new Position(row, col);
        int distance = playerPos.distanceTo(newPos);
        return distance <= 2;
    }

    /**
     * Returns the game map.
     * @return the GameMap instance
     */
    public GameMap getMap() {
        return map;
    }

    /**
     * Returns the main player character.
     * @return the PlayerCharacter instance
     */
    public PlayerCharacter getPlayer() {
        return players.get(0);
    }

    /**
     * Returns a list of all game items on the map.
     * @return the list of GameItem instances
     */
    public List<GameItem> getItems() {
        return items;
    }

    /**
     * Starts the scheduled enemy tasks with random intervals.
     * Each enemy gets its own scheduled task for movement or behavior.
     */
    public void startEnemyTask() {
        Random random = new Random();
        for (Enemy enemy : enemies) {
            EnemyTask enemy_Task = new EnemyTask(enemy, this);
            enemyTasks.add(enemy_Task);

            // Schedule enemy movement with random intervals between 1 and 1.5 seconds
            long initialDelay = random.nextInt(1000);
            long period = 1000 + random.nextInt(500);

            ScheduledFuture<?> task = enemyExecutor.scheduleAtFixedRate(enemy_Task, initialDelay, period, TimeUnit.MILLISECONDS);
            enemy_Task.setScheduledTask(task);
        }
    }

    /**
     * Sets the game controller.
     * @param controller the controller to set
     */
    public void setController(GameController controller) {
        this.controller = controller;
    }

    /**
     * Gets the current game controller.
     * @return the controller
     */
    public GameController getController() {
        return controller;
    }

    /**
     * Shuts down all enemy tasks and terminates the executor.
     * Ensures graceful termination, and forces shutdown if it takes too long.
     */
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

    /**
     * Registers an observer to receive updates from the game world.
     * @param observer the observer to add
     */
    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    /**
     * Notifies all registered observers that the game state has been updated.
     */
    public void notifyObservers() {
        for (GameObserver observer : observers) {
            observer.onGameUpdated();
        }
    }

    /**
     * Returns a lock object for a specific position in the map.
     * Ensures that access to that position can be synchronized across threads.
     * @param pos the position to lock
     * @return the corresponding ReentrantLock
     */
    public static ReentrantLock getMapLock(Position pos) {
        return lockMap.computeIfAbsent(pos, p -> new ReentrantLock());
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
    private final ScheduledExecutorService enemyExecutor;
    // List of enemy tasks that control enemy behavior
    private final List<EnemyTask> enemyTasks;
    // List of observers to be notified when the game state updates
    private List<GameObserver> observers = new ArrayList<>();
    // Controller for handling game interactions
    private GameController controller;
}