package game.controller;
import game.core.GameEntity;
import game.characters.Enemy;
import game.characters.PlayerCharacter;
import game.items.GameItem;
import game.items.Wall;
import java.util.List;

public class CellTypeDetector {

    public static <T extends GameEntity> boolean containsType(List<GameEntity> entities, Class<T> clazz) {
        return entities.stream().anyMatch(clazz::isInstance);
    }

    public static <T extends GameEntity> T getFirstOfType(List<GameEntity> entities, Class<T> clazz) {
        return entities.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .findFirst()
                .orElse(null);
    }

    public static boolean hasPlayer(List<GameEntity> entities) {
        return containsType(entities, PlayerCharacter.class);
    }

    public static boolean hasEnemy(List<GameEntity> entities) {
        return containsType(entities, Enemy.class);
    }

    public static boolean hasItem(List<GameEntity> entities) {
        return containsType(entities, GameItem.class);
    }

    public static boolean hasWall(List<GameEntity> entities) {
        return containsType(entities, Wall.class);
    }

    public static Enemy getFirstEnemy(List<GameEntity> entities) {
        return getFirstOfType(entities, Enemy.class);
    }

    public static GameItem getFirstItem(List<GameEntity> entities) {
        return getFirstOfType(entities, GameItem.class);
    }
    public static PlayerCharacter getFirstPlayer(List<GameEntity> entities) {
        return getFirstOfType(entities, PlayerCharacter.class);
    }
}