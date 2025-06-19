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

    public GameMap(int size) {
        this.size = size;
    }

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

    public boolean isEmpty(Position pos) {
        List<GameEntity> entities = grid.get(pos);
        return entities == null || entities.isEmpty();
    }

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

    public List<GameEntity> getEntitiesAt(Position pos) {
        return grid.getOrDefault(pos, new ArrayList<>());
    }

    public void setGrid(Map<Position, List<GameEntity>> grid) {
        this.grid = grid;
    }

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