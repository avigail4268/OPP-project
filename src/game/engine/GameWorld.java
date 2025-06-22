package game.engine;

import game.combat.MagicElement;
import game.controller.GameController;
import game.characters.*;
import game.combat.CombatSystem;
import game.core.GameEntity;
import game.decorator.*;
import game.gameSaver.GameMemento;
import game.items.*;
import game.log.LogManager;
import game.map.GameMap;
import game.map.Position;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import game.observer.GameObserver;


/**
 * Represents the game world containing the map, players, enemies, items, and game logic.
 * It manages the game state, player actions, enemy behavior, and interactions with the game map.
 */
public class GameWorld {

    /**
     * Constructs a GameWorld with the specified parameters.
     * Initializes the game map, players, enemies, items, and enemy tasks.
     * @param size         The size of the game map (size x size).
     * @param playerType   The type of player character to create.
     * @param playerName   The name of the player character.
     * @param attributes   A map of attributes for the player character.
     * @param element      The magic element associated with the player character.
     * @param decorators   A list of decorators to apply to the player character.
     */
    public GameWorld(int size, int playerType, String playerName, Map<String, Integer> attributes, MagicElement element, List<String> decorators) {
        this.map = GameMap.getInstance(size);
        this.players = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.items = new ArrayList<>();
        this.enemyTasks = new ArrayList<>();
        int N = (int) (map.getSize() * map.getSize() * 0.03);
        if (N > 10)
            N = 10;
        this.enemyExecutor = Executors.newFixedThreadPool(N);
        createPlayer(playerType, playerName, attributes, element,decorators);
        populateGameMap();
    }

    /**
     * Creates a new enemy at the specified position and decorates it randomly.
     * Adds the enemy to the game world and the map.
     * @param pos The position where the enemy should be created.
     */
    public void createEnemy(Position pos) {
        Enemy enemy = enemyFactory.createEnemy(pos);
        enemies.add(enemy);
        map.addToGrid(pos, enemy);
        decorateEnemyRandomly(enemy);
    }

    /**
     * Checks if a move from one position to another is valid.
     * Validates bounds, distance, and checks for walls at the destination.
     * @param from The starting position.
     * @param to The destination position.
     * @return true if the move is valid, false otherwise.
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
     * Moves the player character to a new position on the map.
     * Updates the player's position, logs the move, and updates the map grid.
     * @param newPos The new position to move the player to.
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
     * Fights an enemy at the specified position.
     * @param pos The position where the enemy is located.
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
     * Picks up an item at the specified position.
     * If an interactable item is found, it collects the item and removes it from the map and items list.
     * @param pos The position where the item is located.
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
     * Checks if a position is visible to the player based on distance.
     * A position is considered visible if it is within 2 tiles of the player's position.
     * @param row The row of the position to check.
     * @param col The column of the position to check.
     * @return true if the position is visible to the player, false otherwise.
     */
    public boolean isVisibleToPlayer(int row, int col) {
        Position playerPos = getPlayer().getPosition();
        Position newPos = new Position(row, col);
        int distance = playerPos.distanceTo(newPos);
        return distance <= 2;
    }

    /**
     * Gets the list of enemy tasks that control enemy behavior.
     * @return A list of PlayerCharacter objects.
     */
    public List<EnemyTask> getEnemyTasks() {
        return enemyTasks;
    }

    /**
     * Gets the list of enemies in the game world.
     * @return A list of PlayerCharacter objects.
     */
    public List<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * Gets the game map of the game world.
     * @return The GameMap object representing the game world.
     */
    public GameMap getMap() {
        return map;
    }

    /**
     * Gets the player character in the game world.
     * @return The PlayerCharacter object representing the player.
     */
    public PlayerCharacter getPlayer() {
        return players.get(0);
    }

    /**
     * Gets the list of items in the game world.
     * @return A list of items
     */
    public List<GameItem> getItems() {
        return items;
    }

