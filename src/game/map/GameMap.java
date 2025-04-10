package game.map;
import game.core.GameEntity;

import java.util.*;

public class GameMap {
    private Map < Position, List<GameEntity> > grid = new HashMap<>();
    private int size;

    public GameMap(int size){

    }
    public int getSize(){
        return size;
    }

    public boolean isEmpty(Position pos){
        List<GameEntity> entities = grid.get(pos);
        if (entities == null || entities.isEmpty()) {
            return true;
        }
        return false;
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
            throw new RuntimeException("No empty position found on the board");
    }

    public boolean addToGrid(Position pos, GameEntity gameEntity){
        // TODO check when returns false
        List<GameEntity> entities = grid.get(pos);
        if (entities == null) {
            entities = new ArrayList<>();
        }
        if (!entities.contains(gameEntity)) {
            entities.add(gameEntity);
        }
        grid.put(pos, entities);
        return true;
    }
}
