package game.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import game.characters.*;
import game.combat.CombatSystem;
import game.core.GameEntity;
import game.items.*;
import game.map.GameMap;
import game.map.Position;

/**
 * Represents the main game world where the player, enemies, and items interact.
 * Handles game initialization, player actions, movement, and the game loop.
 */
public class GameWorld {

    /**
     * Constructs a new GameWorld and initializes the game.
     */
    public GameWorld() {
        gameInitialization();
    }

    /**
     * Starts the main game loop.
     * The game ends if the player dies, wins by reaching 500 treasure points, or defeats all enemies.
     */
    public void startGame() {
        while (true) {
            PlayerCharacter currentPlayer = players.get(0);
            //isVisible checks if the player is in range of other players, enemies, or items
            isVisible(currentPlayer);
            printBoard();
            //chooseMovement checks if the player can move to the new position
            Position newPosition = chooseMovement(currentPlayer.getPosition(), currentPlayer);
            System.out.println("Current health: " + currentPlayer.getHealth());
            System.out.println("Treasure points: " + currentPlayer.getTreasurePoints());

            if (newPosition != null) {
                Position lastPosition = currentPlayer.getPosition();
                currentPlayer.setPosition(newPosition);
                //update the map and board
                map.removeFromGrid(lastPosition, currentPlayer);
                map.addToGrid(newPosition, currentPlayer);
                board[lastPosition.getRow()][lastPosition.getCol()] = "_ ";
            }

            if (currentPlayer.isDead()) {
                // if the player is dead remove from the game and end it
                players.remove(currentPlayer);
                map.removeFromGrid(currentPlayer.getPosition(), currentPlayer);
                System.out.println("Player: " + currentPlayer.getName() + " is dead, GAME OVER!");
                break;
            }
            if (currentPlayer.getTreasurePoints() >= 500) {
                // if the player has more than 500 points he wins and end the game
                System.out.println("Player: " + currentPlayer.getName() + " achieved more than 500 points and WON THE GAME!");
                break;
            }
            if (enemies.isEmpty()) {
                // if the player has killed all the enemies he wins and end the game
                System.out.println("Player: " + currentPlayer.getName() + " you WON THE GAME! All the enemies are dead!");
                break;
            }
        }
    }

    /**
     * Initializes the game by creating the map, initializing the player, and populating the game world.
     */
    private void gameInitialization() {
        createGameMap();
        initPlayers();
        populateGameMap();
    }

