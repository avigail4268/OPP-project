
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

    public PlayerCharacter(String playerName, Position position,int health) {
        super(position, health);
        this.name = playerName;
        this.inventory = new Inventory();
        this.treasurePoints = 0;
    }

    public PlayerCharacter(PlayerCharacter player) {
        super(player.getPosition(), player.getHealth());
        this.inventory = player.getInventory();
        this.name = player.getName();
        this.treasurePoints = player.getTreasurePoints();
    }

    public String getName() {
        return name;
    }

    protected void setTreasurePoints(int treasurePoints) {
        this.treasurePoints = treasurePoints;
    }

    protected void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public boolean addToInventory(GameItem item) {
        if (item == null) {
            return false;
        }
        return inventory.addItem(item);
    }

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
        potion.interact(this);
        LogManager.addLog(getName() + " used a potion and healed " + healingAmount + " HP!");
    }

    public void usePowerPotion(PowerPotion potion) {
        int powerBoost = potion.getIncreaseAmount();
        potion.interact(this);
        LogManager.addLog(getName() + " used a power potion and gained " + powerBoost + " attack power!");
    }

    public Inventory getInventory () {
        return inventory;
    }

    public void updateTreasurePoint(int amount){
        this.treasurePoints += amount;
    }

    public int getTreasurePoints(){
        return treasurePoints;
    }

    @Override
    public int getMaxHealth() {
        return 100;
    }

    public boolean isMagicUser() {
        return false;
    }
    public void update() {}


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



}