    /**
     * Starts the enemy tasks to control enemy behavior in the game world.
     * Each enemy is assigned a task that runs in a separate thread.
     */
    public void startEnemyTask() {
        for (Enemy enemy : enemies) {
            EnemyTask enemy_Task = new EnemyTask(enemy, this);
            enemyTasks.add(enemy_Task);
            enemyExecutor.submit(enemy_Task);
        }
    }

    /**
     * Sets the game controller for handling game interactions.
     * @param controller The GameController object to set.
     */
    public void setController(GameController controller) {
        this.controller = controller;
    }

    /**
     * Sets the game map for the game world.
     * @param map The GameMap object to set.
     */
    public void setMap(GameMap map) {
        this.map = map;
    }

    /**
     * Gets the game controller for handling game interactions.
     * @return The GameController object.
     */
    public GameController getController() {
        return controller;
    }

    /**
     * Gets the executor service used for running enemy tasks.
     * @return The ExecutorService object for enemy tasks.
     */
    public ExecutorService getEnemyExecutor() {
        return enemyExecutor;
    }

    /**
     * Shuts down the game world, stopping all enemy tasks and cleaning up resources.
     * This method ensures that all enemy tasks are stopped gracefully and the executor service is shut down.
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
     * Adds an observer to the game world.
     * Observers will be notified when the game state is updated.
     * @param observer The GameObserver to add.
     */
    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    /**
     * Notifies all observers that the game state has been updated.
     * This method is called to inform observers about changes in the game world.
     */
    public void notifyObservers() {
        for (GameObserver observer : observers) {
            observer.onGameUpdated();
        }
    }

    /**
     * Sets the list of items in the game world.
     * This method is used to update the items in the game world.
     * @param items The list of GameItem objects to set.
     */
    public void setItems(List<GameItem> items) {
        this.items = items;
    }

    /**
     * Gets a lock for a specific position in the game world.
     * This method ensures that only one thread can access the specified position at a time.
     * @param pos The position for which to get the lock.
     * @return A ReentrantLock object for the specified position.
     */
    public static ReentrantLock getMapLock(Position pos) {
        return lockMap.computeIfAbsent(pos, p -> new ReentrantLock());
    }

    /**
     * Gets the atomic boolean indicating if the game is currently running.
     * This flag is used to control the game loop and determine if the game should continue running.
     * @return The AtomicBoolean indicating if the game is running.
     */
    public AtomicBoolean getIsGameRunning() {
        return isGameRunning;
    }

    /**
     * Restores the game state from a memento.
     * This method updates the game world with the state stored in the provided GameMemento.
     * It replaces the current player, enemies, items, and map with those from the memento.
     * @param memento The GameMemento containing the saved game state.
     */
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

    /**
     * Creates a potion at the specified position and adds it to the game world and map.
     * @param pos The position where the potion should be created.
     */
    private void createPotion(Position pos) {
        Potion potion = new Potion(pos, false, 50, 10);
        items.add(potion);
        map.addToGrid(pos, potion);
    }

    /**
     * Creates a power potion at the specified position and adds it to the game world and map.
     * @param pos The position where the power potion should be created.
     */
    private void createPowerPotion(Position pos) {
        PowerPotion powerPotion = new PowerPotion(pos, false, 5, 1);
        items.add(powerPotion);
        map.addToGrid(pos, powerPotion);
    }

    /**
     * Creates a wall at the specified position and adds it to the game world and map.
     * @param pos The position where the wall should be created.
     */
    private void createWall(Position pos) {
        Wall wall = new Wall(pos, true);
        items.add(wall);
        map.addToGrid(pos, wall);
    }

