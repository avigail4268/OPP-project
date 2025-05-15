package game.map;

import game.core.GameEntity;
import java.util.*;

/**
 * Represents a game map that holds entities at specific positions.
 * The map is a grid where each position can hold multiple entities.
 */
public class GameMap {

    /**
     * Creates a new game map with the specified size.
     *
     * @param size the size of the map (the map will be a size x size grid)
     */
    public GameMap(int size) {
        this.size = size;
    }

    /**
     * Returns the size of the game map.
     *
     * @return the size of the map
     */
    public int getSize() {
        return size;
    }

    /**
     * Checks if the specified position is empty (i.e., no entities are present).
     *
     * @param pos the position to check
     * @return true if the position is empty, false otherwise
     */
    public boolean isEmpty(Position pos) {
        List<GameEntity> entities = grid.get(pos);
        return entities == null || entities.isEmpty();
    }

    /**
     * Returns a random empty position on the map.
     * It attempts to find an empty position by trying up to size * size times.
     *
     * @return a random empty position
     * @throws RuntimeException if no empty position is found after size * size attempts
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
     * Adds a game entity to the specified position on the map.
     * If the position already contains entities, the entity will be added to the list.
     *
     * @param pos the position to add the entity to
     * @param gameEntity the entity to add
     * @return true if the entity was successfully added, false otherwise
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
            grid.put(pos, entities);
            return true;
        }
        return false;
    }

    /**
     * Removes a game entity from the specified position on the map.
     * If the position contains no entities or does not contain the specified entity,
     * the method returns false.
     *
     * @param pos the position to remove the entity from
     * @param gameEntity the entity to remove
     * @return true if the entity was successfully removed, false otherwise
     */
    public boolean removeFromGrid(Position pos, GameEntity gameEntity) {
        List<GameEntity> entities = grid.get(pos);
        if (entities == null) {
            return false;
        }
        if (!entities.contains(gameEntity)) {
            return false;
        }
        entities.remove(gameEntity);
        if (entities.isEmpty()) {
            grid.remove(pos);
        }
        return true;
    }

    /**
     * Returns a list of all entities at the specified position on the map.
     *
     * @param pos the position to retrieve entities from
     * @return a list of entities at the specified position, or null if the position is empty
     */
    public List<GameEntity> getEntitiesAt(Position pos) {
        return grid.getOrDefault(pos, new ArrayList<>());
    }

    // Private field storing the grid of the map
    private Map<Position, List<GameEntity>> grid = new HashMap<>();

    // Size of the map (width and height of the grid)
    private final int size;

    public boolean isWithinBounds(Position newPos) {
        return newPos.getCol() >= 0 && newPos.getCol() < size && newPos.getRow() >= 0 && newPos.getRow() < size;
    }
}
