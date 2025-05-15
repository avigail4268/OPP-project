package game.characters;

import game.core.Inventory;
import game.items.GameItem;
import game.items.Potion;
import game.items.PowerPotion;
import game.map.Position;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * PlayerCharacter represents a playable character in the game.
 * It extends AbstractCharacter and includes inventory management, potion usage,
 * and treasure collection features.
 */
public abstract class PlayerCharacter extends AbstractCharacter {

    /**
     * Constructs a new PlayerCharacter with a given name, position, and health.
     *
     * @param playerName the name of the player
     * @param position the starting position of the player
     * @param health the initial health of the player
     */
    public PlayerCharacter(String playerName, Position position,int health) {
        super(position, health);
        this.name = playerName;
        this.inventory = new Inventory();
        this.treasurePoints = 0;
    }

    /**
     * Gets the player's name.
     *
     * @return the name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Adds an item to the player's inventory.
     *
     * @param item the item to add
     * @return true if the item was added, false if null or failed
     */
    public boolean addToInventory(GameItem item) {
        if (item == null) {
            return false;
        }
        return inventory.addItem(item);
    }
    /**
     * Uses the first available Potion in the inventory that is usable via usePotion().
     *
     * @return true if a potion was found and used, false otherwise
     */
    public boolean useItem(GameItem item) {
        if (item instanceof PowerPotion) {
            usePowerPotion((PowerPotion) item);
            inventory.removeItem(item);
            return true;
        } else if (item instanceof Potion) {
            usePotion((Potion) item);
            inventory.removeItem(item);
            return true;
        }
        return false;
    }


    public void usePotion(Potion potion) {
        int healingAmount = potion.getIncreaseAmount();
        int currentHealth = getHealth();
        potion.interact(this);
        System.out.println(getName() + " used a potion and healed " + healingAmount + " HP!");
    }

    /**
     * Uses the first available PowerPotion in the inventory.
     *
     * @return true if a power potion was found and used, false otherwise
     */
    public void usePowerPotion(PowerPotion potion) {
        int powerBoost = potion.getIncreaseAmount();
        potion.interact(this);
        System.out.println(getName() + " used a power potion and gained " + powerBoost + " attack power!");
    }
    /**
     * Gets the player's inventory.
     *
     * @return the Inventory object
     */
    public Inventory getInventory () {
        return inventory;
    }

    /**
     * Updates the player's treasure points by the specified amount.
     *
     * @param amount the amount to add
     */
    public void updateTreasurePoint(int amount){
        this.treasurePoints += amount;

    }

    /**
     * Gets the current number of treasure points the player has.
     *
     * @return the treasure points
     */
    public int getTreasurePoints(){
        return treasurePoints;
    }

    public ReentrantLock getCombatLock() {
        return combatLock;
    }

    /**
     * Returns the maximum health points of the enemy.
     * This method can be overridden in subclasses to provide different health values.
     * @return the maximum health (default is 100 )
     */
    @Override
    public int getMaxHealth() {
        return 100;
    }
    // --- Fields ---
    /**
     * The name of the player character.
     */
    private final String name;
    /**
     * The inventory of the player character.
     * This inventory holds the items that the player has collected.
     */
    private Inventory inventory;
    /**
     * The number of treasure points the player has collected.
     * This value is used to track the player's progress in collecting treasures.
     */
    private int treasurePoints;
    /**
     * The maximum number of items the player can carry in their inventory.
     * This value is used to limit the size of the player's inventory.
     */

    private final ReentrantLock combatLock = new ReentrantLock(true); // fair lock
}
