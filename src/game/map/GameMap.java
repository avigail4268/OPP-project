package game.map;
import game.core.GameEntity;
import java.util.*;

/**
 * Represents a square grid-based game map that holds game entities at specific positions.
 * Each grid cell (Position) may contain multiple entities.
 */
public class GameMap {
    public static GameMap getInstance(int size) {
        if (instance == null) {
            instance = new GameMap(size);
        }
        return instance;
    }
    /**
     * Creates a new game map of given size.
     * @param size the width and height of the square map
     */
    public GameMap(int size) {
        this.size = size;
    }

    /**
     * Returns the size (width/height) of the map.
     * @return map size
     */
    public int getSize() {
        return size;
    }

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
     * Checks whether the given position contains no entities.
     * @param pos position to check
     * @return true if the position is empty, false otherwise
     */
    public boolean isEmpty(Position pos) {
        List<GameEntity> entities = grid.get(pos);
        return entities == null || entities.isEmpty();
    }

    /**
     * Returns a random empty position on the map.
     * Attempts up to size * size times.
     * @return a random empty Position
     * @throws RuntimeException if no empty position is found
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
     * Adds a game entity to a position on the map.
     * Initializes the list if necessary.
     * @param pos the position to add to
     * @param gameEntity the entity to place
     * @return true if the entity was added, false if already present
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
     * Removes a game entity from the given position.
     * Removes the position from the map if the entity list becomes empty.
     * @param pos the position to remove from
     * @param gameEntity the entity to remove
     * @return true if the entity was removed, false otherwise
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
     * Gets all entities at a specific position.
     * Returns an empty list if the position is unoccupied.
     * @param pos the position to query
     * @return a list of entities at the position
     */
    public List<GameEntity> getEntitiesAt(Position pos) {
        return grid.getOrDefault(pos, new ArrayList<>());
    }

    public Map<Position, List<GameEntity>> getGrid() {
        return grid;
    }
    public void setGrid(Map<Position, List<GameEntity>> grid) {
        this.grid = grid;
    }

    /**
     * Checks whether a position is within the map bounds.
     * @param newPos the position to check
     * @return true if within bounds, false otherwise
     */
    public boolean isWithinBounds(Position newPos) {
        return newPos.getCol() >= 0 && newPos.getCol() < size &&
                newPos.getRow() >= 0 && newPos.getRow() < size;
    }


    // --- Fields ---
    private static GameMap instance = null;
    /**
     * Internal representation of the map: maps a position to a list of entities at that position.
     */
    private Map<Position, List<GameEntity>> grid = new HashMap<>();

    /**
     * Size of the grid (map is size x size).
     */
    private final int size;
}