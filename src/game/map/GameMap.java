package game.map;
import game.core.GameEntity;
import java.util.*;

public class GameMap {

    public GameMap(int size){
        this.size = size;
    }
    public int getSize(){
        return size;
    }
    public boolean isEmpty(Position pos){
        List<GameEntity> entities = grid.get(pos);
        return entities == null || entities.isEmpty();
    }
    public Position getRandomEmptyPosition(){
        Random random = new Random();
        int maxTries = size * size;
            for (int i = 0; i < maxTries; i++) {
                int randX = random.nextInt(size);
                int randY = random.nextInt(size);
                Position pos = new Position(randX, randY);
                if(isEmpty(pos)){
                    return pos;
                }
            }
            throw new RuntimeException ("No empty position found on the board");
    }
    public boolean addToGrid(Position pos, GameEntity gameEntity){
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
    public boolean removeFromGrid(Position pos, GameEntity gameEntity){
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
    public List<GameEntity> getEntityInPosition(Position pos) {
        return grid.get(pos);
    }

    private Map < Position, List<GameEntity> > grid = new HashMap<>();
    private final int size;
}
