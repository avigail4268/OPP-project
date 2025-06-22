package game.characters;
import game.core.Inventory;
import game.items.GameItem;
import game.items.Potion;
import game.items.PowerPotion;
import game.log.LogManager;
import game.map.Position;

/**
 * PlayerCharacter represents a playable character in the game.
 * It extends AbstractCharacter and includes inventory management, potion usage,
 * and treasure collection features.
 */
public abstract class PlayerCharacter extends AbstractCharacter {

    /**
     * Constructs a PlayerCharacter with the specified name, position, and health.
     * Initializes an empty inventory and sets the initial treasure points to zero.
     * @param playerName the name of the player character
     * @param position   the initial position on the map
     * @param health     the initial health value
     */
    public PlayerCharacter(String playerName, Position position, int health) {
        super(position, health);
        this.name = playerName;
        this.inventory = new Inventory();
        this.treasurePoints = 0;
    }

    /**
     * Copy constructor for creating a deep copy of an existing PlayerCharacter.
     * This constructor copies the position, health, inventory, name, and treasure points.
     * @param player the PlayerCharacter to copy
     */
    public PlayerCharacter(PlayerCharacter player) {
        super(player.getPosition(), player.getHealth());
        this.inventory = player.getInventory();
        this.name = player.getName();
        this.treasurePoints = player.getTreasurePoints();
    }

    /**
     * Creates a deep copy of this PlayerCharacter.
     * Subclasses should override this method to ensure proper copying of specific attributes.
     * @return a new instance of PlayerCharacter with copied attributes
     */
    public String getName() {
        return name;
    }

    /**
     * adds an item to the player's inventory.
     * @param item the GameItem to add to the inventory
     */
    public boolean addToInventory(GameItem item) {
        if (item == null) {
            return false;
        }
        return inventory.addItem(item);
    }

    /**
     * Uses an item from the player's inventory.
     * If the item is a PowerPotion, it applies its effect and removes it from the inventory.
     * If the item is a Potion, it applies its effect and removes it from the inventory.
     * @param item the GameItem to use
     * @return true if the item was successfully used, false otherwise
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

    /**
     * Retrieves the inventory of the player character.
     * @return the Inventory object containing the player's items
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Updates the treasure points of the player character.
     * This method is used to add or subtract treasure points based on game events.
     * @param amount the amount to update the treasure points by (can be positive or negative)
     */
    public void updateTreasurePoint(int amount) {
        this.treasurePoints += amount;
    }

    /**
     * Retrieves the current number of treasure points collected by the player character.
     * @return the number of treasure points
     */
    public int getTreasurePoints() {
        return treasurePoints;
    }

    /**
     * updates the player character's state.
     */
    public void update() {}

    /**
     * Returns the maximum health of the player character.
     */
    @Override
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * set the treasure points of the player character.
     * @param treasurePoints the new treasure points to set
     */
    protected void setTreasurePoints(int treasurePoints) {
        this.treasurePoints = treasurePoints;
    }

    /**
     * Sets the inventory of the player character.
     * @param inventory the new inventory to set
     */
    protected void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * Uses a Potion to heal the player character.
     * The healing amount is determined by the potion's increase amount.
     * Logs the action using LogManager.
     * @param potion the Potion to use
     */
    private void usePotion(Potion potion) {
        int healingAmount = potion.getIncreaseAmount();
        potion.interact(this);
        LogManager.addLog(getName() + " used a potion and healed " + healingAmount + " HP!");
    }

    /**
     * Uses a PowerPotion to increase the player's attack power.
     * The power boost amount is determined by the potion's increase amount.
     * Logs the action using LogManager.
     * @param potion the PowerPotion to use
     */
    private void usePowerPotion(PowerPotion potion) {
        int powerBoost = potion.getIncreaseAmount();
        potion.interact(this);
        LogManager.addLog(getName() + " used a power potion and gained " + powerBoost + " attack power!");
    }

    // --- Fields ---

    private final String name;
    private Inventory inventory;
    private int treasurePoints;
    private final int maxHealth = 100; // Default max health, can be overridden by subclasses
}


