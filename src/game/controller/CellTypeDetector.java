package game.controller;
import game.core.GameEntity;
import game.characters.Enemy;
import game.characters.PlayerCharacter;
import game.items.GameItem;
import game.items.Wall;

import java.util.List;

/**
 * Utility class to detect the presence and types of GameEntity objects in a cell.
 * Provides methods to check whether a cell contains players, enemies, walls, or items,
 * and to retrieve the first instance of a specified type.
 */
public class CellTypeDetector {

    /**
     * Checks whether the given list of entities contains at least one instance of the specified type.
     * @param entities List of entities to check
     * @param clazz    Class type to look for
     * @param <T>      Type parameter extending GameEntity
     * @return true if an instance of clazz exists in entities; false otherwise
     */
    public static <T extends GameEntity> boolean containsType(List<GameEntity> entities, Class<T> clazz) {
        return entities.stream().anyMatch(clazz::isInstance);
    }

    /**
     * Returns the first entity in the list that matches the specified type.
     * @param entities List of entities to search
     * @param clazz    Class type to retrieve
     * @param <T>      Type parameter extending GameEntity
     * @return First entity of the given type, or null if none found
     */
    public static <T extends GameEntity> T getFirstOfType(List<GameEntity> entities, Class<T> clazz) {
        return entities.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .findFirst()
                .orElse(null);
    }

    /**
     * Checks if the given list of entities contains a PlayerCharacter.
     * @param entities List of entities to check
     * @return true if a player is present; false otherwise
     */
    public static boolean hasPlayer(List<GameEntity> entities) {
        return containsType(entities, PlayerCharacter.class);
    }

    /**
     * Checks if the given list of entities contains an Enemy.
     * @param entities List of entities to check
     * @return true if an enemy is present; false otherwise
     */
    public static boolean hasEnemy(List<GameEntity> entities) {
        return containsType(entities, Enemy.class);
    }

    /**
     * Checks if the given list of entities contains a GameItem.
     * @param entities List of entities to check
     * @return true if an item is present; false otherwise
     */
    public static boolean hasItem(List<GameEntity> entities) {
        return containsType(entities, GameItem.class);
    }

    /**
     * Checks if the given list of entities contains a Wall.
     * @param entities List of entities to check
     * @return true if a wall is present; false otherwise
     */
    public static boolean hasWall(List<GameEntity> entities) {
        return containsType(entities, Wall.class);
    }

    /**
     * Retrieves the first Enemy found in the entity list.
     * @param entities List of entities to search
     * @return First Enemy found, or null if none
     */
    public static Enemy getFirstEnemy(List<GameEntity> entities) {
        return getFirstOfType(entities, Enemy.class);
    }

    /**
     * Retrieves the first  GameItem found in the entity list.
     * @param entities List of entities to search
     * @return First GameItem found, or null if none
     */
    public static GameItem getFirstItem(List<GameEntity> entities) {
        return getFirstOfType(entities, GameItem.class);
    }

    /**
     * Retrieves the first PlayerCharacter found in the entity list.
     * @param entities List of entities to search
     * @return First PlayerCharacter found, or null if none
     */
    public static PlayerCharacter getFirstPlayer(List<GameEntity> entities) {
        return getFirstOfType(entities, PlayerCharacter.class);
    }
}