    /**
     * Prints the current game board to the console.
     */
    private void printBoard() {
        System.out.println("===== Game Board =====");
        for (String[] row : board) {
            for (String cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

    /**
     * Creates the game map with user input for size.
     * Ensures the map size is at least 10x10.
     */
    private void createGameMap() {
        System.out.println("Enter map size (minimum size is 10x10):");
        Scanner scanner = new Scanner(System.in);
        int size = scanner.nextInt();
        while (size < 10) {
            System.out.println("Invalid input! Please enter a number greater than 10.");
            size = scanner.nextInt();
        }
        board = new String[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = "_ ";
            }
        }
        this.map = new GameMap(size);
    }

    /**
     * Allows the player to choose an action: move or use an item.
     *
     * @param lastPosition The current position of the player.
     * @param currentPlayer The current player.
     * @return The new position after the action, or null if the move is invalid.
     */
    private Position chooseMovement(Position lastPosition, PlayerCharacter currentPlayer) {
        System.out.println("Choose an action:");
        System.out.println("1. Move Up\n2. Move Down\n3. Move Left\n4. Move Right\n5. Use Potion\n6. Use Power Potion");
        Scanner scanner = new Scanner(System.in);
        int action = scanner.nextInt();
        while (action < 1 || action > 6) {
            System.out.println("Invalid input. Please choose a valid action:");
            action = scanner.nextInt();
        }
        // Determine the new position based on the action
        Position newPos = lastPosition;
        int row = lastPosition.getRow(), col = lastPosition.getCol();
        switch (action) {
            case 1 -> newPos = new Position(row - 1, col);
            case 2 -> newPos = new Position(row + 1, col);
            case 3 -> newPos = new Position(row, col - 1);
            case 4 -> newPos = new Position(row, col + 1);
            case 5 -> {
                if (currentPlayer.usePotion()) {
                    System.out.println("Used Health Potion.");
                } else {
                    System.out.println("No potion found in the inventory.");
                }
                return newPos;
            }
            case 6 -> {
                if (currentPlayer.usePowerPotion()) {
                    System.out.println("Used Power Potion.");
                } else {
                    System.out.println("No power potion found in the inventory.");
                }
                return newPos;
            }
        }
        // Check if the new position is valid by using isAvailable function
        return isAvailable(newPos, currentPlayer);
    }

    /**
     * Checks if the desired move position is valid.
     *
     * @param newPos The new desired position.
     * @param currentPlayer The player attempting the move.
     * @return The valid position or null if invalid.
     */
    private Position isAvailable(Position newPos, PlayerCharacter currentPlayer) {
        // Check if the new position is within the map bounds and not blocked
        if (!isInMapBounds(newPos)) {
            // If the new position is out of bounds, print a message and return null
            System.out.println("Out of game bounds! - Invalid move");
            return null;
        } else if (map.isEmpty(newPos)) {
            // If the new position is empty
            return newPos;
        } else {
            // If the new position is occupied by an entity, check if it's blocked or interactable
            boolean move = checkPosition(newPos, currentPlayer);
            if (move) {
                return newPos;
            } else {
                return null;
            }
        }
    }

    /**
     * Checks if a position is within the map bounds.
     *
     * @param pos The position to check.
     * @return True if within bounds, false otherwise.
     */
    private boolean isInMapBounds(Position pos) {
        int row = pos.getRow();
        int col = pos.getCol();
        int size = map.getSize();
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    /**
     * Checks the entities at a given position and handles interactions (combat, collection).
     *
     * @param newPos The position to check.
     * @param currentPlayer The player attempting the interaction.
     * @return True if the player can move into the position, false otherwise.
     */
    private boolean checkPosition(Position newPos, PlayerCharacter currentPlayer) {
        List<GameEntity> entities = map.getEntityInPosition(newPos);
        for (GameEntity entity : entities) {
            if (entity instanceof PlayerCharacter) {
                //only one player at the moment
                return false;
            } else if (entity instanceof Enemy) {
                // If the entity is an enemy, initiate combat
                Enemy enemy = findEnemy(entity);
                CombatSystem combatSystem = new CombatSystem();
                combatSystem.resolveCombat(currentPlayer, enemy);

                if (currentPlayer.isDead()) {
                    // If the player is dead, return false
                    return false;
                } else if (enemy != null && enemy.isDead()) {
                    // If the enemy is dead, remove it from the map and add treasure
                    Treasure replacement = enemy.defeat();
                    if (map.removeFromGrid(newPos, enemy)) {
                        enemies.remove(enemy);
                        items.add(replacement);
                        map.addToGrid(replacement.getPosition(), replacement);
                    }
                    return true;
                } else {
                    return false;
                }
            } else {
                if (isBlocked(entity.getPosition())) {
                    System.out.println("There is a Wall! Choose another spot!");
                    Position retryPos = chooseMovement(currentPlayer.getPosition(), currentPlayer);
                    if (retryPos != null) currentPlayer.setPosition(retryPos);
                    return false;
                } else if (entity instanceof Interactable item) {
                    item.collect(currentPlayer);
                    if (map.removeFromGrid(newPos, entity)) {
                        items.remove(item);
                    }
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    /**
     * Finds an enemy based on a game entity reference.
     *
     * @param entity The game entity.
     * @return The enemy if found, otherwise null.
     */
    private Enemy findEnemy(GameEntity entity) {
        for (Enemy enemy : enemies) {
            if (enemy.equals(entity)) return enemy;
        }
        return null;
    }

    /**
     * Initializes the player list and prompts user to select a player character.
     */
    private void initPlayers() {
        this.players = new ArrayList<>();
        playersTypes();
    }

    /**
     * Prompts the user to select the player character type.
     */
    private void playersTypes() {
        System.out.println("Select player character: 1.Warrior, 2.Mage, 3.Archer");
        Scanner scanner = new Scanner(System.in);
        int playerType = scanner.nextInt();
        while (playerType < 1 || playerType > 3) {
            System.out.println("Wrong input! please choose one of the following options: 1.Warrior, 2.Mage, 3.Archer");
            playerType = scanner.nextInt();
        }
        System.out.println("Enter player name:");
        String playerName = scanner.next();
        //send the player type and name to createPlayer function
        createPlayer(playerType, playerName);
    }

    /**
     * Creates a player based on the selected type and adds them to the game world.
     *
     * @param playerType The selected type (1 = Warrior, 2 = Mage, 3 = Archer).
     * @param playerName The chosen player name.
     */
    private void createPlayer (int playerType, String playerName) {
        // Create a new player character based on the selected type
        PlayerCharacter player;
        try {
            // Get a random empty position on the map
            Position position = this.map.getRandomEmptyPosition();
            if (playerType == 1) {
                player = new Warrior(playerName, position, 100);
            } else if (playerType == 2) {
                player = new Mage(playerName, position, 100);
            } else {
                player = new Archer(playerName, position, 100);
            }
            map.addToGrid(position, player);
            players.add(player);
        } catch (RuntimeException e) {
            // If no empty position is found, print a message and prompt for player type again
            System.out.println("No empty position found on the board. Please try again.");
            playersTypes();
        }
    }

    /**
     * Populates the game map with enemies, items, and walls.
     */
    private void populateGameMap() {
        // Populate the game map with items, enemies, and walls
        this.items = new ArrayList<>();
        this.enemies = new ArrayList<>();
        for (int i = 0; i < map.getSize(); i++) {
            for (int j = 0; j < map.getSize(); j++) {
                Position pos = new Position(i, j);
                if (!map.isEmpty(pos)) continue;
                double random = Math.random();
                //40% for empty space
                if (random <= 0.4) continue;
                //30% for enemy
                else if (random <= 0.7) createEnemy(pos);
                //10% for wall
                else if (random < 0.8) createWall(pos);
                //20% for potion: 15% for health potion
                else if (random < 0.95) createPotion(pos);
                //5% for power potion
                else createPowerPotion(pos);
            }
        }
    }

    /**
     * Updates the visibility of nearby entities around the player.
     *
     * @param currentPlayer The player.
     */
    private void isVisible(PlayerCharacter currentPlayer) {
        System.out.println(currentPlayer + "!");
        System.out.println("Look around you: ");
        // Check visibility for players, enemies, and items by using the checkArrayList method
        checkArrayList(players, currentPlayer);
        checkArrayList(enemies, currentPlayer);
        checkArrayList(items, currentPlayer);
    }

    /**
     * Updates the visibility for a list of entities relative to the player.
     *
     * @param entities The list of entities.
     * @param currentPlayer The player.
     */
    private void checkArrayList(List<? extends GameEntity> entities, PlayerCharacter currentPlayer) {
        final String RESET = "\u001B[0m";
        // Iterate through the list of entities and check their visibility
        for (GameEntity entity : entities) {
            Position entityPosition = entity.getPosition();
            int row = entity.getPosition().getRow();
            int col = entity.getPosition().getCol();

            // Check if the entity is within a distance of 2 from the player
            if (entityPosition.distanceTo(currentPlayer.getPosition()) <= 2 && !entity.equals(currentPlayer)) {
                entity.setVisible(true);
                board[row][col] = entity.getColorCode() + entity.getDisplaySymbol().substring(0, 2) + RESET;
            }
            //If the entity is not in the rage of 2, set it to invisible
            else if (entity.getVisible() && entityPosition.distanceTo(currentPlayer.getPosition()) > 2) {
                entity.setVisible(false);
                board[row][col] = "_ ";
            }
            // If the entity is the current player, set it to visible
            if (entity.equals(currentPlayer)) {
                entity.setVisible(true);
                board[row][col] = entity.getColorCode() + entity.getDisplaySymbol().substring(0, 2) + RESET;
            }
        }
    }

    /**
     * Creates a power potion at a random position.
     *
     * @param pos The position.
     */
    private void createPowerPotion(Position pos) {
        PowerPotion powerPotion = new PowerPotion(pos, false, 5, 1);
        items.add(powerPotion);
        map.addToGrid(pos, powerPotion);
    }

    /**
     * Creates a health potion at a random position.
     *
     * @param pos The position.
     */
    private void createPotion(Position pos) {
        Potion potion = new Potion(pos, false, 50, 10);
        items.add(potion);
        map.addToGrid(pos, potion);
    }

    /**
     * Creates a wall at a random position.
     *
     * @param pos The position.
     */
    private void createWall(Position pos) {
        Wall wall = new Wall(pos, true);
        items.add(wall);
        map.addToGrid(pos, wall);
    }

    /**
     * Checks if a given position is blocked by a game item.
     *
     * @param pos The position to check.
     * @return True if blocked, false otherwise.
     */
    private boolean isBlocked(Position pos) {
        List<GameEntity> entities = map.getEntityInPosition(pos);
        for (GameEntity entity : entities) {
            if (entity instanceof GameItem item && item.isBlocksMovement()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a random enemy at a random position.
     *
     * @param pos The position.
     */
    public void createEnemy(Position pos) {
        double random = Math.random();
        Enemy enemy;
        // Randomly select an enemy type based on the random number
        if (random <= 1.0 / 3.0) {
            enemy = new Dragon(pos, 50);
        } else if (random <= 2.0 / 3.0) {
            enemy = new Orc(pos, 50);
        } else {
            enemy = new Goblin(pos, 50);
        }
        // Add the enemy to the game world and map
        enemies.add(enemy);
        map.addToGrid(pos, enemy);
    }

    public List<PlayerCharacter> getPlayers() {
        return players;
    }
    public PlayerCharacter getCurrentPlayer() {
        return players.get(0);
    }
    public List<GameItem> getItems() {
        return items;
    }
    public List<Enemy> getEnemies() {
        return enemies;
    }
    public GameMap getMap() {
        return map;
    }
    // --- Fields ---

    /** The list of players in the game. */
    private List<PlayerCharacter> players;
    /** The list of enemies in the game. */
    private List<Enemy> enemies;
    /** The list of items in the game. */
    private List<GameItem> items;
    /** The game map object. */
    private GameMap map;
    /** The 2D board representation. */
    private String[][] board;
}