    /**
     * Creates a player character and adds it to the game world.
     * @param playerType   The type of player character to create.
     * @param playerName   The name of the player character.
     * @param attributes   A map of attributes for the player character.
     * @param element      The magic element associated with the player character.
     * @param decorators   A list of decorators to apply to the player character.
     */
    private void createPlayer(int playerType, String playerName, Map<String, Integer> attributes, MagicElement element, List<String> decorators)  {
        Position pos = map.getRandomEmptyPosition();
        PlayerCharacter player = playerFactory.createPlayerFactory(playerType, playerName, pos, attributes, element);
        player = applyDecorators(player, decorators);
        map.addToGrid(pos, player);
        players.add(player);
        LogManager.addLog("Game started with player type: " + player.getDisplaySymbol() + " and name: " + playerName);
    }

    /**
     * Applies decorators to the base player character based on the provided names.
     * @param base   The base player character to decorate.
     * @param names  A list of decorator names to apply.
     * @return The decorated player character.
     */
    private PlayerCharacter applyDecorators(PlayerCharacter base, List<String> names) {
        PlayerCharacter res = base;
        RegenerationDecorator regenRef = null;

        for (String name : names) {
            switch (name.toLowerCase()) {
                case "boost":
                    res = new BoostedAttackDecorator(res, 5);
                    break;
                case "regen":
                    regenRef = new RegenerationDecorator(res, 5, 3);
                    res = regenRef;
                    break;
                case "magicamplifier":
                    res = new MagicAmplifierDecorator(res);
                    break;
            }
        }
        if (regenRef != null) {
            final PlayerCharacter finalPlayer = res;
            new Timer(true).scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    finalPlayer.update();
                }
            }, 0, 1000);

        }
        return res;
    }

    /**
     * Populates the game map with enemies, walls, and items.
     * Randomly places entities based on predefined probabilities.
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
     * Randomly decorates an enemy with a decorator after a delay.
     * The decorator can be either a VampireEnemyDecorator or a TeleportingEnemyDecorator.
     * @param enemy The enemy to decorate.
     */
    private void decorateEnemyRandomly(Enemy enemy) {
        int delayMillis = 1000 * (1 + new Random().nextInt(10));

        new Timer(true).schedule(new TimerTask() {
            @Override
            public void run() {
                Enemy decorated = createRandomEnemyDecorator(enemy);
                int index = enemies.indexOf(enemy);
                if (index != -1) {
                    enemies.set(index, decorated);
                    Position pos = enemy.getPosition();
                    GameMap map = GameMap.getInstance();
                    map.removeFromGrid(pos, enemy);
                    map.addToGrid(pos, decorated);
                    LogManager.addLog("Enemy got a random decorator: " + decorated.getClass().getSimpleName());
                }
            }
        }, delayMillis);
    }

    /**
     * Creates a random enemy decorator for the given enemy.
     * Randomly chooses between VampireEnemyDecorator and TeleportingEnemyDecorator.
     * @param enemy The base enemy to decorate.
     * @return A decorated enemy instance.
     */
    private Enemy createRandomEnemyDecorator(Enemy enemy) {
        int pick = new Random().nextInt(3);
        return switch (pick) {
            case 0 -> new VampireEnemyDecorator(enemy);
            case 1 -> new TeleportingEnemyDecorator(enemy);
            case 2 -> new ExplodingEnemyDecorator(enemy,getPlayer()); // Default case, no special decorator
            default -> enemy;
        };
    }

    // --- Fields ---
    private static final ConcurrentHashMap<Position, ReentrantLock> lockMap = new java.util.concurrent.ConcurrentHashMap<>();
    private GameMap map;
    private List<PlayerCharacter> players;
    private List<Enemy> enemies;
    private List<GameItem> items;
    private ExecutorService enemyExecutor;
    private List<EnemyTask> enemyTasks;
    private List<GameObserver> observers = new ArrayList<>();
    private GameController controller;
    private final AtomicBoolean isGameRunning = new AtomicBoolean(true);
    private final PlayerFactory playerFactory = new PlayerFactory();
    private final EnemyFactory enemyFactory = new EnemyFactory();


}