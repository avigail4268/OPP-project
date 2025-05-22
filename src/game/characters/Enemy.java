
package game.characters;
import game.items.Treasure;
import game.map.Position;

import java.util.Random;

/**
 * Enemy represents a character that can be defeated by a player.
 * It extends the AbstractCharacter class and adds loot functionality.
 * When defeated, it may drop a treasure for the player to collect.
 */
public abstract class Enemy extends AbstractCharacter {

    /**
     * Constructs a new Enemy with the given position and health.
     * A random loot value between 100 and 300 is assigned to the enemy.
     * @param position the position of the enemy
     * @param health the health of the enemy
     */
    public Enemy(Position position, int health) {
        super(position, health);
        Random r = new Random();
        this.loot = r.nextInt(100, 300); // Random loot between 100 and 300
    }

    /**
     * Defeats the enemy and returns a Treasure object if the enemy is dead.
     * If the enemy is alive, no treasure is dropped.
     * @return a Treasure object representing the loot, or null if the enemy is not dead
     */
    public Treasure defeat() {
        if (this.isDead()) {
            return new Treasure(this.getPosition(), false, getLoot());
        }
        return null;
    }

    /**
     * Gets the amount of loot that the enemy holds.
     * This is a random value between 100 and 300.
     * @return the amount of loot
     */
    public int getLoot() {
        return loot;
    }

    /**
     * Returns the maximum health points of the enemy.
     * This method can be overridden in subclasses to provide different health values.
     * @return the maximum health (default is 50)
     */
    @Override
    public int getMaxHealth() {
        return 50; // Example max health, can be overridden in subclasses
    }

    /**
     * Returns a string representation of the enemy, including its current position.
     * @return a string describing the enemy's position
     */
    @Override
    public String toString() {
        return "Enemy in position " + getPosition();
    }

    /**
     * Compares this enemy with another object for equality.
     * Two enemies are considered equal if they occupy the same position on the map.
     * @param obj the object to compare to
     * @return true if the other object is an Enemy and has the same position; false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Enemy other) {
            return getPosition().equals(other.getPosition());
        }
        return false;
    }


    // --- Fields ---
    /**
     * The amount of loot that the enemy holds.
     * This is a random value between 100 and 300.
     */
    private final int loot;
}