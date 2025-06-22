package game.map;
import game.core.GameEntity;
import java.util.*;

/**
 * Represents a square grid-based game map that holds game entities at specific positions.
 * Each grid cell (Position) may contain multiple entities.
 */
public class GameMap {

    /**
     * Returns a singleton instance of GameMap with the specified size.
     * If an instance already exists, it returns that instance.
     * @param size the size of the game map (size x size)
     * @return the singleton instance of GameMap
     */
    public static GameMap getInstance(int size) {
        if (instance == null) {
            instance = new GameMap(size);
        }
        return instance;
    }

    /**
     * Returns the singleton instance of GameMap.
     * If the instance has not been initialized, it throws an IllegalStateException.
     * @return the singleton instance of GameMap
     * @throws IllegalStateException if the instance has not been initialized
     */
    public static GameMap getInstance() {
        if (instance == null) {
            throw new IllegalStateException("GameMap has not been initialized.");
        }
        return instance;
    }

    /**
     * Returns the size of the game map.
     * @return the size of the game map
     */
    public int getSize() {
        return size;
    }

    /**
     * Creates a deep copy of the current game map.
     * Each entity in the grid is also deep copied to ensure independence from the original map.
     * @return a new Map<Position, List<GameEntity>> representing the copied grid
     */
    public Map<Position, List<GameEntity>> copyGrid() {
        Map<Position, List<GameEntity>> copiedGrid = new HashMap<>();

        for (Map.Entry<Position, List<GameEntity>> entry : this.grid.entrySet()) {
            Position originalPos = entry.getKey();
            List<GameEntity> originalEntities = entry.getValue();

            List<GameEntity> copiedEntities = new ArrayList<>();
            for (GameEntity entity : originalEntities) {
                copiedEntities.add(entity.deepCopy());
            }

            copiedGrid.put(originalPos, copiedEntities);
        }

        return copiedGrid;
    }

    /**
     * Checks if the specified position is empty (i.e., contains no entities).
     * @param pos the position to check
     * @return true if the position is empty, false otherwise
     */
    public boolean isEmpty(Position pos) {
        List<GameEntity> entities = grid.get(pos);
        return entities == null || entities.isEmpty();
    }

    /**
     * Returns a random empty position on the game map.
     * It tries up to size * size times to find an empty position.
     * @return a random empty Position
     * @throws RuntimeException if no empty position is found after max tries
     */
    public Position getRandomEmptyPosition() {
        Random random = new Random();
        int maxTries = size * size;
        for (int i = 0; i < maxTries; i++) {
            int randX = random.nextInt(size);
            int randY = random.nextInt(size);
            Position pos = new Position(randX, randY);
            if (isEmpty(pos)) {
                return pos;
            }
        }
        throw new RuntimeException("No empty position found on the board");
    }

    /**
     * Adds a game entity to the grid at the specified position.
     * If the position is empty, it creates a new list for that position.
     * If the entity is already present, it does not add it again.
     * @param pos the position where the entity should be added
     * @param gameEntity the game entity to add
     * @return true if the entity was added, false if it was already present
     */
    public boolean addToGrid(Position pos, GameEntity gameEntity) {
        List<GameEntity> entities = grid.get(pos);
        if (entities == null) {
            entities = new ArrayList<>();
            entities.add(gameEntity);
            grid.put(pos, entities);
            return true;
        }
        if (!entities.contains(gameEntity)) {
            entities.add(gameEntity);
            return true;
        }
        return false;
    }

    /**
     * Removes a game entity from the grid at the specified position.
     * If the position is empty or the entity is not present, it returns false.
     * If the last entity at that position is removed, it also removes the position from the grid.
     * @param pos the position where the entity should be removed
     * @param gameEntity the game entity to remove
     * @return true if the entity was removed, false if it was not present
     */
    public boolean removeFromGrid(Position pos, GameEntity gameEntity) {
        List<GameEntity> entities = grid.get(pos);
        if (entities == null || !entities.contains(gameEntity)) {
            return false;
        }
        entities.remove(gameEntity);
        if (entities.isEmpty()) {
            grid.remove(pos);
        }
        return true;
    }

    /**
     * Retrieves the list of game entities at the specified position.
     * If no entities are present at that position, it returns an empty list.
     * @param pos the position to check
     * @return a list of GameEntity objects at the specified position
     */
    public List<GameEntity> getEntitiesAt(Position pos) {
        return grid.getOrDefault(pos, new ArrayList<>());
    }

    /**
     * Sets the internal grid representation of the game map.
     * This method is typically used for testing or resetting the map.
     * @param grid a Map<Position, List<GameEntity>> representing the new grid
     */
    public void setGrid(Map<Position, List<GameEntity>> grid) {
        this.grid = grid;
    }

    /**
     * Checks if the specified position is within the bounds of the game map.
     * The position is considered within bounds if its row and column are non-negative
     * and less than the size of the map.
     * @param newPos the position to check
     * @return true if the position is within bounds, false otherwise
     */
    public boolean isWithinBounds(Position newPos) {
        return newPos.getCol() >= 0 && newPos.getCol() < size &&
                newPos.getRow() >= 0 && newPos.getRow() < size;
    }

    /**
     * Private constructor to enforce singleton pattern.
     * Initializes the game map with the specified size.
     * @param size the size of the game map (size x size)
     */
    private GameMap(int size) {
        this.size = size;
    }

    // --- Fields ---
    private static GameMap instance = null;
    private Map<Position, List<GameEntity>> grid = new HashMap<>();
    private final int size;


    public Map<Position, List<GameEntity>> getGrid() {
        return Collections.unmodifiableMap(grid);
    }
}