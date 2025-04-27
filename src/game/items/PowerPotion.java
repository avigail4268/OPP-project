package game.items;

import game.characters.PlayerCharacter;
import game.map.Position;

/**
 * Represents a power potion that can be collected and used by a player character.
 * When used, the potion increases the player's power by a random amount.
 */
public class PowerPotion extends Potion {

    /**
     * Creates a new power potion at the specified position.
     *
     * @param position the initial position of the potion
     * @param blocksMovement true if the potion blocks movement, false otherwise
     * @param max the maximum power increase amount
     * @param min the minimum power increase amount
     */
    public PowerPotion(Position position, boolean blocksMovement, int max, int min) {
        super(position, blocksMovement, max, min);
        this.setDescription("This is a power potion, increases power by " + this.getIncreaseAmount());
    }

    /**
     * Interacts with the player character by increasing their power and marking the potion as used.
     *
     * @param c the player character interacting with the potion
     */
    @Override
    public void interact(PlayerCharacter c) {
        if (!getIsUsed()) {
            int amount = this.getIncreaseAmount();
            c.setPower(amount);
            if (this.setIsUsed()) {
                if (c.getInventory().removeItem(this)) {
                    System.out.println("Your power was " + (c.getPower() - amount) + " now is " + c.getPower());
                }
            }
        }
    }

    /**
     * Collects the power potion into the player character's inventory.
     *
     * @param c the player character collecting the potion
     */
    @Override
    public void collect(PlayerCharacter c) {
        if (c.addToInventory(this)) {
            System.out.println("Power Potion added to the inventory!");
        }
    }

    /**
     * Returns the display symbol for the power potion.
     *
     * @return "PP" as the symbol representing the power potion
     */
    @Override
    public String getDisplaySymbol() {
        return "PP";
    }

    /**
     * Compares this power potion to another object for equality.
     * Two power potions are equal if they have the same position and usage status.
     *
     * @param obj the object to compare with
     * @return true if the power potions are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PowerPotion other) {
            return this.getPosition().equals(other.getPosition()) && this.getIsUsed() == other.getIsUsed();
        }
        return false;
    }

    /**
     * Returns a string representation of the power potion.
     *
     * @return a string describing the power potion and its position
     */
    @Override
    public String toString() {
        return "Power Potion " + getPosition();
    }

    /**
     * Checks whether the power potion can be used in a usable-in-use context.
     *
     * @return false, power potions are not usable in use-potion mechanics
     */
    @Override
    public boolean isUsableInUsePotion() {
        return false;
    }
}
