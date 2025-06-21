package game.items;

import game.characters.PlayerCharacter;
import game.log.LogManager;
import game.map.Position;
import java.util.Random;

/**
 * Represents a health potion that can be collected and used by a player character.
 * When used, the potion restores a random amount of health to the player.
 */
public class Potion extends GameItem implements Interactable {

    /**
     * Creates a new health potion at the specified position.
     * @param position the initial position of the potion
     * @param blocksMovement true if the potion blocks movement, false otherwise
     * @param max the maximum amount of health the potion can restore
     * @param min the minimum amount of health the potion can restore
     */
    public Potion(Position position, boolean blocksMovement, int max, int min) {
        super(position, blocksMovement);
        this.increaseAmount = new Random().nextInt(min, max);
        this.isUsed = false;
        this.setDescription("This is a health potion, increased by " + increaseAmount);
    }

    /**
     * Creates a copy constructor for the potion.
     * @param position the initial position of the potion
     * @param blocksMovement true if the potion blocks movement, false otherwise
     * @param increaseAmount the fixed amount of health this potion restores
     */
    public Potion ( Position position, boolean blocksMovement , int increaseAmount) {
        super(position, blocksMovement);
        this.increaseAmount = increaseAmount;
        this.isUsed = false;
        this.setDescription("This is a health potion, increased by " + increaseAmount);
    }

    /**
     * Returns the amount of health this potion restores.
     * @return the health increase amount
     */
    public int getIncreaseAmount() {
        return increaseAmount;
    }

    /**
     * Creates a deep copy of this potion.
     * @return a new instance of Potion with the same properties
     */
    @Override
    public GameItem deepCopy() {
        return new Potion(getPosition(), this.isBlocksMovement(), increaseAmount);
    }

    /**
     * Returns the display symbol for the potion.
     * @return "HP" as the symbol representing the potion
     */
    @Override
    public String getDisplaySymbol() {
        return "Health Potion";
    }

    /**
     * Interacts with the player character by healing them and marking the potion as used.
     * @param c the player character interacting with the potion
     */
    @Override
    public void interact(PlayerCharacter c) {
        if (!isUsed) {
            int amount = Math.min(this.increaseAmount, 100 - c.getHealth());
            c.heal(amount);
            setIsUsed();
            if (c.getInventory().removeItem(this)) {
                LogManager.addLog("Health was " + (c.getHealth() - amount) + " now is " + c.getHealth());
            }
        }
    }

    /**
     * Collects the potion into the player character's inventory.
     * @param c the player character collecting the potion
     */
    @Override
    public void collect(PlayerCharacter c) {
        c.addToInventory(this);
    }

    /**
     * Compares this potion to another object for equality.
     * Two potions are equal if they have the same usage status and position.
     * @param obj the object to compare with
     * @return true if the potions are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Potion other) {
            return isUsed == other.isUsed && getPosition().equals(other.getPosition());
        }
        return false;
    }

    /**
     * Returns a string representation of the potion.
     * @return a string describing the potion and its position
     */
    @Override
    public String toString() {
        return "Health Potion " + getPosition();
    }

    /**
     * Returns whether the potion has already been used.
     * @return true if the potion has been used, false otherwise
     */
    protected boolean getIsUsed() {
        return isUsed;
    }

    /**
     * Marks the potion as used.
     * @return true if the potion was not already used and is now marked used, false otherwise
     */
    protected boolean setIsUsed() {
        if (!isUsed) {
            LogManager.addLog("Potion used!");
            this.isUsed = true;
            return true;
        }
        return false;
    }

    // --- Fields ---
    private final int increaseAmount;
    private boolean isUsed;
}
