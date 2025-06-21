package game.items;

import game.characters.PlayerCharacter;
import game.log.LogManager;
import game.map.Position;

/**
 * Represents a treasure that can be collected by a player character.
 * A treasure can either give a health potion, a power potion, or treasure points randomly.
 */
public class Treasure extends GameItem implements Interactable {

    /**
     * Creates a new treasure at the specified position.
     * @param position       the initial position of the treasure
     * @param blocksMovement true if the treasure blocks movement, false otherwise
     * @param value          the number of treasure points this treasure provides if selected
     */
    public Treasure(Position position, boolean blocksMovement, int value) {
        super(position, blocksMovement);
        this.value = value;
        collected = false;
        this.setDescription("This is a Treasure! It might be a health potion, power potion, or treasure points.");
    }

    @Override
    public GameItem deepCopy() {
        return new Treasure(getPosition(), this.isBlocksMovement(), this.value);
    }

    /**
     * Returns the display symbol for the treasure.
     * @return "TREASURE" as the symbol representing the treasure
     */
    @Override
    public String getDisplaySymbol() {
        return "Treasure";
    }

    /**
     * Interacts with the treasure, providing the player with a random reward.
     * Rewards can be: a health potion, treasure points, or a power potion.
     * @param c the player character interacting with the treasure
     */
    @Override
    public void interact(PlayerCharacter c) {
        double random = Math.random();
        if (random <= 1.0 / 3.0) {
            Potion potion = new Potion(this.getPosition(), false, 50, 10);
            potion.collect(c);
            this.collected = true;
        } else if (random <= 1.0 / 2.0 + 1.0 / 3.0) {
            c.updateTreasurePoint(value);
            LogManager.addLog("Collected " + value + " treasure points!");
            this.collected = true;
        } else {
            PowerPotion powerPotion = new PowerPotion(this.getPosition(), false, 5, 1);
            powerPotion.collect(c);
            this.collected = true;
        }
    }

    /**
     * Collects the treasure if it has not been collected yet.
     * @param c the player character collecting the treasure
     */
    @Override
    public void collect(PlayerCharacter c) {
        if (!collected) {
            this.interact(c);
        }
    }

    /**
     * Returns a string representation of the treasure.
     * @return a string describing the treasure and its position
     */
    @Override
    public String toString() {
        return "Treasure at position " + getPosition();
    }

    /**
     * Compares this treasure to another object for equality.
     * Two treasures are equal if they have the same value and position.
     * @param obj the object to compare with
     * @return true if the treasures are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Treasure other) {
            return other.value == this.value && this.getPosition().equals(other.getPosition());
        }
        return false;
    }

    // --- Fields ---
    private final int value;
    private boolean collected;
}
