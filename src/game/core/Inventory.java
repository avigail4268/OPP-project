package game.core;

import game.items.GameItem;
import game.log.LogManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an inventory that can store multiple game items.
 */
public class Inventory {

    /**
     * Constructs an empty inventory.
     */
    public Inventory() {
        items = new ArrayList<>();
    }

    /**
     * Adds a new item to the inventory.
     * @param item the GameItem to add
     * @return true if the item was added successfully, false if the item was null
     */
    public boolean addItem(GameItem item) {
        if (item != null) {
            items.add(item);
            LogManager.addLog("Added item: " + item.getDisplaySymbol());
            return true;
        }
        return false;
    }

    /**
     * Removes an item from the inventory.
     * @param item the GameItem to remove
     * @return true if the item was present and removed, false otherwise
     */
    public boolean removeItem(GameItem item) {
        if (items.contains(item)) {
            items.remove(item);
            LogManager.addLog("Removed item: " + item.getDisplaySymbol());
            return true;
        }
        return false;
    }

    /**
     * Returns the list of items currently in the inventory.
     * @return a List of GameItem objects
     */
    public List<GameItem> getItems() {
        return items;
    }

    /**
     * Compares this inventory to another object for equality.
     * Two inventories are considered equal if they contain the same items.
     * @param obj the object to compare with
     * @return true if the inventories are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Inventory other) {
            return this.items.equals(other.items);
        }
        return false;
    }

    /**
     * Returns a string representation of the inventory.
     * @return the string "Inventory"
     */
    @Override
    public String toString() {
        return "Inventory";
    }


    // --- Fields ---
    /**
     * The list of items in the inventory.
     */
    private List<GameItem> items;

    public Inventory deepCopy() {
        Inventory copy = new Inventory();
        for (GameItem item : items) {
            copy.addItem(item.deepCopy());
        }
        return copy;
    }
}
